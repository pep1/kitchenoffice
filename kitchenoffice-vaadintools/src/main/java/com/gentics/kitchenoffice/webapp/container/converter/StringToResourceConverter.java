package com.gentics.kitchenoffice.webapp.container.converter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.gentics.kitchenoffice.data.event.Event;
import com.gentics.kitchenoffice.data.event.LocaleResourceName;
import com.gentics.kitchenoffice.data.event.OrderEvent;
import com.gentics.kitchenoffice.resourcemessage.ResourceMessage;
import com.gentics.kitchenoffice.service.EventService;
import com.gentics.kitchenoffice.webapp.resource.ResourceBundleUiMessageSource;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.server.VaadinSession;


@SuppressWarnings("serial")
@Component
@Scope("prototype")
public class StringToResourceConverter implements Converter<String, Class<Event>> {

	@Autowired
	private EventService eventService;
	
	private Collection<Class<? extends Event>> availableEvents = new ArrayList<Class<? extends Event>>();
	
	@Autowired
	private ResourceBundleUiMessageSource ms;
	
	@PostConstruct
	public void postConstruct() {
		this.availableEvents = eventService.getAvailableEvents();
	}
	
	
	public Class convertToModel(String text, Locale locale)
            throws ConversionException {
		
		if(text == null) {
			return null;
		}
		
		if(locale == null) {
			locale = VaadinSession.getCurrent().getLocale();
		}
		
		for(Class<? extends Event> event : availableEvents) {
			LocaleResourceName annotation = event.getAnnotation(LocaleResourceName.class);
			String resourceKey = annotation.value();
			
			if(text.equals(ms.getMessage(resourceKey, locale))) {
				return event;
			}
		}
		
		return null;
    }

	public String convertToPresentation(Class<Event> value, Locale locale)
			throws com.vaadin.data.util.converter.Converter.ConversionException {
		
		if(value == null) {
			return null;
		}
		
		if(locale == null) {
			locale = VaadinSession.getCurrent().getLocale();
		}
		
		LocaleResourceName annotation = value.getAnnotation(LocaleResourceName.class);
		String resourceKey = annotation.value();
		
		return ms.getMessage(resourceKey, locale);
	}

	public Class<String> getPresentationType() {
		return String.class;
	}


	public Class<Class<Event>> getModelType() {
		Class clazz = Event.class;
		return clazz;
	}

    
}
