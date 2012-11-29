package com.gentics.kitchenoffice.webapp.view.component;

import org.vaadin.mvp.uibinder.IUiBindable;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;

public class HTMLLabel extends Label{

	
	public HTMLLabel() {
		setContentMode(ContentMode.HTML);
	}
}
