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

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.filters.Canvas;
import net.coobird.thumbnailator.geometry.Positions;
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
import com.gentics.kitchenoffice.webapp.util.Filename;

public class ImageProcessor {

	private static Logger log = Logger.getLogger(ImageProcessor.class);

	private String imagePath;

	private Storage<Storable> storage;

	private ImageRepository repository;
	
	private Integer[] sizes = new Integer[] {40, 160};
	
	private static final String EXTENSION = "jpg";

	public com.gentics.kitchenoffice.data.Image createImageObject(File file)
			throws IOException, MagicParseException, MagicMatchNotFoundException, MagicException {

		Assert.notNull(file, "File should not be null!");

		file = storage.moveFile(file, imagePath);
		
		// create thumbnails
		for(Integer size : sizes) {
			createThumbFile(size, file);
		}
		
		com.gentics.kitchenoffice.data.Image image = null;

		image = readImageData(file);

		return image;
	}
	
	private synchronized void createThumbFile(Integer size, File file) throws IOException {
		long start = System.currentTimeMillis();
		
		String fileName = Filename.filename(file.getName());
		
		File thumb = new File(imagePath + File.separator + "thumb_" + size.toString(),
				fileName + Filename.EXTENSIONSEPARATOR + EXTENSION);
		createFastThumb(file, thumb, size, size, (float) 0.8);
		
		long end = System.currentTimeMillis() - start;
		log.debug("creating thumb took " + end + " ms");
	}

	public boolean removeImageObject(
			com.gentics.kitchenoffice.data.Image oldImage) {

		Assert.notNull(oldImage, "Image to remove should not be null!");

		try {
			
			boolean success = true;
			File toRemove = new File(imagePath, oldImage.getFileName());
			
			// delete original
			if(!storage.deleteFile(toRemove)) {
				success = false;
			}
			
			// delete thumbnails
			for(Integer size : sizes) {
				File toRemoveThumb = new File(imagePath + File.separator
						+ "thumb_" + size, oldImage.getFileName());
				
				if(!storage.deleteFile(toRemoveThumb)) {
					success = false;
				}
			}

			if (success) {

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

		long start = System.currentTimeMillis();
		
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
		
		long end = System.currentTimeMillis() - start;
		log.debug("Reading Image data took " + end + " ms");

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
	
	private synchronized File createFastThumb(File inputFile, File outputFile, int x, int y, float quality) throws IOException {
		
		BufferedImage image;

		image = ImageIO.read(inputFile);
		
		double ratio = (double) image.getWidth()
				/ (double) image.getHeight();
		double scaleRatio;
		
		if (ratio > 1) {
			// Width larger than Height
			scaleRatio = (double) x / (double) image.getHeight();
		} else {
			scaleRatio = (double) y / (double) image.getWidth();
		}
		
		int newWidth = (int) Math.ceil((image.getWidth() * scaleRatio));
		int newHeight = (int) Math.ceil((image.getHeight() * scaleRatio));
		
		Thumbnails.of(image)
			.size(newWidth, newHeight)
			.outputQuality(quality)
			.outputFormat("jpg")
			.addFilter(new Canvas(x, y, Positions.CENTER, true))
			.toFile(outputFile);
		
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
