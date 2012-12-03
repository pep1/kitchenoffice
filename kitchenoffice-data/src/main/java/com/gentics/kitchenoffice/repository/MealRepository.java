package com.gentics.kitchenoffice.repository;

import org.springframework.data.neo4j.repository.GraphRepository;

import com.gentics.kitchenoffice.data.Event;

public interface MealRepository extends GraphRepository<Event> {

}
