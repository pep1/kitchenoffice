package com.gentics.kitchenoffice.data;

import lombok.Getter;
import lombok.Setter;
import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;
import org.springframework.data.neo4j.support.index.IndexType;

@NodeEntity
public class Job extends AbstractPersistable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8056799339793622909L;

	@Fetch
    @RelatedTo(type = "HAS_IMAGE", direction = Direction.OUTGOING, elementClass = Image.class)
	@Getter @Setter
	private Image image;
	
	@Indexed(indexType = IndexType.FULLTEXT, indexName = "jobnamesearch")
	@Getter @Setter
	private String name;
	
	@Getter @Setter
	private String description;

	public Job() {
		super();
	}

	public Job(Image image, String name, String description) {
		super();
		this.image = image;
		this.name = name;
		this.description = description;
	}

	@Override
	public String toString() {
		return String.format("Job{\n  name=%s\n}", name.toString());
	}
}
