package com.gentics.kitchenoffice.webapp.container;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.gentics.kitchenoffice.data.Article;
import com.gentics.kitchenoffice.repository.ArticleRepository;


@Component
@Scope("prototype")
public class ArticleSelectContainer extends SpringDataBeanItemContainer<Article> {

	public ArticleSelectContainer() throws IllegalArgumentException {
		super(Article.class, ArticleRepository.class);
	}

}
