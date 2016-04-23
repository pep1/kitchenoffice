package com.gentics.kitchenoffice.service;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.gentics.kitchenoffice.data.Comment;
import com.gentics.kitchenoffice.data.Job;
import com.gentics.kitchenoffice.data.Participant;
import com.gentics.kitchenoffice.data.event.Event;
import com.gentics.kitchenoffice.data.user.User;
import com.gentics.kitchenoffice.repository.CommentRepository;
import com.gentics.kitchenoffice.repository.EventRepository;
import com.gentics.kitchenoffice.repository.JobRepository;
import com.gentics.kitchenoffice.repository.ParticipantRepository;
import com.gentics.kitchenoffice.service.event.EventCreatedEvent;
import com.google.common.eventbus.EventBus;

@Service
@Scope("singleton")
public class EventService {

	private static Logger log = LoggerFactory.getLogger(EventService.class);

	@Value("${rss.itemcount}")
	private Integer itemCount;

	@Autowired
	private EventRepository eventRepository;

	@Autowired
	private CrowdUserService userService;

	@Autowired
	private JobRepository jobRepository;

	@Autowired
	private ParticipantRepository participantRepository;

	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private EventBus eventBus;

	@PostConstruct
	public void initialize() {
		log.debug("initializing " + this.getClass().getSimpleName() + " instance ...");
	}

	public Page<Event> getEvents(Pageable pageable) {
		return eventRepository.findAll(pageable);
	}

	public List<Event> getFutureEvents(Pageable pageable) {
		return eventRepository.findAllinFutureOf(new DateTime().toDateTimeISO().getMillis(), pageable);
	}

	public List<Event> getPastEvents(Pageable pageable) {
		return eventRepository.findAllinPastOf(new DateTime().toDateTimeISO().getMillis(), pageable);
	}

	public List<Event> getMyAttendedEvents(Pageable pageable) {
		return eventRepository.findAllAttended(userService.getUser(), pageable);
	}

	public List<Event> getEventsOfUser(Pageable pageable) {
		return eventRepository.findByCreator(userService.getUser(), pageable);
	}

	public Event getEventById(Long id) {
		Assert.notNull(id);
		return eventRepository.findById(id);
	}

	@Transactional
	public Event saveEvent(Event event) {
		Assert.notNull(event);

		if (!event.isNew()) {
			// check if the user really is the creator
			if (getEventById(event.getId()).getCreator() != userService.getUser()) {
				throw new IllegalStateException("You are not the creator of this event");
			}
		} else {
			// set actual logged in user as creator
			event.setCreator(userService.getUser());
			// set creation date to now
			event.setCreationDate((new DateTime()).toDateTimeISO().toDate());

			if (!checkIfUserCanCreateEvent(event, userService.getUser())) {
				throw new IllegalStateException("You already have an event created in this time");
			}
		}

		if (new DateTime(event.getStartDate()).isBeforeNow()) {
			throw new IllegalStateException("User can not create or edit an event in the past");
		}

		// save the event
		eventRepository.save(event);
		// publish create event
		eventBus.post(new EventCreatedEvent(event));

		return event;
	}

	@PreAuthorize("(#event.creator.equals(principal)) or hasRole('ROLE_ADMIN')")
	@Transactional
	public void deleteEvent(@Param("event") Event event) {
		Assert.notNull(event);

		if (new DateTime(event.getStartDate()).isBeforeNow()) {
			throw new IllegalStateException("User can not delete past events");
		}

		eventRepository.delete(event);
	}

	@PreAuthorize("(#event.creator.equals(principal)) or hasRole('ROLE_ADMIN')")
	@Transactional
	public Event lockEvent(@Param("event") Event event) {

		Assert.notNull(event);

		if (event.isLocked()) {
			throw new IllegalStateException("Event is already locked");
		} else {
			event.setLocked(true);
		}

		return eventRepository.save(event);
	}

	@PreAuthorize("(#event.creator.equals(principal)) or hasRole('ROLE_ADMIN')")
	@Transactional
	public Event unlockEvent(@Param("event") Event event) {
		Assert.notNull(event);

		if (!event.isLocked()) {
			throw new IllegalStateException("Event not locked");
		} else {
			event.setLocked(false);
		}

		return eventRepository.save(event);
	}

