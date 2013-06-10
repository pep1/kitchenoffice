package com.gentics.kitchenoffice.data.event;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;
import org.springframework.data.neo4j.annotation.RelatedToVia;
import org.springframework.util.Assert;

import com.gentics.kitchenoffice.data.AbstractPersistable;
import com.gentics.kitchenoffice.data.Comment;
import com.gentics.kitchenoffice.data.Job;
import com.gentics.kitchenoffice.data.Participant;
import com.gentics.kitchenoffice.data.Recipe;
import com.gentics.kitchenoffice.data.user.User;

@NodeEntity
public abstract class Event extends AbstractPersistable{
	
	private EventType type;

	@Indexed
	private Date date;
	
	@Indexed
	private String note;
	
	@Fetch
	@RelatedToVia(type = "TAKES_PART", direction = Direction.BOTH)
	private Set<Participant> participants = new HashSet<Participant>();
	
	@Fetch
    @RelatedTo(type = "HAS_LOCATION", direction = Direction.BOTH)
	private Location location = new Location();
	
	@Fetch
    @RelatedTo(type = "COOKING", direction = Direction.BOTH)
	private Recipe recipe;
	
	@Fetch
	@RelatedTo(type = "EVENT_HAS_COMMENT", direction = Direction.BOTH)
	private Set<Comment> comments = new HashSet<Comment>();
	
	public Event() {
		
	}
	
	public Event(Date date) {
		super();
		this.date = date;
	}

	public EventType getType() {
		return type;
	}

	public void setType(EventType type) {
		this.type = type;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
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
	
	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Recipe getRecipe() {
		return recipe;
	}

	public void setRecipe(Recipe recipe) {
		this.recipe = recipe;
	}

	public Comment addComment(User user, String comment) {
		Assert.notNull(user, "User may not be null!");
		Assert.hasLength(comment, "comment may not be null or empty!");
		
		Comment c = new Comment(user, comment);
		
		comments.add(c);
		
		return c;
	}

	public Participant addParticipant (User user, Job job) {
		
		Assert.notNull(user, "user may not be null");
		Assert.notNull(job, "job may not be null");
		
		Participant p = new Participant(this, user, job);
		
		participants.add(p);
		
		return p;
		
	}
	
	@Override
	public String toString() {
		return String.format("Event{\n date=%s,\n  participants=%s\n, comments=%s\n}", date.toString(), participants.toString(), comments.toString());
	}
	
}
