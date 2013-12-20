package com.gentics.kitchenoffice.server.storage;

public interface Storable {

	public String getStorageType();

	public String getFileName();

	public void setFileName(String fileName);

}
