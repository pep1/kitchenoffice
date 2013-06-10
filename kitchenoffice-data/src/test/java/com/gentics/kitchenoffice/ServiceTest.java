package com.gentics.kitchenoffice;

import org.apache.log4j.Logger;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.data.neo4j.support.node.Neo4jHelper;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.transaction.annotation.Transactional;

import com.gentics.kitchenoffice.service.EventService;


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

}
