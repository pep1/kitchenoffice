package com.gentics.kitchenoffice.storage;

import java.io.File;

public interface Storage<T extends Storable> {
	
	public File moveFile(File file, String newLocation);
	
	public File createTempFile(String fileName, String mimeType);
	
	public boolean deleteFile(String filePath);
	
	public boolean deleteFile(File file);
}
