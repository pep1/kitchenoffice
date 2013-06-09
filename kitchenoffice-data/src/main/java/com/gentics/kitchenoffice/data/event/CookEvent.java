package com.gentics.kitchenoffice.data.event;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

import com.gentics.kitchenoffice.data.Recipe;

@NodeEntity
public class CookEvent extends Event{
	
	@Fetch
    @RelatedTo(type = "COOKING", direction = Direction.BOTH)
	private Recipe recipe;
	
	public CookEvent() {
		super();
	}
	
	public Recipe getRecipe() {
		return recipe;
	}

	public void setRecipe(Recipe recipe) {
		this.recipe = recipe;
	}

}
