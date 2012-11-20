package com.gentics.kitchenoffice.webapp.container.util;

import com.vaadin.data.Item;
import com.vaadin.data.Property;

import java.util.EventObject;

public class ItemChangedEvent extends EventObject {
	
	private Property<?> property;

	public ItemChangedEvent(Item source, Property<?> property) {
		super(source);
		if (property == null) {
			throw new IllegalArgumentException("Property cannot be null");
		}
		this.property = property;
	}

	public Item getItem() {
		return (Item) getSource();
	}

	public Property<?> getProperty() {
		return property;
	}
}
