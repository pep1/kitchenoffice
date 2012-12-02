package com.gentics.kitchenoffice.webapp.view.component;

import org.vaadin.mvp.uibinder.IUiBindable;
import org.vaadin.mvp.uibinder.IUiInitializable;
import org.vaadin.mvp.uibinder.annotation.UiField;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;

public class IngredientAdd extends HorizontalLayout implements IUiBindable, IUiInitializable, ValueChangeListener{

	@UiField
	private ComboBox input;
	
	@UiField
	private Button add;
	
	@UiField
	private TextField amount;
	
	@UiField
	private Button save;
	
	public IngredientAdd() {
	}
	
	@Override
	public void init() {
		setWidth("100%");
		setSpacing(false);
		
		input.addValueChangeListener(this);
		
		this.setExpandRatio(input, 1.0f);
	}

	public ComboBox getInput() {
		return input;
	}
	
	public TextField getAmount() {
		return amount;
	}

	public Button getAddButton() {
		return add;
	}
	
	public Button getSaveButton() {
		return save;
	}
	
	public void reset() {
		input.setValue(input.getNullSelectionItemId());
		amount.setValue("");
	}

	@Override
	public void valueChange(ValueChangeEvent event) {
		// reset amount when changing article selection
		amount.setValue("");
	}
	
}
