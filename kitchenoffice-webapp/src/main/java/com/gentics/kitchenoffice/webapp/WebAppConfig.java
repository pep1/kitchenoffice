package com.gentics.kitchenoffice.webapp;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.ServletContextAware;

import com.gentics.kitchenoffice.repository.ImageRepository;
import com.gentics.kitchenoffice.storage.Storable;
import com.gentics.kitchenoffice.storage.Storage;
import com.gentics.kitchenoffice.storage.file.FileBuffer;
import com.gentics.kitchenoffice.storage.processing.ImageProcessor;

@Configuration
@ImportResource("classpath:spring/propertyContext.xml")
public class WebAppConfig implements ServletContextAware{
	
	private static Logger log = Logger.getLogger(WebAppConfig.class);
	
	private ServletContext context;
	
	@Value("${webapp.storageclass}")
	private Class<? extends Storage<Storable>> storageClass;
	
	@Value("${storage.temppath}")
	private String tempPath;

	@Value("${storage.imagepath}")
	private String imagePath;
	
	private String realImagePath;
	private String realTempPath;
	
	@Autowired 
	ImageRepository repository;
	
	@PostConstruct
	public void initialize() {
		
		log.debug("initializing WebApp Config.. ");
		
		realImagePath = context.getRealPath(imagePath);
		realTempPath = context.getRealPath(tempPath);
		
	}
	
	@Bean
	@Scope("singleton")
	public Storage<Storable> storage() {
		
		log.debug("initializing Storage Type: " + storageClass);
		
		try {
			return storageClass.newInstance();
		} catch (InstantiationException e) {
			log.error("failed to inialize Storage: " + e.getStackTrace());
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			log.error("failed to inialize Storage: " + e.getStackTrace());
		}
		
		return null;
	}
	
	@Bean
	@Scope("session")
	public ImageProcessor imageProcessor() {
		ImageProcessor ip = new ImageProcessor();
		
		ip.setImagePath(realImagePath);
		ip.setRepository(repository);
		ip.setStorage(storage());
		
		return ip;
	}
	
	@Bean
	@Scope("session")
	public FileBuffer fileBuffer() {
		FileBuffer fb = new FileBuffer();
		
		fb.setStorage(storage());
		fb.setTempPath(realTempPath);
		
		return fb;
		
	}
	
	
	@Override
	public void setServletContext(ServletContext context) {
		this.context = context;
		
	}

}
