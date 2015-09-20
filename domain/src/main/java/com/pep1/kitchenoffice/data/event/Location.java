package com.pep1.kitchenoffice.data.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pep1.kitchenoffice.data.AbstractPersistable;
import com.pep1.kitchenoffice.data.Image;
import com.pep1.kitchenoffice.data.Tag;
import com.pep1.kitchenoffice.data.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;
import org.springframework.data.neo4j.support.index.IndexType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@NodeEntity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Location extends AbstractPersistable {

    /**
     *
     */
    private static final long serialVersionUID = 5410871118357785312L;

    @Indexed(indexType = IndexType.FULLTEXT, indexName = "locationnamesearch")
    private String name;

    @NotBlank(message = "Address is mandatory")
    @Size(min = 5, message = "Address should have at least 5 characters")
    private String address;

    @URL
    private String website;

    private String description;

    @NotNull
    private Float latitude;

    @NotNull
    private Float longitude;

    @Fetch
    @RelatedTo(type = "HAS_TAG", direction = Direction.BOTH, enforceTargetType = true, elementClass = Tag.class)
    private Set<Tag> tags = new HashSet<Tag>();

    @Fetch
    @RelatedTo(type = "HAS_IMAGE", direction = Direction.OUTGOING, elementClass = Image.class)
    private Image image;

    @Fetch
    @JsonIgnore
    @RelatedTo(type = "SUBSCRIBES", direction = Direction.BOTH, elementClass = User.class)
    private Set<User> subscribers = new HashSet<User>();

}
