package com.gentics.kitchenoffice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.gentics.kitchenoffice.data.Tag;
import com.gentics.kitchenoffice.repository.TagRepository;

@Service
@Scope("singleton")
public class TagService {

	@Autowired
	private TagRepository tagRepository;
	
	public Tag findByTag(String tag) {
		return tagRepository.findByTag(tag);
	}

	public Tag save(Tag tag) {
		Assert.notNull(tag);
		return tagRepository.save(tag);
	}
}
