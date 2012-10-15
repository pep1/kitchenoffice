package com.gentics.kitchenoffice.storage.file;

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

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.gentics.kitchenoffice.storage.Storable;
import com.gentics.kitchenoffice.storage.Storage;

@Component
@Scope("singleton")
public class ImageProcessor {

	private static Logger log = Logger.getLogger(ImageProcessor.class);

	@Value("${storage.temppath}")
	private String tempPath = "C:/TEMP";

	@Value("${storage.imagepath}")
	private String imagePath;

	@Autowired
	private Storage<Storable> storage;

	public com.gentics.kitchenoffice.data.Image createImageObject(File file) throws IOException {

		com.gentics.kitchenoffice.data.Image image = null;

		file = storage.moveFile(file, imagePath);
		
		File thumb120 = new File(imagePath + File.separator + "thumb_120" + File.separator, file.getName());

		createThumb(file, thumb120, 120, 120, (float) 0.6);

		image = readImageData(file);

		return image;
	}

	private com.gentics.kitchenoffice.data.Image readImageData(File file)
			throws IOException {

		BufferedImage bi = ImageIO.read(file);

		com.gentics.kitchenoffice.data.Image image = new com.gentics.kitchenoffice.data.Image();

		image.setHeight(bi.getHeight());
		image.setWidth(bi.getWidth());
		// TODO get mimetype
		// image.setType(bi.get);

		image.setFileName(file.getName());
		image.setFilePath(file.getAbsolutePath());

		image.setSize(file.length());

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
			iwp.setCompressionQuality(1);

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

				writer = null;
			}

		} catch (IOException e) {
			log.debug("something went wrong with the image processing: "
					+ e.getStackTrace());
		}

		return outputFile;
	}

}
