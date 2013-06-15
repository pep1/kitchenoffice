package com.gentics.kitchenoffice.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.gentics.kitchenoffice.data.event.Event;
import com.gentics.kitchenoffice.repository.EventRepository;

@Service
@Scope("singleton")
public class EventService {

	private static Logger log = Logger.getLogger(EventService.class);

	@Autowired
	private EventRepository eventRepository;

	@PostConstruct
	public void initialize() {
		log.debug("initializing " + this.getClass().getSimpleName()
				+ " instance ...");
	}

	public List<String> getAvailableEventTypes() {

		List<String> availableEventTypes = new ArrayList<String>();

		return availableEventTypes;
	}

	public List<Event> getEvents(PageRequest pagerequest) {
		return eventRepository.findAll(pagerequest).getContent();
	}

	public Event saveEvent(Event event) {
		return eventRepository.save(event);
	}
}
