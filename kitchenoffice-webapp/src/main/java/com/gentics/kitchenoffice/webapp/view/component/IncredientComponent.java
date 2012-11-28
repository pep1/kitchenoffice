package com.gentics.kitchenoffice.webapp.view.component;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

public class IncredientComponent extends HorizontalLayout{

	private Label name;
	
	public IncredientComponent(String name) {
		this.name.setValue(name);
	}
	
}
