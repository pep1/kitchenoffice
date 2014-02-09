package com.gentics.kitchenoffice.data;

import java.net.URL;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.springframework.data.annotation.Transient;
import org.springframework.data.neo4j.annotation.NodeEntity;

import com.gentics.kitchenoffice.server.storage.Storable;

@SuppressWarnings("serial")
@NodeEntity
public class Image extends AbstractPersistable implements Storable {

	@JsonIgnore
	public static final String STORAGE_TYPE = "img";

	@JsonProperty
	private String fileName;

	@JsonIgnore
	private long size;

	@JsonIgnore
	private int width;

	@JsonIgnore
	private int height;

	@Transient
	private URL url;

	@Override
	@JsonProperty
	public String getFileName() {
		return fileName;
	}

	@Override
	@JsonIgnore
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}

	@Override
	public String getStorageType() {
		return STORAGE_TYPE;
	}

}
