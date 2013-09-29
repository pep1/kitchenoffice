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

@NodeEntity
public class Tag extends AbstractPersistable {

	@XmlJavaTypeAdapter(DateAdapter.class)
	@GraphProperty(propertyType = Long.class)
	@XmlAttribute(name="timstamp")
	private Date timeStamp;

	@Indexed(indexType = IndexType.FULLTEXT, indexName = "tagnamesearch")
	private String name;

	public Tag() {
		timeStamp = new Date();
	}

	public Tag(String tagName) {
		Assert.hasText(tagName);

		timeStamp = new Date();
		name = tagName;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return String.format("Tag{\n  name=%s\n}", name.toString());
	}

}
