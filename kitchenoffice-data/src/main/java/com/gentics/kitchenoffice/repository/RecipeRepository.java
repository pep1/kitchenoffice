package com.gentics.kitchenoffice.repository;

import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.neo4j.repository.RelationshipOperationsRepository;

import com.gentics.kitchenoffice.data.Recipe;

public interface RecipeRepository extends GraphRepository<Recipe>, RelationshipOperationsRepository<Recipe>{
	
	public List<Recipe> findByNameContaining(String name);
	
	@Query("start recipe={0} match recipe-->name<--similar return similar")
    public List<Recipe> getSimilarRecipes(Recipe recipe);
	
	@Query("start recipe={0} " + 
			" match recipe-->article<--similar" + 
			" return distinct similar" +
			" order by count(article) desc" + 
			" limit 10")
	public List<Recipe> getRecipesWithSimilarIngredients(Recipe recipe);

}
