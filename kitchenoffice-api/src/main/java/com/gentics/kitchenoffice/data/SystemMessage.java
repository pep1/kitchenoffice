package com.gentics.kitchenoffice.data;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SystemMessage {

	public enum MessageType {
		note, warning, error
	}

	private MessageType type;

	private String description;

	public MessageType getType() {
		return type;
	}

	public void setType(MessageType type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
