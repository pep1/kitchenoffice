package com.pep1.kitchenoffice.service;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.pep1.kitchenoffice.service.event.EventCreatedEvent;
import com.google.common.eventbus.Subscribe;

@Service
@Scope("singleton")
public class MessageService {

	private static Logger log = LoggerFactory.getLogger(MessageService.class);

	@Autowired
	private MailService mailService;

	@PostConstruct
	public void initialize() {
		log.debug("Initializing " + this.getClass().getSimpleName() + " instance ...");
	}

	@Subscribe
	public void onEventCreated(EventCreatedEvent event) {
		log.debug("Event " + event.getEvent() + " created by " + event.getEvent().getCreator());

		if (event.getEvent().getLocation() != null) {

			mailService.sendMailToUsers(event.getEvent().getLocation().getSubscribers(),
					"New Food Event in your subscribed Location " + event.getEvent().getLocation().getName(),
					EventCreatedEvent.MAIL_TEMPLATE + "." + "vm", event.getReplacements());
		}
	}
}
