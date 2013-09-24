package com.gentics.kitchenoffice.service;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
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

@Service
@Scope("singleton")
public class EventService {

	private static Logger log = Logger.getLogger(EventService.class);

	@Autowired
	private EventRepository eventRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private JobRepository jobRepository;

	@Autowired
	private ParticipantRepository participantRepository;
	
	@Autowired
	private CommentRepository commentRepository;

	@PostConstruct
	public void initialize() {
		log.debug("initializing " + this.getClass().getSimpleName() + " instance ...");
	}

	public List<Event> getFutureEvents(PageRequest pagerequest) {
		return eventRepository.findAllinFutureOf(new DateTime().toDateTimeISO().getMillis(), pagerequest);
	}

	public List<Event> getMyAttendedEvents(PageRequest pagerequest) {
		return eventRepository.findAllAttended(userService.getUser(), pagerequest);
	}

	public List<Event> getEventsOfUser(PageRequest pagerequest) {
		return eventRepository.findByCreator(userService.getUser(), pagerequest);
	}

	public Event getEventById(Long id) {
		Assert.notNull(id);
		return eventRepository.findById(id);
	}

	public Event saveEvent(Event event) {
		Assert.notNull(event);
		return eventRepository.save(event);
	}

	@PreAuthorize("(#event.creator == authentication) or hasRole('ROLE_ADMIN')")
	public void deleteEvent(Event event) {
		Assert.notNull(event);
		eventRepository.delete(event);
	}

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

	public Event dismissEvent(Event event) {
		Assert.notNull(event);
		Assert.isTrue(!event.isNew());
		return unAttendUserFromEvent(event, userService.getUser());
	}

	public Event unAttendUserFromEvent(Event event, final User user) {
		Assert.notNull(event);
		Assert.isTrue(!event.isNew());
		Assert.notNull(user);

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

		boolean output = CollectionUtils.isEmpty(getAttendedEventsFromUserInPeriod(event.getStartDate(),
				event.getEndDate()));
		return output;
	}

	public boolean checkIfUserCanCreateEvent(Event event, User user) {
		Assert.notNull(event);
		Assert.notNull(user);

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
}
