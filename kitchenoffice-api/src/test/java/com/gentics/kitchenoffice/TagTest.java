package com.gentics.kitchenoffice;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.data.neo4j.support.node.Neo4jHelper;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.transaction.annotation.Transactional;

import com.gentics.kitchenoffice.data.Tag;
import com.gentics.kitchenoffice.data.event.Location;
import com.gentics.kitchenoffice.repository.TagRepository;
import com.gentics.kitchenoffice.service.LocationService;
import com.gentics.kitchenoffice.service.TagService;

@ContextConfiguration(locations = "classpath:/spring/applicationContext.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class TagTest {

	private static Logger log = Logger.getLogger(TagTest.class);

	@Autowired
	private LocationService locationService;

	@Autowired
	private TagService tagService;

	@Autowired
	private TagRepository tagRepository;

	@Autowired
	private Neo4jTemplate template;

	@Rollback(false)
	@BeforeTransaction
	public void cleanUpGraph() {
		Neo4jHelper.cleanDb(template);
	}

	@Test
	public void firstTest() {
		log.debug("starting first Test");
	}

	@SuppressWarnings("unchecked")
	@Test
	public void tagTest() {

		log.debug("starting tag Test");
		log.debug("findall: " + ((List<?>) tagRepository.findAll().as(List.class)));
		log.debug("findwithoutrelation: " + tagRepository.findWithoutRelation());

		// assertTrue(tagRepository.findWithoutRelation().size() == 2);

		Location loc1 = locationService.findByName("test location 1");
		assertNotNull(loc1);

		log.debug("location before adding: " + locationService.findByName("test location 1"));

		Tag tag1 = tagService.findByName("tag1");
		assertNotNull(tag1);
		log.debug("find tagged objects: " + tagRepository.findTaggedObjects(tag1));

		Tag tag2 = tagService.findByName("tag2");
		assertNotNull(tag2);

		loc1.getTags().add(tag1);
		loc1.getTags().add(tag2);
		loc1 = locationService.saveLocation(loc1);

		log.debug("location after adding" + loc1);
		log.debug("find tagged objects: " + tagRepository.findTaggedObjects(tag1));
		log.debug("findwithoutrelation after adding: " + tagRepository.findWithoutRelation());

		assertTrue(tagRepository.findWithoutRelation().size() == 0);
	}

	@Before
	public void setUp() {
		log.debug("creating some data");

		Location loc = new Location();
		loc.setName("test location 1");
		loc.setAddress("test address 2");
		loc.setWebsite("http://www1.example.com");

		locationService.saveLocation(loc);

		Location loc2 = new Location();
		loc2.setName("test location 2");
		loc2.setAddress("test address 2");
		loc2.setWebsite("http://www2.example.com");

		locationService.saveLocation(loc2);

		Tag tag1 = new Tag();
		tag1.setName("tag1");
		tag1.setTimeStamp((new DateTime()).toDate());

		tagService.save(tag1);

		Tag tag2 = new Tag();
		tag2.setName("tag2");
		tag2.setTimeStamp((new DateTime()).plusDays(2).toDate());

		tagService.save(tag2);

	}

}
