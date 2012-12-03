package com.gentics.kitchenoffice.data;

import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.RelationshipEntity;
import org.springframework.data.neo4j.annotation.StartNode;

@RelationshipEntity(type="TAKES_PART")
public class Participant extends AbstractPersistable{

	@Fetch
	@StartNode
    private Event event;
	
	@Fetch
	@EndNode
    private User user;
	
	//TODO has to be externalized to own entity
	private String job;
	
	public Participant() {
		
	}

	public Participant(Event event, User user, String job) {
		super();
		this.event = event;
		this.user = user;
		this.job = job;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}
	
	@Override
	public String toString() {
		return String.format("Participant{\n  name='%s',\n  job=%s,\n}", user.getFirstName(), job);
	}
}
