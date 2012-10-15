package com.gentics.kitchenoffice.webapp;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Scope;

import com.gentics.kitchenoffice.storage.Storable;
import com.gentics.kitchenoffice.storage.Storage;

@Configuration
@ImportResource("classpath:spring/propertyContext.xml")
public class WebAppConfig {
	
	private static Logger log = Logger.getLogger(WebAppConfig.class);
	
	@Value("${webapp.storageclass}")
	private Class<? extends Storage<Storable>> storageClass;
	
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

}
