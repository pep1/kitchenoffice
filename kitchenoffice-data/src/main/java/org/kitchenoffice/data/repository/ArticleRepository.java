package org.kitchenoffice.data.repository;

import org.kitchenoffice.data.domain.Article;
import org.springframework.data.neo4j.repository.GraphRepository;

public interface ArticleRepository extends GraphRepository<Article> {}
