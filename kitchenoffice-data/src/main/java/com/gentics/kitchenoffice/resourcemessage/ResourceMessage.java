package com.gentics.kitchenoffice.resourcemessage;

import java.io.Serializable;

public class ResourceMessage implements Serializable{

	private String resourceKey;
	
	public ResourceMessage(String resourceKey) {
		this.resourceKey = resourceKey;
	}

	public String getResourceKey() {
		return resourceKey;
	}

	public void setResourceKey(String resourceKey) {
		this.resourceKey = resourceKey;
	}
	
}
