package com.gentics.kitchenoffice.repository;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.neo4j.repository.RelationshipOperationsRepository;

import com.gentics.kitchenoffice.data.event.Event;
import com.gentics.kitchenoffice.data.user.User;

public interface EventRepository extends GraphRepository<Event>, RelationshipOperationsRepository<Event> {
	
	public Event findById(Long id);
	
	public List<Event> findByCreator(User user, PageRequest pagerequest);

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

	@Query("start user=node({0}) "
			+ "match user-[:TAKES_PART]-events "
			+ "where events.startDate > {1} "
			+ "return distinct events "
			+ "order by events.startDate asc")
	public List<Event> findAllAttendedInFutureOf(User user, long date, Pageable Page);

	@Query("start user=node({0}) "
			+ "match user-[:TAKES_PART]-events "
			+ "return distinct events "
			+ "order by events.startDate asc")
	public List<Event> findAllAttended(User user);
	
	@Query("start user=node({0}) "
			+ "match user-[:TAKES_PART]-events "
			+ "return distinct events "
			+ "order by events.startDate asc")
	public List<Event> findAllAttended(User user, Pageable page);
	
	@Query("start user=node({0}) "
			+ "match user-[:TAKES_PART]-events "
			+ "where events.startDate > {1} "
			+ "return distinct events "
			+ "order by events.startDate asc")
	public List<Event> findAllAttendedInFuture(User user, long date);

	@Query("start user=node({0}) "
			+ "match user-[:TAKES_PART]-events "
			+ "where not (events.startDate > {2} OR events.endDate < {1}) " // -> not ( komplementärmenge von innerhalb )
			+ "return distinct events "
			+ "order by events.startDate asc")
	public List<Event> findAllAttendedInPeriodOf(User user, long startDate, long endDate);

	@Query("start user=node({0}) "
			+ "match user-[:creator]-events "
			+ "where events.startDate > {1} "
			+ "return distinct events "
			+ "order by events.startDate asc")
	public List<Event> findAllCreatedInFutureOf(User user, long date);

	@Query("start user=node({0}) "
			+ "match user-[:creator]-events "
			+ "where not (events.startDate > {2} OR events.endDate < {1}) " // -> not ( komplementärmenge von innerhalb )
			+ "return distinct events "
			+ "order by events.startDate asc")
	public List<Event> findAllCreatedInPeriodOf(User user, long startDate, long endDate);

}
