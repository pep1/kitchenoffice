/*
 * @author: Leonard Osang <leonard@osang.at>
 */
package com.gentics.kitchenoffice.webservice;

import com.gentics.kitchenoffice.data.Comment;
import com.gentics.kitchenoffice.data.Job;
import com.gentics.kitchenoffice.data.event.Event;
import com.gentics.kitchenoffice.service.EventService;
import com.gentics.kitchenoffice.service.JobService;
import com.gentics.kitchenoffice.service.CrowdUserService;
import com.gentics.kitchenoffice.webservice.filter.CacheAnnotations.NoCache;
import com.sun.jersey.api.NotFoundException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.NumberUtils;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * The Class EventWebService.
 * 
 * Provides event CRUD functionality.
 */
@Component
@Scope("singleton")
@Path("/events")
@PreAuthorize("isAuthenticated()")
@Produces(MediaType.APPLICATION_JSON)
@NoCache
public class EventWebService {

	/** The log. */
	private static Logger log = LoggerFactory.getLogger(EventWebService.class);

	/** The user service. */
	@Autowired
	private CrowdUserService userService;

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
	 * @param page
	 *            the page
	 * @param size
	 *            the size
	 * @return the events
	 */
	@GET
	public List<Event> getEvents(@Context Pageable pageable) {
		log.debug("calling getEvents");
		return eventService.getFutureEvents(pageable);
	}

	/**
	 * Gets the past events.
	 * 
	 * @param page
	 *            the page
	 * @param size
	 *            the size
	 * @return the past events
	 */
	@GET
	@Path("/past")
	public List<Event> getPastEvents(@Context Pageable pageable) {
		log.debug("calling getPastEvents");
		return eventService.getPastEvents(pageable);
	}

	/**
	 * Gets the my events.
	 * 
	 * @param page
	 *            the page
	 * @param size
	 *            the size
	 * @return the my events
	 */
	@GET
	@Path("/mine")
	public List<Event> getMyEvents(@Context Pageable pageable) {
		log.debug("calling getMyEvents");
		return eventService.getEventsOfUser(pageable);
	}

	/**
	 * Gets the my attended events.
	 * 
	 * @param page
	 *            the page
	 * @param size
	 *            the size
	 * @return the my attended events
	 */
	@GET
	@Path("/attended")
	public List<Event> getMyAttendedEvents(@Context Pageable pageable) {
		log.debug("calling getMyAttendedEvents");
		return eventService.getFutureEvents(pageable);
	}

	/**
	 * Gets the event by the given Id.
	 * 
	 * @param id
	 *            the id
	 * @return the event
	 */
	@GET
	@Path("/{id}")
	public Event getEvent(@PathParam("id") String id) {
		Long parsedId = NumberUtils.parseNumber(id, Long.class);
		Assert.notNull(parsedId);

		Event event = eventService.getEventById(parsedId);

		if (event == null) {
			throw new NotFoundException("Sorry, no event found with id " + parsedId);
		}

		return event;
	}

	/**
	 * Removes the event.
	 * 
	 * @param id
	 *            the id
	 */
	@DELETE
	@Path("/{id}")
	public void removeEvent(@PathParam("id") String id) {
		eventService.deleteEvent(getEvent(id));
	}

	/**
	 * Attend event.
	 * 
	 * @param id
	 *            the id
	 * @return the event
	 */
	@GET
	@Path("/{id}/attend")
	public Event attendEvent(@PathParam("id") String id) {
		return attendEventWithJob(id, null);
	}

	/**
	 * Attend event with job.
	 * 
	 * @param id
	 *            the id
	 * @param jobId
	 *            the job id
	 * @return the event
	 */
	@GET
	@Path("/{id}/attend/{jobid}")
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
	 * @param id
	 *            the id
	 * @return the event
	 */
	@GET
	@Path("/{id}/dismiss")
	public Event dismissEvent(@PathParam("id") String id) {

		Long parsedId = NumberUtils.parseNumber(id, Long.class);
		Assert.notNull(parsedId, "event id could not be parsed");

		return eventService.dismissEvent(eventService.getEventById(parsedId));
	}

	/**
	 * Comment event.
	 * 
	 * @param id
	 *            the id
	 * @param comment
	 *            the comment
	 * @return the comment
	 */
	@POST
	@Path("/{id}/comment")
	@Consumes(MediaType.APPLICATION_JSON)
	public Comment commentEvent(@PathParam("id") String id, Comment comment) {

		Long parsedId = NumberUtils.parseNumber(id, Long.class);
		Assert.notNull(parsedId, "event id could not be parsed");
		Assert.notNull(comment);
		Assert.hasText(comment.getText(), "Comment should have text");

		return eventService.commentEvent(eventService.getEventById(parsedId), comment);
	}

	@GET
	@Path("/{id}/lock")
	@Consumes(MediaType.APPLICATION_JSON)
	public Event lockEvent(@PathParam("id") String id) {

		Long parsedId = NumberUtils.parseNumber(id, Long.class);
		Assert.notNull(parsedId, "event id could not be parsed");

		return eventService.lockEvent(eventService.getEventById(parsedId));
	}

	@GET
	@Path("/{id}/unlock")
	@Consumes(MediaType.APPLICATION_JSON)
	public Event unlockEvent(@PathParam("id") String id) {

		Long parsedId = NumberUtils.parseNumber(id, Long.class);
		Assert.notNull(parsedId, "event id could not be parsed");

		return eventService.unlockEvent(eventService.getEventById(parsedId));
	}

	/**
	 * Creates or updates an event.
	 * 
	 * @param event
	 *            the event
	 * @return the event
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Event createOrUpdateEvent(@Valid Event event) {

		log.debug("calling createEvent");
		Assert.notNull(event);

		eventService.saveEvent(event);

		return event;
	}
}
