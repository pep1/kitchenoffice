package com.gentics.kitchenoffice.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.gentics.kitchenoffice.data.event.Event;
import com.gentics.kitchenoffice.data.user.User;

public interface EventRepository extends GraphRepository<Event> {

	public Event findById(Long id);

	@Query("start events=node:__types__(className=\"com.gentics.kitchenoffice.data.event.Event\") "
			+ "where has(events.startDate) and events.startDate > {0} "
			+ "return distinct events "
			+ "order by events.startDate asc")
	public List<Event> findAllinFutureOf(long date, Pageable page);

	@Query("start events=node:__types__(className=\"com.gentics.kitchenoffice.data.event.Event\") "
			+ "where has(events.startDate) and events.startDate > {0} "
			+ "return distinct events "
			+ "order by events.startDate asc")
	public List<Event> findAllinFutureOf(long date);

	@Query("start user={0} "
			+ "match user-[:TAKES_PART]-events "
			+ "where events.startDate > {1} "
			+ "return distinct events "
			+ "order by events.startDate asc")
	public List<Event> findAllAttendedInFutureOf(User user, long date, Pageable Page);

	@Query("start user={0} "
			+ "match user-[:TAKES_PART]-events "
			+ "where events.startDate > {1} "
			+ "return distinct events "
			+ "order by events.startDate asc")
	public List<Event> findAllAttendedInFutureOf(User user, long date);

	@Query("start user={0} "
			+ "match user-[:TAKES_PART]-events "
			+ "and not (events.startDate >= {2} OR events.endDate <= {1}) " // -> not ( komplementärmenge von innerhalb )
			+ "return distinct events "
			+ "order by events.startDate asc")
	public List<Event> findAllAttendedInPeriodOf(User user, long startDate, long endDate);

	@Query("start events=node:__types__(className=\"com.gentics.kitchenoffice.data.event.Event\") "
			+ "where has(events.creator) and events.creator = {0} "
			+ "and events.startDate > {1} "
			+ "return distinct events "
			+ "order by events.startDate asc")
	public List<Event> findAllCreatedInFutureOf(User user, long date);

	@Query("start events=node:__types__(className=\"com.gentics.kitchenoffice.data.event.Event\") "
			+ "where has(events.creator) and events.creator = {0} "
			+ "and not (events.startDate >= {2} OR events.endDate <= {1}) " // -> not ( komplementärmenge von innerhalb )
			+ "return distinct events "
			+ "order by events.startDate asc")
	public List<Event> findAllCreatedInPeriodOf(User user, long startDate, long endDate);
}
