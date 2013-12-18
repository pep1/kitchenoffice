package com.gentics.kitchenoffice.server.storage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.web.context.ServletContextAware;

public class FileStorageImpl implements Storage, ServletContextAware {

	private static Logger log = LoggerFactory.getLogger(FileStorageImpl.class);

	private ServletContext context;

	private String storageBasePath;

	@Override
	public void init(String basePath) {
		log.debug("Initializing FilestorageImpl instance ... ");
		
		Assert.notNull(basePath);
		
		this.storageBasePath = basePath;
	}

	@Override
	public URL getStorableUrl(Storable storable) {

		URL url = null;
		try {
			StringBuilder builder = new StringBuilder();
			builder.append(context.getContextPath());
			builder.append("/");
			builder.append(storageBasePath);
			builder.append(storable.getStorageType());
			builder.append("/");

			builder.append(storable.getFileName());
			url = new URL(builder.toString());

		} catch (MalformedURLException e) {
			log.error("Error while generating URL for Image", e);
		}

		return url;
	}

	public String getStorablePath(Storable storable) {
		Assert.notNull(storable);
		return context.getRealPath(storageBasePath + File.separator + storable.getStorageType());
	}

	@Override
	public void setServletContext(ServletContext arg0) {
		context = arg0;
	}

	@Override
	public File getFileFromStorable(Storable storable) {
		Assert.notNull(storable);
		return new File(getStorablePath(storable));
	}

	@Override
	public Storable persistStorable(Storable storable, File file) throws IOException {

		Assert.notNull(file);
		Assert.notNull(storable);

		String fileName = file.getName();
		String storablePath = getStorablePath(storable);

		log.debug("storing file: " + file.getAbsolutePath());
		log.debug("to:" + storablePath);

		// Make sure the file or directory exists and isn't write protected
		Assert.isTrue(file.exists(), "Move: no such file or directory: " + fileName);

		new File(storablePath).mkdirs();

		// new file where the storable should be moved to
		File newFile = new File(storablePath, file.getName());

		// overwrite
		if (newFile.exists())
			newFile.delete();

		// Move file to new directory
		if (file.renameTo(newFile)) {
			throw new IOException("could not move file, please check permissions!");
		}

		storable.setFileName(fileName);

		return storable;
	}

	@Override
	public void deleteStorable(Storable storable) throws IOException {

		Assert.notNull(storable);

		File file = getFileFromStorable(storable);
		Assert.notNull(file, "Could not get file from storable.");

		String fileName = file.getName();

		// Make sure the file or directory exists and isn't write protected
		Assert.isTrue(file.exists(), "Move: no such file or directory: " + fileName);
		Assert.isTrue(file.canWrite(), "write protected: " + fileName);

		// If it is a directory, make sure it is empty
		if (file.isDirectory()) {
			String[] files = file.list();
			if (files.length > 0)
				throw new IllegalArgumentException("Delete: directory not empty: " + fileName);
		}

		// Attempt to delete it
		boolean success = file.delete();

		if (!success) {
			throw new IOException("Delete: deletion failed");
		}
	}

}
