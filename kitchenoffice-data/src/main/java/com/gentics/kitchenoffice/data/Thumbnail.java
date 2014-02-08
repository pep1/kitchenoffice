package com.gentics.kitchenoffice.data;

import java.io.File;

import com.gentics.kitchenoffice.server.storage.Storable;

public class Thumbnail implements Storable {

	public static final String STORAGE_TYPE = "img" + File.separator + "thumb";

	private String fileName;

	private String url;

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String getStorageType() {
		return Thumbnail.STORAGE_TYPE;
	}

	@Override
	public String getFileName() {
		return fileName;
	}

	@Override
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
