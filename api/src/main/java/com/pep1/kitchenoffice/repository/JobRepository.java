package com.pep1.kitchenoffice.repository;

import org.springframework.data.neo4j.repository.GraphRepository;

import com.pep1.kitchenoffice.data.Job;

public interface JobRepository extends GraphRepository<Job>{

	public Job findByName(String name);
}
