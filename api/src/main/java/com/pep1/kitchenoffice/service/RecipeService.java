package com.pep1.kitchenoffice.service;

import com.pep1.kitchenoffice.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;


@Service
@Scope("singleton")
public class RecipeService {

	@Autowired
	private RecipeRepository recipeRepository;
	
}
