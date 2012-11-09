package com.gentics.kitchenoffice.storage.processing;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;

import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicException;
import net.sf.jmimemagic.MagicMatch;
import net.sf.jmimemagic.MagicMatchNotFoundException;
import net.sf.jmimemagic.MagicParseException;

import org.apache.log4j.Logger;
import org.imgscalr.Scalr;
import org.springframework.util.Assert;

import com.gentics.kitchenoffice.repository.ImageRepository;
import com.gentics.kitchenoffice.storage.Storable;
import com.gentics.kitchenoffice.storage.Storage;

public class ImageProcessor {

	private static Logger log = Logger.getLogger(ImageProcessor.class);

	private String imagePath;

	private Storage<Storable> storage;

	private ImageRepository repository;

	public com.gentics.kitchenoffice.data.Image createImageObject(File file)
			throws IOException, MagicParseException, MagicMatchNotFoundException, MagicException {

		Assert.notNull(file, "File should not be null!");

		file = storage.moveFile(file, imagePath);

		File thumb160 = new File(imagePath + File.separator + "thumb_160",
				file.getName());

		createThumb(file, thumb160, 160, 160, (float) 0.8);
		
		com.gentics.kitchenoffice.data.Image image = null;

		image = readImageData(file);

		return image;
	}

	public boolean removeImageObject(
			com.gentics.kitchenoffice.data.Image oldImage) {

		Assert.notNull(oldImage, "Image to remove should not be null!");

		try {
			File toRemove = new File(imagePath, oldImage.getFileName());
			File toRemoveThumb = new File(imagePath + File.separator
					+ "thumb_160", oldImage.getFileName());

			if (storage.deleteFile(toRemove)
					&& storage.deleteFile(toRemoveThumb)) {

				if (!oldImage.isNew()) {
					repository.delete(oldImage);
				}

				return true;
			}
		} catch (Exception e) {
			log.error("Failed to delete Image: " + e.getMessage());
		}

		return false;

	}

	private com.gentics.kitchenoffice.data.Image readImageData(File file)
			throws IOException, MagicParseException, MagicMatchNotFoundException, MagicException {

		BufferedImage bi = ImageIO.read(file);

		com.gentics.kitchenoffice.data.Image image = new com.gentics.kitchenoffice.data.Image();

		image.setHeight(bi.getHeight());
		image.setWidth(bi.getWidth());
		
		MagicMatch match = Magic.getMagicMatch(file, true);
		image.setType(match.getMimeType());

		image.setFileName(file.getName());
		image.setFilePath(file.getAbsolutePath());

		image.setSize(file.length());
		
		bi.flush();

		return image;
	}

	private File createThumb(File inputFile, File outputFile, int x, int y,
			float quality) {

		try {
			BufferedImage image;

			image = ImageIO.read(inputFile);

			Iterator<ImageWriter> iter = ImageIO
					.getImageWritersByFormatName("jpg");

			ImageWriter writer = iter.next();
			ImageWriteParam iwp = writer.getDefaultWriteParam();

			iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			iwp.setCompressionQuality(quality);

			FileImageOutputStream output = new FileImageOutputStream(outputFile);
			writer.setOutput(output);

			double ratio = (double) image.getWidth()
					/ (double) image.getHeight();
			BufferedImage processedImage = null;

			if ((image.getWidth() < x)) {

				processedImage = new BufferedImage(x, y,
						BufferedImage.TYPE_INT_RGB);
				Graphics2D graphic = processedImage.createGraphics();
				graphic.setColor(Color.darkGray);
				graphic.drawRect(0, 0, x, y);
				graphic.fillRect(0, 0, x, y);
				graphic.drawImage(image, null,
						((x / 2) - (int) (image.getWidth() / 2.0)),
						((y / 2) - (int) (image.getHeight() / 2.0)));

			} else {

				double scaleRatio;

				if (ratio > 1) {
					// Width larger than Height
					scaleRatio = (double) x / (double) image.getHeight();
				} else {
					scaleRatio = (double) y / (double) image.getWidth();
				}

				int newX = (int) (image.getWidth() * scaleRatio);
				int newY = (int) (image.getHeight() * scaleRatio);

				Image scaledImage = image.getScaledInstance(newX, newY,
						Image.SCALE_SMOOTH);
				processedImage = new BufferedImage(x, y,
						BufferedImage.TYPE_INT_RGB);

				Graphics2D graphic = processedImage.createGraphics();
				graphic.setColor(Color.darkGray);
				graphic.drawRect(0, 0, x, y);
				graphic.fillRect(0, 0, x, y);

				int newPosX = (int) ((x / 2) - (int) (newX / 2.0));
				int newPosY = (int) ((y / 2) - (int) (newY / 2.0));

				graphic.drawImage(scaledImage, newPosX, newPosY, null);

				IIOImage imageWrite = new IIOImage(processedImage, null, null);
				writer.write(null, imageWrite, iwp);
				writer.dispose();

				output.close();

				writer = null;
				output = null;
			}

		} catch (IOException e) {
			log.debug("something went wrong with the image processing: " + e.getMessage());
			e.printStackTrace();
		}

		return outputFile;
	}

	@SuppressWarnings("unused")
	private File createQualityThumb(File inputFile, File outputFile, int x,
			int y, float quality) {

		try {
			BufferedImage image = ImageIO.read(inputFile);

			BufferedImage rescaledImage = Scalr.resize(image,
					Scalr.Method.QUALITY, Scalr.Mode.AUTOMATIC, x, y,
					Scalr.OP_ANTIALIAS);

			ImageIO.write(rescaledImage, "jpg", outputFile);

		} catch (IOException e) {
			log.error("something went wrong with the image processing: " + e.getMessage());
			e.printStackTrace();
		}

		return outputFile;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public void setStorage(Storage<Storable> storage) {
		this.storage = storage;
	}

	public void setRepository(ImageRepository repository) {
		this.repository = repository;
	}

}
