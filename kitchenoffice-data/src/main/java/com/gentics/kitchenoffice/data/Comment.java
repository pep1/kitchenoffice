package com.gentics.kitchenoffice.data;

import java.util.Date;

import org.springframework.data.neo4j.annotation.NodeEntity;

import com.gentics.kitchenoffice.data.user.User;

@NodeEntity
public class Comment extends AbstractPersistable{

	private Date timeStamp;
	
	private User user;
	
	private String comment;
	
	public Comment() {
		
	}

	public Comment(User user, String comment) {
		super();
		this.timeStamp = new Date();
		this.user = user;
		this.comment = comment;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	@Override
	public String toString() {
		return String.format("Comment{\n  user='%s',\n  comment=%s\n}", user.getFirstName(), comment);
	}
}
