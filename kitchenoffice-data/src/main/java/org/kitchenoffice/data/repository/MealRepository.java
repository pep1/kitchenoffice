package org.kitchenoffice.data.repository;

import org.kitchenoffice.data.domain.Meal;
import org.springframework.data.neo4j.repository.GraphRepository;

public interface MealRepository extends GraphRepository<Meal> {

}
