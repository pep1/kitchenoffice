package com.gentics.kitchenoffice.repository;

import java.util.List;

import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.neo4j.repository.RelationshipOperationsRepository;

import com.gentics.kitchenoffice.data.Recipe;

public interface RecipeRepository extends GraphRepository<Recipe>, RelationshipOperationsRepository<Recipe>{
	
	public List<Recipe> findByNameContaining(String name);

}
