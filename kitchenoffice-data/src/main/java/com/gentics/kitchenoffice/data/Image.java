package com.gentics.kitchenoffice.data;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.annotation.Transient;
import org.springframework.data.neo4j.annotation.NodeEntity;

import com.gentics.kitchenoffice.server.storage.Storable;

@SuppressWarnings("serial")
@NodeEntity
public class Image extends AbstractPersistable implements Storable {

	public static final String STORAGE_TYPE = "image";

	private String fileName;

	private String mimeType;

	private long size;

	private int width;

	private int height;

	@Transient
	private Map<Integer, Thumbnail> thumbs = new HashMap<Integer, Thumbnail>();

	public Image() {

	}

	@Override
	public String getFileName() {
		return fileName;
	}

	@Override
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
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

	public Map<Integer, Thumbnail> getThumbs() {
		return thumbs;
	}

	public void setThumbs(Map<Integer, Thumbnail> thumbs) {
		this.thumbs = thumbs;
	}

	@Override
	public String getStorageType() {
		return STORAGE_TYPE;
	}

}
