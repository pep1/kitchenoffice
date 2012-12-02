package com.gentics.kitchenoffice.service;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.gentics.kitchenoffice.data.Article;
import com.gentics.kitchenoffice.data.Recipe;
import com.gentics.kitchenoffice.repository.ArticleRepository;


@Service
@Scope("singleton")
public class ArticleService {
	
	private static Logger log = Logger
			.getLogger(ArticleService.class);
	
	@Autowired
	ArticleRepository articleRepository;
	
	@PostConstruct
	public void initialize() {
		log.debug("initializing " + this.getClass().getSimpleName() + " instance ... ");
	}
	
	public Article save(Article article) {
		
		Assert.notNull(article, "Article to save may not be null");
		articleRepository.save(article);
		return article;
	}
	
	public Article refresh(Article article) {
		return articleRepository.findOne(article.getId());
	}
	
	public void addToRecipe(Article article , Recipe recipe, Integer amount) {
		
	}


}
