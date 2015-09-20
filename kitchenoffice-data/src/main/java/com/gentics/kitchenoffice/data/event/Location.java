package com.gentics.kitchenoffice.data.event;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;
import org.springframework.data.neo4j.support.index.IndexType;

import com.gentics.kitchenoffice.data.AbstractPersistable;
import com.gentics.kitchenoffice.data.Image;
import com.gentics.kitchenoffice.data.Tag;
import com.gentics.kitchenoffice.data.user.User;
import lombok.Getter;
import lombok.Setter;

@NodeEntity
public class Location extends AbstractPersistable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5410871118357785312L;

	@Indexed(indexType = IndexType.FULLTEXT, indexName = "locationnamesearch")
	@Getter @Setter
	private String name;

	@NotBlank(message = "Address is mandatory")
	@Size(min = 5, message = "Address should have at least 5 characters")
	@Getter @Setter
	private String address;

	@URL
	@Getter @Setter
	private String website;

	@Getter @Setter
	private String description;

	@NotNull
	@Getter @Setter
	private Float latitude;

	@NotNull
	@Getter @Setter
	private Float longitude;

	@Fetch
	@RelatedTo(type = "HAS_TAG", direction = Direction.BOTH, enforceTargetType = true, elementClass = Tag.class)
	@Getter @Setter
	private Set<Tag> tags = new HashSet<Tag>();

	@Fetch
	@RelatedTo(type = "HAS_IMAGE", direction = Direction.OUTGOING, elementClass = Image.class)
	@Getter @Setter
	private Image image;

	@Fetch
	@JsonIgnore
	@RelatedTo(type = "SUBSCRIBES", direction = Direction.BOTH, elementClass = User.class)
	@Getter @Setter
	private Set<User> subscribers = new HashSet<User>();

	public Location() {
		super();
	}

	@Override
	public String toString() {
		return String
				.format("Location [name=%s, address=%s, website=%s, description=%s, latitude=%s, longitude=%s, tags=%s, image=%s]",
						name, address, website, description, latitude, longitude, tags, image);
	}

}
