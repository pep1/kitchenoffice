package com.pep1.kitchenoffice.server.storage;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public interface Storage {
	
	/**
	 * Initialize the Storage
	 * 
	 * @param basePath for the public acessible files
	 */
	public void init(String basePath);

	/**
	 * Gets the url of the storable
	 * 
	 * @param storable
	 * @return the url where the storable is found online
	 */
	public URL getStorableUrl(Storable storable);

	/**
	 * returns the file for the given storable.
	 * 
	 * @param storable the storable
	 * @return the file
	 */
	public File getFileFromStorable(Storable storable);

	/**
	 * Store the file into the storage
	 * 
	 * @param file the file to be moved in the storage
	 * @param storable
	 * @throws IOException 
	 */
	public Storable persistStorable(Storable storable, File file) throws IOException;
	
	/**
	 * delete the given storable
	 * 
	 * @param storable
	 * @throws IOException 
	 */
	public void deleteStorable(Storable storable) throws IOException;
}
