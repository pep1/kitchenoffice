package org.kitchenoffice.data.repository;

import java.util.List;

import org.kitchenoffice.data.domain.Recipe;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.neo4j.repository.RelationshipOperationsRepository;

public interface RecipeRepository extends GraphRepository<Recipe>, RelationshipOperationsRepository<Recipe>{
	
	public List<Recipe> findByNameContaining(String name);

}
