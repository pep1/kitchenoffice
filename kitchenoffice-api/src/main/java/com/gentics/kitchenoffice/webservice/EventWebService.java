package com.gentics.kitchenoffice.webservice;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.NumberUtils;

import com.gentics.kitchenoffice.data.Job;
import com.gentics.kitchenoffice.data.event.Event;
import com.gentics.kitchenoffice.service.EventService;
import com.gentics.kitchenoffice.service.JobService;
import com.gentics.kitchenoffice.service.KitchenOfficeUserService;

@Component
@Scope("singleton")
@Path("/events")
public class EventWebService {

	private static Logger log = Logger.getLogger(EventWebService.class);

	@Autowired
	private KitchenOfficeUserService userService;

	@Autowired
	private EventService eventService;
	
	@Autowired
	private JobService jobService;
	
	@PostConstruct
	public void initialize() {
		log.debug("Initializing " + this.getClass().getSimpleName() + " instance ...");
	}

	@GET
	@PreAuthorize("hasRole('ROLE_USER')")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Event> getEvents(@QueryParam("page") Integer page, @QueryParam("size") Integer size) {

		log.debug("calling getEvents");

		if (page == null) {
			page = 0;
		}
		if (size == null) {
			size = 25;
		}

		return eventService.getFutureEvents(new PageRequest(page, size));
	}
	
	@GET
	@Path("/{id}/attend")
	@PreAuthorize("hasRole('ROLE_USER')")
	@Produces(MediaType.APPLICATION_JSON)
	public Event attendEvent(@PathParam("id") String id) {
		return attendEventWithJob(id, null);
	}
	
	@GET
	@Path("/{id}/attend/{jobid}")
	@PreAuthorize("hasRole('ROLE_USER')")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Event attendEventWithJob(@PathParam("id") String id, @PathParam("jobid") String jobId) {

		log.debug("calling attendEvent");
		
		Long parsedId = NumberUtils.parseNumber(id, Long.class);
		Assert.notNull(parsedId);
		
		Job job = null;
		
		// if there is given a job, is should be found in the repository
		if(StringUtils.isNotBlank(jobId)) {
			job = jobService.getJobByName(jobId);
			Assert.notNull(job);
		}
		
		return eventService.attendEvent(eventService.getEventById(parsedId), job);
	}

	@POST
	@PreAuthorize("hasRole('ROLE_USER')")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Event createEvent(Event event) {

		log.debug("calling createEvent");

		try {
			Assert.notNull(event);
			
			eventService.saveEvent(event);
			// set actual logged in user as creator
			event.setCreator(userService.getUser());
			// set creation date to now
			event.setCreationDate(new Date());
			// TODO validate event
			eventService.saveEvent(event);
			
			return event;

		} catch (ConstraintViolationException e) {

			String message = "";
			Iterator<?> iterator = e.getConstraintViolations().iterator();

			while (iterator.hasNext()) {
				ConstraintViolation<?> current = (ConstraintViolation<?>) iterator.next();
				message += current.getPropertyPath().toString() + ": ";
				message += current.getMessage() + " ";
			}

			throw new WebApplicationException(Response.status(Response.Status.NOT_ACCEPTABLE).entity(message).build());
		} catch (IllegalArgumentException e) {
			log.error("Failed to fetch event", e);
			throw new WebApplicationException(Response.status(Response.Status.NOT_ACCEPTABLE).entity("Failed to fetch event").build());
		}

	}

}
