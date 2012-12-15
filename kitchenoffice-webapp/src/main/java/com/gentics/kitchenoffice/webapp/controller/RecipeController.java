package com.gentics.kitchenoffice.webapp.controller;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.gentics.kitchenoffice.data.Recipe;
import com.gentics.kitchenoffice.repository.RecipeRepository;
import com.gentics.kitchenoffice.service.KitchenOfficeUserService;
import com.gentics.kitchenoffice.webapp.container.RecipeContainer;
import com.gentics.kitchenoffice.webapp.container.SpringDataBeanItemContainer;


@Controller
@Scope("session")
public class RecipeController implements ControllerInterface {
	
	private static final Logger log = Logger.getLogger(RecipeController.class);

	@Autowired
	private RecipeRepository recipeRepository;
	
	@Autowired
	private RecipeContainer recipeContainer;
	
	@Autowired
	private KitchenOfficeUserService userService;
	
	@PostConstruct
	public void postConstruct() {
		
		log.debug("initlializing RecipeController ...");
	}

	@Override
	public SpringDataBeanItemContainer<Recipe> getContainer() {
		return recipeContainer;
	}

	public Recipe addNewRecipe() {

		Recipe newRecipe = new Recipe();
		newRecipe.setCreator(userService.getUser());
		recipeContainer.addItem(newRecipe);
		
		return newRecipe;
	}
}
