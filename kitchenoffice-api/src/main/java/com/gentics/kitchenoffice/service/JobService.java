package com.gentics.kitchenoffice.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.gentics.kitchenoffice.data.Job;
import com.gentics.kitchenoffice.repository.JobRepository;

@Service
@Scope("singleton")
public class JobService {
	
	private static Logger log = Logger.getLogger(JobService.class);
	
	@Autowired
	private JobRepository jobRepository;
	
	public Job getJobByName(String name) {
		Assert.notNull(name);
		return jobRepository.findByName(name);
	}

}
