/*
 * @author: Leonard Osang <leonard@osang.at>
 */
package com.gentics.kitchenoffice.webservice;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.NumberUtils;

import com.gentics.kitchenoffice.data.Comment;
import com.gentics.kitchenoffice.data.Job;
import com.gentics.kitchenoffice.data.event.Event;
import com.gentics.kitchenoffice.service.EventService;
import com.gentics.kitchenoffice.service.JobService;
import com.gentics.kitchenoffice.service.UserService;
import com.sun.jersey.api.NotFoundException;

/**
 * The Class EventWebService.
 * 
 * Provides event CRUD functionality.
 */
@Component
@Scope("singleton")
@Path("/events")
public class EventWebService {

	/** The log. */
	private static Logger log = Logger.getLogger(EventWebService.class);

	/** The user service. */
	@Autowired
	private UserService userService;

	/** The event service. */
	@Autowired
	private EventService eventService;

	/** The job service. */
	@Autowired
	private JobService jobService;

	/**
	 * Initialize.
	 */
	@PostConstruct
	public void initialize() {
		log.debug("Initializing " + this.getClass().getSimpleName() + " instance ...");
	}

	/**
	 * Gets the events.
	 *
	 * @param page the page
	 * @param size the size
	 * @return the events
	 */
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
	
	/**
	 * Gets the past events.
	 *
	 * @param page the page
	 * @param size the size
	 * @return the past events
	 */
	@GET
	@Path("/past")
	@PreAuthorize("hasRole('ROLE_USER')")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Event> getPastEvents(@QueryParam("page") Integer page, @QueryParam("size") Integer size) {

		log.debug("calling getPastEvents");

		if (page == null) {
			page = 0;
		}
		if (size == null) {
			size = 25;
		}

		return eventService.getPastEvents(new PageRequest(page, size));
	}
	
	/**
	 * Gets the my events.
	 *
	 * @param page the page
	 * @param size the size
	 * @return the my events
	 */
	@GET
	@Path("/mine")
	@PreAuthorize("hasRole('ROLE_USER')")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Event> getMyEvents(@QueryParam("page") Integer page, @QueryParam("size") Integer size) {

		log.debug("calling getMyEvents");

		if (page == null) {
			page = 0;
		}
		if (size == null) {
			size = 25;
		}

		return eventService.getEventsOfUser(new PageRequest(page, size));
	}
	
	/**
	 * Gets the my attended events.
	 *
	 * @param page the page
	 * @param size the size
	 * @return the my attended events
	 */
	@GET
	@Path("/attended")
	@PreAuthorize("hasRole('ROLE_USER')")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Event> getMyAttendedEvents(@QueryParam("page") Integer page, @QueryParam("size") Integer size) {

		log.debug("calling getMyAttendedEvents");

		if (page == null) {
			page = 0;
		}
		if (size == null) {
			size = 25;
		}

		return eventService.getFutureEvents(new PageRequest(page, size));
	}
	
	/**
	 * Gets the event by the given Id.
	 *
	 * @param id the id
	 * @return the event
	 */
	@GET
	@Path("/{id}")
	@PreAuthorize("hasRole('ROLE_USER')")
	@Produces(MediaType.APPLICATION_JSON)
	public Event getEvent(@PathParam("id") String id) {
		Long parsedId = NumberUtils.parseNumber(id, Long.class);
		Assert.notNull(parsedId);
		
		Event event = eventService.getEventById(parsedId);
		
		if(event == null) {
			throw new NotFoundException("Sorry, no event found with id " + parsedId);
		}
		
		return event;
	}
	
	/**
	 * Removes the event.
	 *
	 * @param id the id
	 */
	@DELETE
	@Path("/{id}")
	@PreAuthorize("hasRole('ROLE_USER')")
	@Produces(MediaType.APPLICATION_JSON)
	public void removeEvent(@PathParam("id") String id) {
		eventService.deleteEvent(getEvent(id));
	}

	/**
	 * Attend event.
	 *
	 * @param id the id
	 * @return the event
	 */
	@GET
	@Path("/{id}/attend")
	@PreAuthorize("hasRole('ROLE_USER')")
	@Produces(MediaType.APPLICATION_JSON)
	public Event attendEvent(@PathParam("id") String id) {
		return attendEventWithJob(id, null);
	}

	/**
	 * Attend event with job.
	 *
	 * @param id the id
	 * @param jobId the job id
	 * @return the event
	 */
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
		if (StringUtils.isNotBlank(jobId)) {
			job = jobService.getJobByName(jobId);
			Assert.notNull(job);
		}

		return eventService.attendEvent(eventService.getEventById(parsedId), job);
	}

	/**
	 * Dismiss event.
	 *
	 * @param id the id
	 * @return the event
	 */
	@GET
	@Path("/{id}/dismiss")
	@PreAuthorize("hasRole('ROLE_USER')")
	@Produces(MediaType.APPLICATION_JSON)
	public Event dismissEvent(@PathParam("id") String id) {

		Long parsedId = NumberUtils.parseNumber(id, Long.class);
		Assert.notNull(parsedId, "event id could not be parsed");

		return eventService.dismissEvent(eventService.getEventById(parsedId));
	}
	
	/**
	 * Comment event.
	 *
	 * @param id the id
	 * @param comment the comment
	 * @return the comment
	 */
	@POST
	@Path("/{id}/comment")
	@PreAuthorize("hasRole('ROLE_USER')")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Comment commentEvent(@PathParam("id") String id, Comment comment) {

		Long parsedId = NumberUtils.parseNumber(id, Long.class);
		Assert.notNull(parsedId, "event id could not be parsed");
		Assert.notNull(comment);
		Assert.hasText(comment.getText(), "Comment should have text");

		return eventService.commentEvent(eventService.getEventById(parsedId), comment);
	}

	/**
	 * Creates the event.
	 *
	 * @param event the event
	 * @return the event
	 */
	@POST
	@PreAuthorize("hasRole('ROLE_USER')")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Event createEvent(Event event) {

		log.debug("calling createEvent");
		Assert.notNull(event);

		if (!eventService.checkIfUserCanCreateEvent(event, userService.getUser())) {
			throw new IllegalStateException("You already have an event created in this time");
		}

		eventService.saveEvent(event);
		// set actual logged in user as creator
		event.setCreator(userService.getUser());
		// set creation date to now
		event.setCreationDate((new DateTime()).toDateTimeISO().toDate());
		// TODO validate event
		eventService.saveEvent(event);

		return event;

	}
}
