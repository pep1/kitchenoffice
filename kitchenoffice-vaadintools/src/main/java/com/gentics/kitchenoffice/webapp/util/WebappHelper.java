package com.gentics.kitchenoffice.webapp.util;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import com.gentics.kitchenoffice.data.Image;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Resource;


@Configuration
@ImportResource("classpath:spring/propertyContext.xml")
public class WebappHelper {
	
	private static Logger log = Logger.getLogger(WebappHelper.class);
	
	@Value("${webapp.imagepath}")
	private String imagePath;
	
	public Resource getImageThumbnail(Image image, Integer size) {
		
		String url;
		
		if (image == null || image.getFilePath() == null
				|| "".equals(image.getFilePath())) {
			url = imagePath + "thumb_" + size.toString() + "/no_image.png";
		} else {
			String fileName = Filename.filename(image.getFileName());
			url = imagePath + "thumb_" + size.toString() + "/" + fileName + Filename.EXTENSIONSEPARATOR + "jpg";
		}
		
		return new ExternalResource(url);
	}

}
