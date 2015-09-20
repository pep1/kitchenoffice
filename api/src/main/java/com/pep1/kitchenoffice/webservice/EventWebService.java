/*
 * @author: Leonard Osang <leonard@osang.at>
 */
package com.pep1.kitchenoffice.webservice;

import com.pep1.kitchenoffice.data.Comment;
import com.pep1.kitchenoffice.data.Job;
import com.pep1.kitchenoffice.data.event.Event;
import com.pep1.kitchenoffice.service.EventService;
import com.pep1.kitchenoffice.service.JobService;
import com.pep1.kitchenoffice.service.UserService;
import com.pep1.kitchenoffice.webservice.filter.CacheAnnotations.NoCache;
import org.apache.commons.lang.StringUtils;
import org.neo4j.graphdb.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;
import org.springframework.util.NumberUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.util.List;

/**
 * The Class EventWebService.
 * <p>
 * Provides event CRUD functionality.
 */
@RestController
@Scope("singleton")
@RequestMapping("/api/v1/events")
@PreAuthorize("hasRole('ROLE_USER')")
@NoCache
public class EventWebService {

    /**
     * The log.
     */
    private static Logger log = LoggerFactory.getLogger(EventWebService.class);

    /**
     * The user service.
     */
    @Autowired
    private UserService userService;

    /**
     * The event service.
     */
    @Autowired
    private EventService eventService;

    /**
     * The job service.
     */
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
     * @return the events
     */
    @RequestMapping(method = RequestMethod.GET)
    public List<Event> getEvents(Pageable pageable) {
        log.debug("calling getEvents");
        return eventService.getFutureEvents(pageable);
    }

    /**
     * Gets the past events.
     *
     * @return the past events
     */
    @RequestMapping(method = RequestMethod.GET, value = "/past")
    public List<Event> getPastEvents(Pageable pageable) {
        log.debug("calling getPastEvents");
        return eventService.getPastEvents(pageable);
    }

    /**
     * Gets the my events.
     *
     * @return the my events
     */
    @RequestMapping(method = RequestMethod.GET, value = "/mine")
    public List<Event> getMyEvents(Pageable pageable) {
        log.debug("calling getMyEvents");
        return eventService.getEventsOfUser(pageable);
    }

    /**
     * Gets the my attended events.
     *
     * @return the my attended events
     */
    @RequestMapping(method = RequestMethod.GET, value = "/attended")
    public List<Event> getMyAttendedEvents(Pageable pageable) {
        log.debug("calling getMyAttendedEvents");
        return eventService.getFutureEvents(pageable);
    }

    /**
     * Gets the event by the given Id.
     *
     * @param id the id
     * @return the event
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public Event getEvent(@PathVariable("id") String id) {
        Long parsedId = NumberUtils.parseNumber(id, Long.class);
        Assert.notNull(parsedId);

        Event event = eventService.getEventById(parsedId);

        if (event == null) {
            throw new NotFoundException("Sorry, no event found with id " + parsedId);
        }

        return event;
    }


    /**
     * Creates or updates an event.
     *
     * @param event the event
     * @return the event
     */
    @RequestMapping(method = RequestMethod.POST)
    public Event createOrUpdateEvent(@Valid Event event) {

        log.debug("calling createEvent");
        Assert.notNull(event);

        eventService.saveEvent(event);

        return event;
    }

    /**
     * Removes the event.
     *
     * @param id the id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void removeEvent(@PathVariable("id") String id) {
        eventService.deleteEvent(getEvent(id));
    }

    /**
     * Attend event.
     *
     * @param id the id
     * @return the event
     */
    @RequestMapping(value = "/{id}/attend", method = RequestMethod.GET)
    public Event attendEvent(@PathVariable("id") String id) {
        return attendEventWithJob(id, null);
    }

    /**
     * Attend event with job.
     *
     * @param id    the id
     * @param jobId the job id
     * @return the event
     */
    @RequestMapping(value = "/{id}/attend/{jobid}", method = RequestMethod.GET)
    public Event attendEventWithJob(@PathVariable("id") String id, @PathVariable("jobid") String jobId) {
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
    @RequestMapping(value = "/{id}/dismiss", method = RequestMethod.GET)
    public Event dismissEvent(@PathVariable("id") String id) {

        Long parsedId = NumberUtils.parseNumber(id, Long.class);
        Assert.notNull(parsedId, "event id could not be parsed");

        return eventService.dismissEvent(eventService.getEventById(parsedId));
    }

    /**
     * Comment event.
     *
     * @param id      the id
     * @param comment the comment
     * @return the comment
     */
    @RequestMapping(value = "/{id}/comment", method = RequestMethod.POST)
    public Comment commentEvent(@PathVariable("id") String id, @RequestBody Comment comment) {

        Long parsedId = NumberUtils.parseNumber(id, Long.class);
        Assert.notNull(parsedId, "event id could not be parsed");
        Assert.notNull(comment);
        Assert.hasText(comment.getText(), "Comment should have text");

        return eventService.commentEvent(eventService.getEventById(parsedId), comment);
    }

    @RequestMapping(value = "/{id}/lock", method = RequestMethod.GET)
    public Event lockEvent(@PathVariable("id") String id) {

        Long parsedId = NumberUtils.parseNumber(id, Long.class);
        Assert.notNull(parsedId, "event id could not be parsed");

        return eventService.lockEvent(eventService.getEventById(parsedId));
    }

    @RequestMapping(value = "/{id}/unlock", method = RequestMethod.GET)
    public Event unlockEvent(@PathVariable("id") String id) {

        Long parsedId = NumberUtils.parseNumber(id, Long.class);
        Assert.notNull(parsedId, "event id could not be parsed");

        return eventService.unlockEvent(eventService.getEventById(parsedId));
    }

}
