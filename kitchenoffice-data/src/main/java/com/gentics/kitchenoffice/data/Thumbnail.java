package com.gentics.kitchenoffice.data;

import java.io.File;
import java.net.URL;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.gentics.kitchenoffice.server.storage.Storable;

public class Thumbnail implements Storable {

	@JsonIgnore
	public static final String STORAGE_TYPE = "img" + File.separator + "thumb";

	@JsonIgnore
	private String fileName;

	private URL url;

	/**
	 * @return the url
	 */
	public URL getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(URL url) {
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
