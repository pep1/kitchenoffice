package com.gentics.kitchenoffice.service;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.gentics.kitchenoffice.data.event.Event;
import com.gentics.kitchenoffice.data.user.User;

@Service
@Scope("singleton")
public class EventService {
	
	private static Logger log = Logger.getLogger(EventService.class);
	
	@Autowired
	private EventService eventService;
	
	private Collection<Class<? extends Event>> availableEvents = new ArrayList<Class<? extends Event>>();
	
	@PostConstruct
	public void initialize() {
		log.debug("initializing " + this.getClass().getSimpleName() + " instance ...");
	}
	
	public Collection<Class<? extends Event>> getAvailableEvents() {
		
		if(availableEvents.size() == 0) {
			log.error("no events found!");
		}
		
		return availableEvents;
	}

	public List<Event> getEventsForUser(PageRequest pageRequest, User user) {
		// TODO Auto-generated method stub
		return new ArrayList<Event>();
	}
}
