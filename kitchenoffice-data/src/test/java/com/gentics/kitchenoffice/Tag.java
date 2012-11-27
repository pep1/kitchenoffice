package com.gentics.kitchenoffice;

import static org.neo4j.graphdb.Direction.BOTH;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

import com.gentics.kitchenoffice.data.AbstractPersistable;

@NodeEntity
public class Tag extends AbstractPersistable {

	private final static String TAGGED_TAG = "TAGGED_TAG";

	@Indexed(unique = true)
	String name;

	@Fetch
	@RelatedTo(type = TAGGED_TAG, direction = BOTH)
	private Set<Tag> tags = new HashSet<Tag>();

	@Fetch
	@RelatedTo(elementClass = Auto.class, type = "TAG")
	private Set<Auto> taggedAutos = new HashSet<Auto>();

	public Tag(String name) {
		this.name = name;
	}

	public Tag() {
	}

	public Set<Auto> getTaggedAutos() {
		return this.taggedAutos;
	}

	public Set<Tag> getTags() {
		return this.tags;
	}

	public void addTag(Tag tag) {
		this.tags.add(tag);
	}

	public void addTag(String name) {
		this.tags.add(new Tag(name));
	}

	public void tag(Auto auto) {
		this.taggedAutos.add(auto);
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
