package com.gentics.kitchenoffice.webservice;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import com.gentics.kitchenoffice.data.event.Event;
import com.gentics.kitchenoffice.service.EventService;
import com.gentics.kitchenoffice.service.KitchenOfficeUserService;

@Component
@Scope("singleton")
@Path("/event")
public class EventWebService {
	
	private static Logger log = Logger.getLogger(EventWebService.class);
	
	@Autowired
	private KitchenOfficeUserService userService;

	@Autowired
	private EventService eventService;
	
	@GET
	@PreAuthorize("hasRole('ROLE_USER')")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Event> getMySounds(@QueryParam("page") Integer page, @QueryParam("size") Integer size) {

		if (page == null) {
			page = 0;
		}
		if (size == null) {
			size = 25;
		}

		return eventService.getEventsForUser(new PageRequest(page, size), userService.getUser());
	}

}
