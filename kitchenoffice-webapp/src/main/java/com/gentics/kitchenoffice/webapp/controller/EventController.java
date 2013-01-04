package com.gentics.kitchenoffice.webapp.controller;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.gentics.kitchenoffice.service.EventService;
import com.gentics.kitchenoffice.webapp.view.component.EventSelect;

@Controller
@Scope("session")
public class EventController {
	
	@Autowired
	private EventService service;
	
	@Autowired
	private ApplicationContext context;
	
	public EventService getService() {
		return service;
	}
	
	public EventSelect getNewEventSelect(Class<? extends com.gentics.kitchenoffice.data.event.Event> clazz) throws BeansException, ClassNotFoundException {
		
		String name = clazz.getSimpleName();
		
		return (EventSelect) context.getBean(name + "Select");
		
	}
	
}
