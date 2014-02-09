package com.gentics.kitchenoffice.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.filters.Canvas;
import net.coobird.thumbnailator.geometry.Positions;
import net.coobird.thumbnailator.resizers.configurations.Antialiasing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.gentics.kitchenoffice.data.Image;
import com.gentics.kitchenoffice.repository.ImageRepository;

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

	@Value("${process.image.extension:jpg}")
	private String THUMB_EXTENSION;

	@Value("${process.image.quality:0.9}")
	private Float THUMB_QUALITY;

	@PostConstruct
	public void initialize() {
		log.debug("Initializing " + this.getClass().getSimpleName() + " instance ...");
	}

	@Transactional
	public Image findById(Long id) {
		Assert.notNull(id);
		Image output = repository.findOne(id);
		return output;
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

			Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName("jpg");
			ImageWriter writer = iter.next();
			ImageWriteParam iwp = writer.getDefaultWriteParam();

			iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			iwp.setCompressionQuality(1);

			FileImageOutputStream output = new FileImageOutputStream(file);
			writer.setOutput(output);

			IIOImage imageWrite = new IIOImage(bi, null, null);
			writer.write(null, imageWrite, iwp);
			writer.dispose();

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

//		File thumbFile = null;

		// create thumbnails
//		for (Integer size : sizes) {
//			thumbFile = createThumbFile(size, bi, image.getFileName());
//			storageService.getStorage().persistStorable(new Thumbnail(), thumbFile);
//		}

		// store file
		storageService.getStorage().persistStorable(image, file);
		// save entity
		repository.save(image);

		return image;
	}

//	private synchronized File createThumbFile(Integer size, BufferedImage image, String fileName) throws IOException {
//		long start = System.currentTimeMillis();
//
//		File thumbFile = storageService.createTempFile(Thumbnail.STORAGE_TYPE, FilenameUtils.getBaseName(fileName)
//				+ "." + size, THUMB_EXTENSION);
//
//		createThumb(image, thumbFile, size, size);
//
//		long end = System.currentTimeMillis() - start;
//		log.debug("creating thumb took " + end + " ms");
//
//		return thumbFile;
//	}

	public void removeImageObject(Image oldImage) throws IOException {

		try {
			// delete original
			storageService.getStorage().deleteStorable(oldImage);
			
		} catch (IllegalArgumentException e) {
			log.error("Error while deleting image.", e);
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

	@SuppressWarnings("unused")
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

		Thumbnails.of(image).size(newWidth, newHeight).outputQuality(THUMB_QUALITY).outputFormat(THUMB_EXTENSION).antialiasing(Antialiasing.ON)
				.addFilter(new Canvas(x, y, Positions.CENTER, true)).toFile(outputFile);

		return outputFile;
	}

	public Set<Integer> getSizes() {
		return sizes;
	}

	public String getThumbExtension() {
		return THUMB_EXTENSION;
	}

}