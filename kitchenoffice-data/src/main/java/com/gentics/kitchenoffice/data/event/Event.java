package com.gentics.kitchenoffice.data.event;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.GraphProperty;
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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NodeEntity
@XmlRootElement
@NoArgsConstructor
public class Event extends AbstractPersistable implements Feedable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4856195825764426200L;

	@NotNull
	@Getter @Setter
	private EventType type;

	@Fetch
	@NotNull
	@Valid
	@Getter @Setter
	private User creator;

	@Getter @Setter
	private boolean locked = false;

	@JsonIgnore
	@XmlJavaTypeAdapter(DateAdapter.class)
	@GraphProperty(propertyType = Long.class)
	@XmlAttribute(name = "creationDate")
	@Getter @Setter
	private Date creationDate;

	@XmlJavaTypeAdapter(DateAdapter.class)
	@GraphProperty(propertyType = Long.class)
	@XmlAttribute(name = "startDate")
	@Getter @Setter
	private Date startDate;

	@XmlJavaTypeAdapter(DateAdapter.class)
	@GraphProperty(propertyType = Long.class)
	@XmlAttribute(name = "endDate")
	@Getter @Setter
	private Date endDate;

	@Indexed
	@Getter @Setter
	private String description;

	@Fetch
	@RelatedTo(type = "HAS_LOCATION", direction = Direction.BOTH, elementClass = Location.class)
	@Getter @Setter
	private Location location;

	@Fetch
	@RelatedTo(type = "COOKING", direction = Direction.BOTH, elementClass = Recipe.class)
	@Getter @Setter
	private Recipe recipe;

	@Fetch
	@RelatedToVia(type = "TAKES_PART", direction = Direction.BOTH, elementClass = Participant.class)
	@Getter @Setter
	private Set<Participant> participants = new HashSet<Participant>();

	@Fetch
	@RelatedTo(type = "HAS_COMMENT", direction = Direction.OUTGOING, enforceTargetType = true, elementClass = Comment.class)
	@Getter @Setter
	private Set<Comment> comments = new HashSet<Comment>();

	public Comment addComment(Comment comment) {
		Assert.notNull(comment, "comment may not be null or empty!");

		comments.add(comment);
		return comment;
	}

	public Participant addParticipant(User user, Job job) {

		Assert.notNull(user, "user may not be null");

		// TODO: check if job can be null
		// Assert.notNull(job, "job may not be null");

		Participant p = new Participant(this, user, job);
		participants.add(p);

		return p;

	}

	@Override
	public String toString() {
		return String.format("Event{\n date=%s,\n  participants=%s\n, comments=%s\n}", startDate.toString(),
				participants.toString(), comments.toString());
	}

	@Override
	public String getContent() {

		StringBuilder builder = new StringBuilder();

		if (location != null) {
			builder.append("Location: " + location.getName() + ", " + location.getAddress());
		}

		if (description != null) {
			builder.append(", Description: " + description + "\n");
		}

		if (participants.size() > 0) {
			builder.append(", Attendees: ");
			for (Participant participant : participants) {
				builder.append("	" + participant.getUser().getFirstName() + " " + participant.getUser().getLastName()
						+ ", ");
			}
		}

		return builder.toString();
	}

	@Override
	public String getTitle() {

		StringBuilder builder = new StringBuilder();

		if (location != null) {
			builder.append(location.getName() + " - ");
		}

		builder.append(this.type);
		builder.append(", date: " + this.startDate);
		builder.append(", created by " + creator.getUsername());

		return builder.toString();
	}

	@Override
	public Date getPubDate() {
		return creationDate;
	}

	@Override
	public String getLink() {
		return "/event/" + getId();
	}

}
