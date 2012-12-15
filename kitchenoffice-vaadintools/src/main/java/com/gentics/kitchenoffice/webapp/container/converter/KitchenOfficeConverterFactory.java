package com.gentics.kitchenoffice.webapp.container.converter;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.converter.DefaultConverterFactory;


@Component
@Scope("singleton")
public class KitchenOfficeConverterFactory extends DefaultConverterFactory{
	
	@Autowired
	private ApplicationContext context;
	
	@Override
	protected <PRESENTATION, MODEL> Converter<PRESENTATION, MODEL> findConverter(
            Class<PRESENTATION> presentationType, Class<MODEL> modelType) {
         if (modelType == Class.class) {
            // Class converters and more
            Converter<PRESENTATION, MODEL> converter = (Converter<PRESENTATION, MODEL>) createClassConverter(modelType);
            if (converter != null) {
                return converter;
            }
         }

        return super.findConverter(presentationType, modelType);

    }
	
	
	protected Converter<String, ?> createClassConverter(Class<?> sourceType) {
        return context.getBean(StringToResourceConverter.class);
    }
}
