package com.gentics.kitchenoffice.data;

import org.springframework.data.neo4j.annotation.NodeEntity;

import com.gentics.kitchenoffice.server.storage.Storable;
import lombok.Getter;
import lombok.Setter;

@NodeEntity
public class Image extends AbstractPersistable implements Storable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3712876154954811045L;

	private static final String STORAGE_TYPE = "image";

	@Getter @Setter
	private String fileName;

	@Getter @Setter
	private String mimeType;

	@Getter @Setter
	private long size;

	@Getter @Setter
	private int width;

	@Getter @Setter
	private int height;

	public Image() {

	}

	@Override
	public String getStorageType() {
		return STORAGE_TYPE;
	}

}
