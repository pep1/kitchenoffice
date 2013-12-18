package com.gentics.kitchenoffice.data;

import org.springframework.data.neo4j.annotation.NodeEntity;

import com.gentics.kitchenoffice.server.storage.Storable;

@NodeEntity
public class Image extends AbstractPersistable implements Storable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3712876154954811045L;

	private static final String STORAGE_TYPE = "image";

	private String fileName;

	private String mimeType;

	private long size;

	private int width;

	private int height;

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

	@Override
	public String getStorageType() {
		return STORAGE_TYPE;
	}

}
