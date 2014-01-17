package com.gentics.kitchenoffice.data;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.RelatedTo;
import org.springframework.data.neo4j.annotation.RelationshipEntity;
import org.springframework.data.neo4j.annotation.StartNode;

import com.gentics.kitchenoffice.data.event.Event;
import com.gentics.kitchenoffice.data.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@RelationshipEntity(type = "TAKES_PART") @NoArgsConstructor
public class Participant extends AbstractPersistable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5706768770697984647L;

	@Fetch
	@StartNode
	@JsonIgnore
	@Getter @Setter
	private Event event;

	@Fetch
	@EndNode
	@Getter @Setter
	private User user;

	@Fetch
	@RelatedTo(type = "HAS_JOB", direction = Direction.BOTH, elementClass = Job.class)
	@Getter @Setter
	private Job job;

	public Participant(Event event, User user, Job job) {
		super();
		this.event = event;
		this.user = user;
		this.job = job;
	}

	@Override
	public String toString() {
		return String.format("Participant{\n  name='%s',\n  job=%s,\n}", user.getFirstName(), job);
	}
}
