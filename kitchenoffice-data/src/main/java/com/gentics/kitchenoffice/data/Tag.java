package com.gentics.kitchenoffice.data;

import java.util.Date;

import org.springframework.data.neo4j.annotation.NodeEntity;


@NodeEntity
public class Tag extends AbstractPersistable{
	
	private Date timeStamp;
	
	private String tag;
	
	public Tag() {
		
		timeStamp = new Date();
		
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
