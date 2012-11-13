package com.gentics.kitchenoffice.webapp.container;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.gentics.kitchenoffice.data.Recipe;
import com.gentics.kitchenoffice.repository.RecipeRepository;


@Component
@Scope("prototype")
public class RecipeContainer extends SpringDataBeanItemContainer<Recipe>{

	public RecipeContainer()
			throws IllegalArgumentException {
		super(Recipe.class, RecipeRepository.class);
	}
	
}
