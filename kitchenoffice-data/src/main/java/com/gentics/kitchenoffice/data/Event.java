package com.gentics.kitchenoffice.data;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedToVia;
import org.springframework.data.neo4j.annotation.RelatedTo;
import org.springframework.util.Assert;

@NodeEntity
public class Event extends AbstractPersistable{
	
	@Indexed
	private Date date;
	
	@Fetch
	private Recipe recipe;
	
	@Fetch
	@RelatedToVia(type = "TAKES_PART", direction = Direction.BOTH)
	private Set<Participant> participants = new HashSet<Participant>();
	
	@Fetch
	@RelatedTo(type = "EVENT_HAS_COMMENT", direction = Direction.BOTH)
	private Set<Comment> comments = new HashSet<Comment>();
	
	public Event() {
		
	}
	
	public Event(Date date, Recipe recipe) {
		super();
		this.date = date;
		this.recipe = recipe;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Recipe getRecipe() {
		return recipe;
	}

	public void setRecipe(Recipe recipe) {
		this.recipe = recipe;
	}

	public Set<Participant> getParticipants() {
		return participants;
	}

	public void setParticipants(Set<Participant> participants) {
		this.participants = participants;
	}
	
	public Set<Comment> getComments() {
		return comments;
	}

	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}
	
	public Comment addComment(User user, String comment) {
		Assert.notNull(user, "User may not be null!");
		Assert.hasLength(comment, "comment may not be null or empty!");
		
		Comment c = new Comment(user, comment);
		
		comments.add(c);
		
		return c;
	}

	public Participant addParticipant (User user, String job) {
		
		Assert.notNull(user, "user may not be null");
		Assert.hasLength(job, "job may not be null or empty");
		
		Participant p = new Participant(this, user, job);
		
		participants.add(p);
		
		return p;
		
	}
	
	@Override
	public String toString() {
		return String.format("Event{\n  recipe='%s',\n  date=%s,\n  participants=%s\n, comments=%s\n}", recipe.getName(), date.toString(), participants.toString(), comments.toString());
	}
	
}
