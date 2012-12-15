package com.gentics.kitchenoffice.webapp.view.form;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.gentics.kitchenoffice.webapp.controller.RecipeController;
import com.vaadin.data.fieldgroup.FieldGroup;

@Component
@Scope("prototype")
public class CookEventForm extends FieldGroup{

	@Autowired
	private RecipeController recipeController;
	
	
}
