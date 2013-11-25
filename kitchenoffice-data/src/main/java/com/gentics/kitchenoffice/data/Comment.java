package com.gentics.kitchenoffice.data;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.GraphProperty;
import org.springframework.data.neo4j.annotation.NodeEntity;

import com.gentics.kitchenoffice.adapter.DateAdapter;
import com.gentics.kitchenoffice.data.user.User;

@NodeEntity
public class Comment extends AbstractPersistable{

	@XmlJavaTypeAdapter(DateAdapter.class)
	@GraphProperty(propertyType=Long.class)
	@XmlAttribute(name="timeStamp")
	@NotNull
	private Date timeStamp;
	
	@Fetch
	@NotNull
	private User user;
	
	@NotBlank(message="Comment text should not be blank")
	@Size(min=2, message="Comment should have at least 2 characters")
	private String text;
	
	public Comment() {
		
	}

	public Comment(User user, String text) {
		super();
		this.timeStamp = new Date();
		this.user = user;
		this.text = text;
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

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	@Override
	public String toString() {
		return String.format("Comment{\n  user='%s',\n  text=%s\n}", user.getFirstName(), text);
	}
}
