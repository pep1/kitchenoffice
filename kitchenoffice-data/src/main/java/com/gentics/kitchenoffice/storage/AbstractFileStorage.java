package com.gentics.kitchenoffice.storage;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public abstract class AbstractFileStorage<T extends Storable> implements
		Storage<Storable>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6183649154315527155L;

	public String getUniqueFileName(String extension) {

		Date date = new Date();

		return new StringBuilder().append("upload_").append(date.getTime())
				.append(UUID.randomUUID()).append(".").append(extension)
				.toString();
	}
}
