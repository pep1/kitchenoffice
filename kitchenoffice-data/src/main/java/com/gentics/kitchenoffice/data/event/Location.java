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

@NodeEntity
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

	public Location() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Float getLatitude() {
		return latitude;
	}

	public void setLatitude(Float latitude) {
		this.latitude = latitude;
	}

	public Float getLongitude() {
		return longitude;
	}

	public void setLongitude(Float longitude) {
		this.longitude = longitude;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public Set<Tag> getTags() {
		return tags;
	}

	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}

	public Set<User> getSubscribers() {
		return subscribers;
	}

	public void setSubscribers(Set<User> subscribers) {
		this.subscribers = subscribers;
	}

	@Override
	public String toString() {
		return String
				.format("Location [name=%s, address=%s, website=%s, description=%s, latitude=%s, longitude=%s, tags=%s, image=%s]",
						name, address, website, description, latitude, longitude, tags, image);
	}

}
