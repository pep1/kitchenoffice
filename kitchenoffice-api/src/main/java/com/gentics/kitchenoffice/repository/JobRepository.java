package com.gentics.kitchenoffice.repository;

import org.springframework.data.neo4j.repository.GraphRepository;

import com.gentics.kitchenoffice.data.Job;

public interface JobRepository extends GraphRepository<Job>{

}
