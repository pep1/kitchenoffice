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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NodeEntity @NoArgsConstructor
public class Comment extends AbstractPersistable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5222339515058181833L;

	@XmlJavaTypeAdapter(DateAdapter.class)
	@GraphProperty(propertyType=Long.class)
	@XmlAttribute(name="timeStamp")
	@NotNull
	@Getter @Setter
	private Date timeStamp;
	
	@Fetch
	@NotNull
	@Getter @Setter
	private User user;
	
	@NotBlank(message="Comment text should not be blank")
	@Size(min=2, message="Comment should have at least 2 characters")
	@Getter @Setter
	private String text;
	
	public Comment(User user, String text) {
		super();
		this.timeStamp = new Date();
		this.user = user;
		this.text = text;
	}

	@Override
	public String toString() {
		return String.format("Comment{\n  user='%s',\n  text=%s\n}", user.getFirstName(), text);
	}
}
