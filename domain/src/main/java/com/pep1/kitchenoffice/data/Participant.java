package com.pep1.kitchenoffice.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pep1.kitchenoffice.data.event.Event;
import com.pep1.kitchenoffice.data.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.*;

@RelationshipEntity(type = "TAKES_PART")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Participant extends AbstractPersistable {

    @Fetch
    @StartNode
    @JsonIgnore
    private Event event;

    @Fetch
    @EndNode
    private User user;

    @Fetch
    @RelatedTo(type = "HAS_JOB", direction = Direction.BOTH, elementClass = Job.class)
    private Job job;

    public Participant(Event event, User user, Job job) {
        super();
        this.event = event;
        this.user = user;
        this.job = job;
    }
}
