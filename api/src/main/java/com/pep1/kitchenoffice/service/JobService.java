package com.pep1.kitchenoffice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.pep1.kitchenoffice.data.Job;
import com.pep1.kitchenoffice.repository.JobRepository;

@Service
@Scope("singleton")
public class JobService {

	private static Logger log = LoggerFactory.getLogger(JobService.class);

	@Autowired
	private JobRepository jobRepository;

	public Job getJobByName(String name) {
		log.debug("calling get job by name");
		Assert.notNull(name);
		return jobRepository.findByName(name);
	}

}
