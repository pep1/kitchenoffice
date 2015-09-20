package com.pep1.kitchenoffice.data.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pep1.kitchenoffice.adapter.DateAdapter;
import com.pep1.kitchenoffice.data.*;
import com.pep1.kitchenoffice.data.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.*;
import org.springframework.util.Assert;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@NodeEntity
@XmlRootElement
@NoArgsConstructor
@Getter
@Setter
public class Event extends AbstractPersistable implements Feedable {

    @NotNull
    private EventType type;

    @Fetch
    @NotNull
    @Valid
    private User creator;

    private boolean locked = false;

    @JsonIgnore
    @XmlJavaTypeAdapter(DateAdapter.class)
    @GraphProperty(propertyType = Long.class)
    @XmlAttribute(name = "creationDate")
    private Date creationDate;

    @XmlJavaTypeAdapter(DateAdapter.class)
    @GraphProperty(propertyType = Long.class)
    @XmlAttribute(name = "startDate")
    private Date startDate;

    @XmlJavaTypeAdapter(DateAdapter.class)
    @GraphProperty(propertyType = Long.class)
    @XmlAttribute(name = "endDate")
    private Date endDate;

    @Indexed
    private String description;

    @Fetch
    @RelatedTo(type = "HAS_LOCATION", direction = Direction.BOTH, elementClass = Location.class)
    private Location location;

    @Fetch
    @RelatedTo(type = "COOKING", direction = Direction.BOTH, elementClass = Recipe.class)
    private Recipe recipe;

    @Fetch
    @RelatedToVia(type = "TAKES_PART", direction = Direction.BOTH, elementClass = Participant.class)
    private Set<Participant> participants = new HashSet<Participant>();

    @Fetch
    @RelatedTo(type = "HAS_COMMENT", direction = Direction.OUTGOING, enforceTargetType = true, elementClass = Comment.class)
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

    public Date getPubDate() {
        return creationDate;
    }

    public String getLink() {
        return "/event/" + getId();
    }

}
