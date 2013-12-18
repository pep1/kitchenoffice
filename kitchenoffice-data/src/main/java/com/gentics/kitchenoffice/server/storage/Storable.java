package com.gentics.kitchenoffice.server.storage;

public interface Storable {

	public String getStorageType();

	public String getFilename();

	public void setFilename(String fileName);

}
