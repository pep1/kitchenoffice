package com.gentics.kitchenoffice.service;

import java.io.File;
import java.io.IOException;
import java.net.URL;
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

	@Autowired(required = false)
	private ServletContext servletContext;

	@Value("${storage.class}")
	private Class<? extends Storage> storageClazz;

	@Value("${storage.hostname:localhost}")
	private String hostname;

	@Value("${storage.port:8080}")
	private Integer port;

	@Value("${storage.protocol:http}")
	private String protocol;

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
		storage.init(new URL(protocol, hostname, port, ""), basePath);

		if (storage instanceof ServletContextAware) {
			((ServletContextAware) storage).setServletContext(servletContext);
		}

		Assert.notNull(tempPath);
		File tempDir = new File(tempPath);

		if (!tempDir.isDirectory()) {
			// auto create temp directory if not exist
			if (!tempDir.mkdirs()) {
				throw new IOException("Could not create tempdir in filesystem.");
			}
		}

		if (!tempDir.canWrite()) {
			throw new InstantiationException("Can not write in temp file directory " + tempPath);
		}
	}

	public File createTempFile(String type, String extension) throws IOException {

		String filename = getUniqueFileName(extension);
		String filePath = tempPath + File.separator + type;
		File dir = new File(filePath);

		// auto create temp directory if not exist
		if (!dir.isDirectory()) {
			if (!dir.mkdirs()) {
				throw new IOException("Could not create tempdir in filesystem.");
			}
		}

		if (!dir.canWrite()) {
			throw new IOException("Can not write to temp file path " + dir.getAbsolutePath());
		}

		log.debug("creating new temp file: " + filePath + File.separator + filename);
		File f = new File(dir, filename);

		return f;
	}
	
	public File createTempFile (String type, String basename, String extension) throws IOException {
		
		String filePath = tempPath + File.separator + type;
		File dir = new File(filePath);

		// auto create temp directory if not exist
		if (!dir.isDirectory()) {
			if (!dir.mkdirs()) {
				throw new IOException("Could not create tempdir in filesystem.");
			}
		}

		if (!dir.canWrite()) {
			throw new IOException("Can not write to temp file path " + dir.getAbsolutePath());
		}

		log.debug("creating new temp file: " + filePath + File.separator + basename + "." + extension);
		File f = new File(dir, basename + "." + extension);

		return f;
	}

	private String getUniqueFileName(String extension) {

		Assert.hasLength(extension);

		return new StringBuilder().append(RandomStringUtils.random(16, 0, 0, true, true, null, random)).append(".")
				.append(extension).toString();
	}

	public Storage getStorage() {
		return storage;
	}

}