	@Transactional
	public Event attendEvent(Event event, Job job) {
		Assert.notNull(event);
		Assert.isTrue(!event.isNew());
		// TODO: check if job can be null

		// check if user already attend this event
		if (CollectionUtils.countMatches(event.getParticipants(), new Predicate() {
			@Override
			public boolean evaluate(Object object) {
				return userService.getUser().equals(((Participant) object).getUser());
			}
		}) > 0) {
			throw new IllegalStateException("You already attend this event");
		}

		if (!checkIfUserCanAttendEvent(event, userService.getUser())) {
			throw new IllegalStateException("You already attend an event at this time!");
		}

		event.addParticipant(userService.getUser(), job);
		return eventRepository.save(event);
	}

	@Transactional
	public Event dismissEvent(Event event) {
		Assert.notNull(event);
		Assert.isTrue(!event.isNew());
		return unAttendUserFromEvent(event, userService.getUser());
	}

	private Event unAttendUserFromEvent(Event event, final User user) {
		Assert.notNull(event);
		Assert.isTrue(!event.isNew());
		Assert.notNull(user);

		if (new DateTime(event.getStartDate()).isBeforeNow()) {
			throw new IllegalStateException("User can not dismiss past events");
		}

		if (event.isLocked()) {
			throw new IllegalStateException("User can not dismiss this event because it's locked");
		}

		Set<Participant> participants = event.getParticipants();

		Participant participant = (Participant) CollectionUtils.find(participants, new Predicate() {
			@Override
			public boolean evaluate(Object object) {
				return user.equals(((Participant) object).getUser());
			}
		});
		// return if we got no participant
		if (participant == null) {
			throw new IllegalArgumentException("User is not in the list of participants!");
		}

		// remove the participant from the list
		event.getParticipants().remove(participant);
		// and save the event. Spring will take care about all unnecessary
		// references
		eventRepository.save(event);

		return event;
	}

	public boolean checkIfUserCanAttendEvent(Event event, User user) {
		Assert.notNull(event);
		Assert.notNull(user);
		Assert.notNull(event.getStartDate());
		Assert.notNull(event.getEndDate());

		if (event.isLocked()) {
			throw new IllegalStateException("User can not attend this event because it's locked");
		}

		if (new DateTime(event.getStartDate()).isBeforeNow()) {
			throw new IllegalStateException("User can not attend past events");
		}

		boolean output = CollectionUtils.isEmpty(getAttendedEventsFromUserInPeriod(event.getStartDate(),
				event.getEndDate()));
		return output;
	}

	public boolean checkIfUserCanCreateEvent(Event event, User user) {
		Assert.notNull(event);
		Assert.notNull(user);
		Assert.notNull(event.getStartDate());
		Assert.notNull(event.getEndDate());

		if (new DateTime(event.getStartDate()).isBeforeNow()) {
			throw new IllegalArgumentException("Events startdate can not be in the past");
		}

		boolean output = CollectionUtils.isEmpty(getCreatedEventsFromUserInPeriod(event.getStartDate(),
				event.getEndDate()));
		return output;
	}

	public List<Event> getAttendedEventsFromUserInFutureOf(Date date) {
		Assert.notNull(date);
		return eventRepository.findAllAttendedInFuture(userService.getUser(), new DateTime(date).getMillis());
	}

	public List<Event> getAttendedEventsFromUserInPeriod(Date startDate, Date endDate) {
		Assert.notNull(startDate);
		Assert.notNull(endDate);
		return eventRepository.findAllAttendedInPeriodOf(userService.getUser(), new DateTime(startDate).getMillis(),
				new DateTime(endDate).getMillis());
	}

	public List<Event> getCreatedEventsFromUserInFutureOf(Date date) {
		Assert.notNull(date);
		return eventRepository.findAllCreatedInFutureOf(userService.getUser(), new DateTime(date).getMillis());
	}

	public List<Event> getCreatedEventsFromUserInPeriod(Date startDate, Date endDate) {
		Assert.notNull(startDate);
		Assert.notNull(endDate);

		List<Event> output = eventRepository.findAllCreatedInPeriodOf(userService.getUser(),
				new DateTime(startDate).getMillis(), new DateTime(endDate).getMillis());
		return output;
	}

	@Transactional
	public Comment commentEvent(Event event, Comment comment) {
		Assert.notNull(event);
		Assert.notNull(comment);

		comment.setTimeStamp(new DateTime().toDateTimeISO().toDate());
		comment.setUser(userService.getUser());
		commentRepository.save(comment);

		event.addComment(comment);
		eventRepository.save(event);

		return comment;
	}

	public int getRssItemCount() {
		return itemCount;
	}
}
