package com.gentics.kitchenoffice.service;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.context.ServletContextAware;

import com.gentics.kitchenoffice.server.storage.Storage;

@Service
@Scope("singleton")
public class StorageService {

	private static Logger log = LoggerFactory.getLogger(StorageService.class);

	private SecureRandom random = new SecureRandom();

	private Storage storage;
	
	@Autowired
	private ServletContext servletContext;

	@Value("${storage.class}")
	private Class<? extends Storage> storageClazz;
	
	@Value("${storage.basepath}")
	private String basePath;

	@Value("${storage.temppath}")
	private String tempPath;

	@PostConstruct
	public void initialize() throws IOException, SecurityException, InstantiationException, IllegalAccessException {
		log.debug("Initializing " + this.getClass().getSimpleName() + " instance ... ");

		Assert.notNull(storageClazz);
		Assert.notNull(basePath);
		
		// initialize storage
		this.storage = storageClazz.newInstance();
		storage.init(basePath);
		
		if(storage instanceof ServletContextAware) {
			((ServletContextAware)storage).setServletContext(servletContext);
		}

		Assert.notNull(tempPath);
		File tempDir = new File(tempPath);

		// auto create temp directory if not exist
		if (!tempDir.mkdirs()) {
			throw new IOException("Could not create tempdir in filesystem.");
		}
	}

	public File createTempFile(String type, String extension) {

		String filename = getUniqueFileName(extension);

		log.debug("creating new temp file: " + tempPath + File.separator + filename);
		File f = new File(tempPath, filename);

		return f;
	}

	private String getUniqueFileName(String extension) {

		Assert.hasLength(extension);

		return new StringBuilder().append(RandomStringUtils.random(16, 0, 0, true, true, null, random)).append(".")
				.append(extension).toString();
	}
}
