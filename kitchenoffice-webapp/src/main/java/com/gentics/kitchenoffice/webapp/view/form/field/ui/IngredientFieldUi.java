package com.gentics.kitchenoffice.webapp.view.form.field.ui;

import org.springframework.context.annotation.Scope;
import org.vaadin.mvp.uibinder.IUiBindable;
import org.vaadin.mvp.uibinder.IUiInitializable;
import org.vaadin.mvp.uibinder.annotation.UiField;

import ru.xpoft.vaadin.KitchenOfficeNavigator;

import com.gentics.kitchenoffice.webapp.view.component.HTMLLabel;
import com.gentics.kitchenoffice.webapp.view.component.IngredientAdd;
import com.vaadin.ui.VerticalLayout;

@org.springframework.stereotype.Component
@Scope("prototype")
public class IngredientFieldUi extends VerticalLayout implements IUiBindable, IUiInitializable {

	@UiField
	private HTMLLabel header;
	@UiField
	private VerticalLayout ingredientContainer;
	@UiField
	private IngredientAdd addComponent;
	
	public IngredientFieldUi() {
		KitchenOfficeNavigator.bindUIToComponent(this);
	}
	
	
	
	@Override
	public void init() {
		this.setExpandRatio(ingredientContainer, 1.0f);
	}


	public VerticalLayout getIngredientContainer() {
		return ingredientContainer;
	}

	public IngredientAdd getAddComponent() {
		return addComponent;
	}
	
	

}
