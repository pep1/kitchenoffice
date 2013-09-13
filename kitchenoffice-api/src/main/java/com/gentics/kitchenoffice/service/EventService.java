package com.gentics.kitchenoffice.service;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.gentics.kitchenoffice.data.Job;
import com.gentics.kitchenoffice.data.Participant;
import com.gentics.kitchenoffice.data.event.Event;
import com.gentics.kitchenoffice.data.user.User;
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
	private KitchenOfficeUserService userService;

	@Autowired
	private JobRepository jobRepository;

	@Autowired
	private ParticipantRepository participantRepository;

	@PostConstruct
	public void initialize() {
		log.debug("initializing " + this.getClass().getSimpleName() + " instance ...");
	}

	public List<Event> getFutureEvents(PageRequest pagerequest) {
		return eventRepository.findAllinFutureOf(new Date().getTime(), pagerequest);
	}

	public Event getEventById(Long id) {
		return eventRepository.findById(id);
	}

	public Event saveEvent(Event event) {
		Assert.notNull(event);
		return eventRepository.save(event);
	}

	public Event attendEvent(Event event, Job job) {

		Assert.notNull(event);
		Assert.isTrue(!event.isNew());
		// TODO: check if job can be null

		// refresh
		event = getEventById(event.getId());

		// check if user already attend this event
		if (CollectionUtils.countMatches(event.getParticipants(), new Predicate() {
			@Override
			public boolean evaluate(Object object) {
				return userService.getUser().equals(((Participant) object).getUser());
			}
		}) > 0) {
			throw new IllegalStateException("You already attend this event");
		}

		if (checkIfUserCanAttendEvent(event, userService.getUser())) {
			throw new IllegalStateException("You already attend an event at this time!");
		}

		event.addParticipant(userService.getUser(), job);
		return eventRepository.save(event);
	}

	public Event unAttendEvent(Event event) {
		Assert.notNull(event);
		Assert.isTrue(!event.isNew());
		// refresh
		event = getEventById(event.getId());

		Set<Participant> participants = event.getParticipants();
		CollectionUtils.filter(participants, new Predicate() {
			@Override
			public boolean evaluate(Object object) {
				return userService.getUser().equals(((Participant) object).getUser());
			}
		});
		// return if we got no participant
		if (!participants.isEmpty() && participants.size() != 1) {
			return event;
		}
		Participant participant = participants.iterator().next();

		// remove the participant from the list
		event.getParticipants().remove(participant);
		event = eventRepository.save(event);

		// delete the participant itself
		participantRepository.delete(participant);

		return event;
	}

	public boolean checkIfUserCanAttendEvent(Event event, User user) {
		Assert.notNull(event);
		Assert.notNull(user);

		return (CollectionUtils.isEmpty(getAttendedEventsFromUserInPeriod(event.getStartDate(), event.getEndDate()))) ? true
				: false;
	}

	public boolean checkIfUserCanCreateEvent(Event event, User user) {
		Assert.notNull(event);
		Assert.notNull(user);

		boolean output = CollectionUtils.isEmpty(getCreatedEventsFromUserInPeriod(event.getStartDate(),
				event.getEndDate()));
		return (output) ? true : false;
	}

	public List<Event> getAttendedEventsFromUserInFutureOf(Date date) {
		Assert.notNull(date);
		return eventRepository.findAllAttendedInFutureOf(userService.getUser(), date.getTime());
	}

	public List<Event> getAttendedEventsFromUserInPeriod(Date startDate, Date endDate) {
		Assert.notNull(startDate);
		Assert.notNull(endDate);
		return eventRepository.findAllAttendedInPeriodOf(userService.getUser(), startDate.getTime(), endDate.getTime());
	}

	public List<Event> getCreatedEventsFromUserInFutureOf(Date date) {
		Assert.notNull(date);
		return eventRepository.findAllCreatedInFutureOf(userService.getUser(), date.getTime());
	}

	public List<Event> getCreatedEventsFromUserInPeriod(Date startDate, Date endDate) {
		Assert.notNull(startDate);
		Assert.notNull(endDate);
		
		List<Event> output = eventRepository.findAllCreatedInPeriodOf(userService.getUser(), startDate.getTime(), endDate.getTime());
		return output;
	}
}
