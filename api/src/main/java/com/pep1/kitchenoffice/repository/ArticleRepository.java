package com.pep1.kitchenoffice.repository;

import org.springframework.data.neo4j.repository.GraphRepository;

import com.pep1.kitchenoffice.data.Article;

public interface ArticleRepository extends GraphRepository<Article> {}
