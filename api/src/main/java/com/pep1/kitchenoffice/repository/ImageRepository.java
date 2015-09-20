package com.pep1.kitchenoffice.repository;

import com.pep1.kitchenoffice.data.Image;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.neo4j.repository.RelationshipOperationsRepository;

public interface ImageRepository extends GraphRepository<Image>, RelationshipOperationsRepository<Image>{

}
