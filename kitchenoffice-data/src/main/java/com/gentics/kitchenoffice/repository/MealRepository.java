package com.gentics.kitchenoffice.repository;

import org.springframework.data.neo4j.repository.GraphRepository;

import com.gentics.kitchenoffice.data.Meal;

public interface MealRepository extends GraphRepository<Meal> {

}
