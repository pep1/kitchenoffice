package com.gentics.kitchenoffice.repository;

import org.springframework.data.neo4j.repository.GraphRepository;

import com.gentics.kitchenoffice.data.Article;

public interface ArticleRepository extends GraphRepository<Article> {}
