package com.gentics.kitchenoffice.storage.file;

import java.io.File;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.springframework.util.Assert;

import com.gentics.kitchenoffice.storage.AbstractFileStorage;
import com.gentics.kitchenoffice.storage.Storable;

public class FileStorageImpl extends AbstractFileStorage<Storable> {
	
	private static Logger log = Logger.getLogger(FileStorageImpl.class);
	
	public FileStorageImpl() {
	}

	@Override
	public File moveFile(File file, String newLocation) {
		log.debug("storing file: " + file.getAbsolutePath());
		log.debug("to:" + newLocation);
		
		String fileName = file.getName();
	    
	    // Make sure the file or directory exists and isn't write protected
		Assert.isTrue(file.exists(), "ImageFile Move: no such file or directory: " + fileName);
		Assert.isTrue(file.canWrite(), "ImageFile: write protected: " + fileName);
		Assert.hasLength(newLocation, "new location should not be empty");
		
		File newFile = new File(newLocation, file.getName());
		 
        if(newFile.exists())
            newFile.delete();
		
		// Move file to new directory
		if(file.renameTo(newFile)) {
			return newFile;
		}
		
		return null;
	}

	@Override
	public File createTempFile(String directory, String fileName, String mimeType) {
		
		Assert.hasLength(fileName, "new location should not be empty");
		
		int dotPos = fileName.lastIndexOf(".");
        String extension = fileName.substring(dotPos+1).toLowerCase();
        
        String tempFileName = this.getUniqueFileName(extension);
        
		log.debug("creating new temp file: "+  directory + File.separator +  tempFileName);
        
		File f = new File(directory, tempFileName);
		
		return f;
	}

	@Override
	public boolean deleteFile(String filePath) {
		return deleteFile(new File(filePath));
	}

	@Override
	public boolean deleteFile(File file) {
		
		String fileName = file.getName();
	    
	    // Make sure the file or directory exists and isn't write protected
		Assert.isTrue(file.exists(), "ImageFile Move: no such file or directory: " + fileName);
		Assert.isTrue(file.canWrite(), "ImageFile: write protected: " + fileName);
		
		// If it is a directory, make sure it is empty
	    if (file.isDirectory()) {
	      String[] files = file.list();
	      if (files.length > 0)
	        throw new IllegalArgumentException(
	            "Delete: directory not empty: " + fileName);
	    }

	    // Attempt to delete it
	    boolean success = file.delete();

	    if (!success)
	      throw new IllegalArgumentException("Delete: deletion failed");
	    
		return success;
	}

}
