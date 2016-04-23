package com.gentics.kitchenoffice.server.storage;

import org.codehaus.jackson.annotate.JsonIgnore;

public interface Storable {

	@JsonIgnore
	public String getStorageType();

	@JsonIgnore
	public String getFileName();

	public void setFileName(String fileName);

}
