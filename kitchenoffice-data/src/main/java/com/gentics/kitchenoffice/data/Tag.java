package com.gentics.kitchenoffice.data;

import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.springframework.data.neo4j.annotation.GraphProperty;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.support.index.IndexType;
import org.springframework.util.Assert;

import com.gentics.kitchenoffice.adapter.DateAdapter;
import lombok.Getter;
import lombok.Setter;

@NodeEntity
public class Tag extends AbstractPersistable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7643855471172418354L;

	@XmlJavaTypeAdapter(DateAdapter.class)
	@GraphProperty(propertyType = Long.class)
	@XmlAttribute(name="timstamp")
	@Getter @Setter
	private Date timeStamp;

	@Indexed(indexType = IndexType.FULLTEXT, indexName = "tagnamesearch")
	@Getter @Setter
	private String name;

	public Tag() {
		timeStamp = new Date();
	}

	public Tag(String tagName) {
		Assert.hasText(tagName);

		timeStamp = new Date();
		name = tagName;
	}

	@Override
	public String toString() {
		return String.format("Tag{\n  name=%s\n}", name.toString());
	}

}
