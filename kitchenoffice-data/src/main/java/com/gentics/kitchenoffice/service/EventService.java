package com.gentics.kitchenoffice.service;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.gentics.kitchenoffice.data.event.Event;
import com.gentics.kitchenoffice.repository.CookEventRepository;
import com.gentics.kitchenoffice.repository.EatOutEventRepository;
import com.gentics.kitchenoffice.repository.OrderEventRepository;

@Service
@Scope("singleton")
public class EventService {
	
	private static Logger log = Logger.getLogger(EventService.class);
	
	public static final Class EVENT_CLASS = Event.class;

	@Autowired
	private OrderEventRepository orderEventRepo;
	
	@Autowired
	private CookEventRepository cookEventRepo;
	
	@Autowired
	private EatOutEventRepository eatOutEventRepo;
	
	private Collection<Class<? extends Event>> availableEvents = new ArrayList<Class<? extends Event>>();
	
	@PostConstruct
	public void initialize() {
		findAvailableEvents();
	}
	
	private void findAvailableEvents() {
		
		log.debug("Looking for available Events in package: " + EVENT_CLASS.getPackage().getName());
		
		Reflections reflections = new Reflections(EVENT_CLASS.getPackage().getName());    
		Set<Class<? extends Event>> classes = reflections.getSubTypesOf(EVENT_CLASS);
		
		availableEvents.addAll(classes);
	}
	
	public Collection<Class<? extends Event>> getAvailableEvents() {
		
		if(availableEvents.size() == 0) {
			log.error("no events found!");
		}
		
		return availableEvents;
	}
}
