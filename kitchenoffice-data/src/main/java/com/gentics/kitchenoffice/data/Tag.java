package com.gentics.kitchenoffice.data;

import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.springframework.data.neo4j.annotation.GraphProperty;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.support.index.IndexType;
import org.springframework.util.Assert;

import com.gentics.kitchenoffice.adapter.DateAdapter;


@NodeEntity
public class Tag extends AbstractPersistable{
	
	@XmlJavaTypeAdapter(DateAdapter.class)
	@GraphProperty(propertyType=Long.class)
	private Date timeStamp;
	
	@Indexed(unique = true, indexType = IndexType.FULLTEXT, indexName = "tagsearch")
	private String tag;
	
	public Tag() {
		timeStamp = new Date();
	}
	
	public Tag(String tagName) {
		Assert.hasText(tagName);
		
		timeStamp = new Date();
		tag = tagName;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}
	
	@Override
	public String toString() {
		return String.format("Tag{\n  tag=%s\n}", tag.toString());
	}
	
}
