package com.gentics.kitchenoffice.data.event;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.joda.time.DateTime;
import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;
import org.springframework.data.neo4j.annotation.RelatedToVia;
import org.springframework.util.Assert;

import com.gentics.kitchenoffice.adapter.DateAdapter;
import com.gentics.kitchenoffice.data.AbstractPersistable;
import com.gentics.kitchenoffice.data.Comment;
import com.gentics.kitchenoffice.data.Job;
import com.gentics.kitchenoffice.data.Participant;
import com.gentics.kitchenoffice.data.Recipe;
import com.gentics.kitchenoffice.data.user.User;

@NodeEntity
@XmlRootElement
public class Event extends AbstractPersistable {

	private EventType type;

	@Fetch
	private User creator;
	
	@JsonIgnore
	private Date creationDate;

	@Indexed
	@XmlJavaTypeAdapter(DateAdapter.class)
	private Date date;

	@Indexed
	private String description;

	@Fetch
	@RelatedTo(type = "HAS_LOCATION", direction = Direction.BOTH)
	private Location location;

	@Fetch
	@RelatedTo(type = "COOKING", direction = Direction.BOTH)
	private Recipe recipe;

	@Fetch
	@RelatedToVia(type = "TAKES_PART", direction = Direction.BOTH)
	private Set<Participant> participants = new HashSet<Participant>();

	@Fetch
	@RelatedTo(type = "EVENT_HAS_COMMENT", direction = Direction.OUTGOING)
	private Set<Comment> comments = new HashSet<Comment>();

	public Event() {
	}

	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
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

	public String getDesciption() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public Participant addParticipant(User user, Job job) {

		Assert.notNull(user, "user may not be null");
		Assert.notNull(job, "job may not be null");

		Participant p = new Participant(this, user, job);

		participants.add(p);

		return p;

	}

	@Override
	public String toString() {
		return String.format(
				"Event{\n date=%s,\n  participants=%s\n, comments=%s\n}",
				date.toString(), participants.toString(), comments.toString());
	}

}
