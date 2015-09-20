package com.pep1.kitchenoffice.service.event;

import java.util.HashMap;
import java.util.Map;

import com.pep1.kitchenoffice.data.event.Event;

public class EventCreatedEvent {

	public static final String MAIL_TEMPLATE = "subscribe.location";
	
	private Event event;

	public EventCreatedEvent(Event event) {
		this.event = event;
	}
	
	public Map<String, Object> getReplacements(){
		
		Map<String, Object> replacements = new HashMap<String, Object>();
		replacements.put("event", getEvent());
		
		return replacements;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}
}
