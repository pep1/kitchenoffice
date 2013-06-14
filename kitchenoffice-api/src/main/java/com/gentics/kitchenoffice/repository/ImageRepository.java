package com.gentics.kitchenoffice.repository;

import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.neo4j.repository.RelationshipOperationsRepository;

import com.gentics.kitchenoffice.data.Image;

public interface ImageRepository extends GraphRepository<Image>, RelationshipOperationsRepository<Image>{

}
