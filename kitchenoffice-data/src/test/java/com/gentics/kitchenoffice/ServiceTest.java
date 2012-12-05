package com.gentics.kitchenoffice;

import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.conversion.EndResult;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.data.neo4j.support.node.Neo4jHelper;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.gentics.kitchenoffice.data.Article;
import com.gentics.kitchenoffice.data.Job;
import com.gentics.kitchenoffice.data.Recipe;
import com.gentics.kitchenoffice.data.Tag;
import com.gentics.kitchenoffice.data.event.CookEvent;
import com.gentics.kitchenoffice.data.event.Event;
import com.gentics.kitchenoffice.data.user.User;
import com.gentics.kitchenoffice.repository.ArticleRepository;
import com.gentics.kitchenoffice.repository.CommentRepository;
import com.gentics.kitchenoffice.repository.EventRepository;
import com.gentics.kitchenoffice.repository.JobRepository;
import com.gentics.kitchenoffice.repository.RecipeRepository;
import com.gentics.kitchenoffice.repository.TagRepository;
import com.gentics.kitchenoffice.repository.UserRepository;
import com.gentics.kitchenoffice.service.EventService;

import static org.junit.Assert.*;


@ContextConfiguration(locations = "classpath:/spring/kitchenContext.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ServiceTest {
	
	private static Logger log = Logger.getLogger(ServiceTest.class);
	
	
	@Autowired
	private EventService eventService;
	
	@Autowired
	private Neo4jTemplate template;
	
	@Rollback(false)
	@BeforeTransaction
	public void cleanUpGraph() {
		Neo4jHelper.cleanDb(template);
	}
	
	@Test
	public void eventRepoTest() {
		
		log.debug("starting first Test");
		
		List<Class<? extends Event>> events = eventService.getAvailableEvents();
		
		log.debug("found event classes: " + events);
		
		Assert.notEmpty(events);
		Assert.state(events.size() == 3);
	}
}
