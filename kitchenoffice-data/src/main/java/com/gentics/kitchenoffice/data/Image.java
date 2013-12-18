package com.gentics.kitchenoffice.data;

import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;

import com.gentics.kitchenoffice.storage.Storable;

@NodeEntity
public class Image extends AbstractPersistable implements Storable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3712876154954811045L;

	@Indexed
	private String fileName;
 
	private String type;
	
	private long size;

	private String filePath;
	
	private int width;
	
	private int height;
	
	public Image() {
		
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
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
	
}
