package com.gentics.kitchenoffice.repository;

import org.springframework.data.neo4j.repository.GraphRepository;

import com.gentics.kitchenoffice.data.Article;
import com.gentics.kitchenoffice.data.Tag;

public interface TagRepository extends GraphRepository<Tag> {}
