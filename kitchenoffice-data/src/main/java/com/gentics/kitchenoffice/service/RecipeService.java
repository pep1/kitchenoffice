package com.gentics.kitchenoffice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.gentics.kitchenoffice.repository.RecipeRepository;


@Service
@Scope("singleton")
public class RecipeService {

	@Autowired
	private RecipeRepository recipeRepository;
	
}
