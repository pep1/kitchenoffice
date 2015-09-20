package com.pep1.kitchenoffice.service;

import java.util.List;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.pep1.kitchenoffice.data.Tag;
import com.pep1.kitchenoffice.repository.TagRepository;

@Service
@Scope("singleton")
public class TagService {

	private static Logger log = LoggerFactory.getLogger(TagService.class);

	@Autowired
	private TagRepository tagRepository;

	public Tag findByName(String tag) {
		return tagRepository.findByName(tag);
	}

	public List<Tag> findAll(Pageable pageable) {
		return tagRepository.findAll(pageable).getContent();
	}

	public List<Tag> findByNameLike(String name, Pageable pageable) {
		if (StringUtils.hasLength(name) && name.length() > 2) {
			return tagRepository.findByNameLike("*" + name + "*", pageable);
		} else {
			return findAll(pageable);
		}
	}

	public List<Tag> getTags(Pageable pageable) {
		return tagRepository.findAll(pageable).getContent();
	}

	@Transactional
	public Tag save(Tag tag) {
		Assert.notNull(tag);
		return tagRepository.save(tag);
	}

	protected void checkAndCleanUp(Tag tag) {
		Assert.notNull(tag);
		if (!(tagRepository.countTaggedObjects(tag) > 0)) {
			log.info("Cleaning up tag: " + tag.getName());
			tagRepository.delete(tag);
		}
	}

	protected void checkAndCleanUp() {
		for (Tag tag : tagRepository.findWithoutRelation()) {

			DateTime date = new DateTime(tag.getTimeStamp());
			// check if the tag is older than one hour
			if (date.isBefore((new DateTime()).minusHours(1))) {
				log.info("Cleaning up tag: " + tag.getName());
				tagRepository.delete(tag);
			}
		}
	}
}
