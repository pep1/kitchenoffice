package com.gentics.kitchenoffice.server;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.filters.Canvas;
import net.coobird.thumbnailator.geometry.Positions;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.gentics.kitchenoffice.data.Image;
import com.gentics.kitchenoffice.data.Thumbnail;
import com.gentics.kitchenoffice.repository.ImageRepository;
import com.gentics.kitchenoffice.service.StorageService;

@Service
@Scope("singleton")
public class ImageService {

	private static Logger log = LoggerFactory.getLogger(ImageService.class);

	@Autowired
	private StorageService storageService;
	
	@Autowired
	private ImageRepository repository;

	@Value("#{'${process.image.thumbs.sizes:40,160,480}'.split(',')}")
	private Set<Integer> sizes;

	@Value("process.image.extension")
	private String THUMB_EXTENSION = "jpg";

	@Value("process.image.quality:0.9")
	private Float THUMB_QUALITY;

	@PostConstruct
	public void initialize() {
		log.debug("Initializing " + this.getClass().getSimpleName() + " instance ...");
	}

	public Image createFromUrl(String urlString) throws IOException {
		URL url = new URL(urlString);
		return createImageObject(url.openStream());
	}

	public Image createImageObject(InputStream in) throws IOException {

		Image imageFile = null;
		File file = null;

		try {
			// write image to new temp file
			BufferedImage bi = ImageIO.read(in);
			file = storageService.createTempFile(Image.STORAGE_TYPE, THUMB_EXTENSION);
			ImageIO.write(bi, THUMB_EXTENSION, file);

			imageFile = createImageObject(file);

		} catch (IOException e) {
			log.error("Error while creating image from input stream", e);

			if (file != null) {
				file.delete();
			}

			throw new IOException("Error while creating image from input stream", e);
		}

		return imageFile;
	}

	public Image createImageObject(File file) throws IOException {

		Assert.notNull(file);

		// create buffered image
		BufferedImage bi = ImageIO.read(file);

		Image image = null;

		try {
			// read image data
			image = readImageData(file, bi);
		} catch (Exception e) {
			throw new IllegalArgumentException("Could not read image mime type");
		}

		bi.flush();

		File thumbFile = null;

		// create thumbnails
		for (Integer size : sizes) {
			thumbFile = createThumbFile(size, bi, FilenameUtils.getExtension(file.getName()));
			storageService.getStorage().persistStorable(new Thumbnail(), thumbFile);
		}

		// store file
		storageService.getStorage().persistStorable(image, file);
		// save entity
		repository.save(image);

		return image;
	}

	private synchronized File createThumbFile(Integer size, BufferedImage image, String fileName) throws IOException {
		long start = System.currentTimeMillis();

		File thumbFile = storageService.createTempFile(Thumbnail.STORAGE_TYPE, THUMB_EXTENSION);

		createThumb(image, thumbFile, size, size);

		long end = System.currentTimeMillis() - start;
		log.debug("creating thumb took " + end + " ms");

		return thumbFile;
	}

	public void removeImageObject(Image oldImage) throws IOException {
		// delete original
		storageService.getStorage().deleteStorable(oldImage);

		// delete thumbnails
		for (Thumbnail thumb : oldImage.getThumbs().values()) {
			storageService.getStorage().deleteStorable(thumb);
		}
		
		repository.delete(oldImage);
	}

	private Image readImageData(File file, BufferedImage bi) throws IOException {

		long start = System.currentTimeMillis();

		Image image = new Image();

		image.setHeight(bi.getHeight());
		image.setWidth(bi.getWidth());
		image.setFileName(file.getName());
		image.setSize(file.length());

		long end = System.currentTimeMillis() - start;
		log.debug("Reading Image data took " + end + " ms");

		return image;
	}

	private File createThumb(BufferedImage image, File outputFile, int x, int y) throws IOException {

		double ratio = (double) image.getWidth() / (double) image.getHeight();
		double scaleRatio;

		if (ratio > 1) {
			// Width larger than Height
			scaleRatio = (double) x / (double) image.getHeight();
		} else {
			scaleRatio = (double) y / (double) image.getWidth();
		}

		int newWidth = (int) Math.ceil((image.getWidth() * scaleRatio));
		int newHeight = (int) Math.ceil((image.getHeight() * scaleRatio));

		Thumbnails.of(image).size(newWidth, newHeight).outputQuality(THUMB_QUALITY).outputFormat(THUMB_EXTENSION)
				.addFilter(new Canvas(x, y, Positions.CENTER, true)).toFile(outputFile);

		return outputFile;
	}

}
