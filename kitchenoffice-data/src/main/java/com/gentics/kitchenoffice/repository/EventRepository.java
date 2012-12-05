package com.gentics.kitchenoffice.repository;

import org.springframework.data.neo4j.repository.GraphRepository;

import com.gentics.kitchenoffice.data.event.Event;

public interface EventRepository extends GraphRepository<Event> {

}
