package com.gentics.kitchenoffice.webapp.view.form.ui;

import org.vaadin.mvp.uibinder.IUiBindable;
import org.vaadin.mvp.uibinder.IUiInitializable;
import org.vaadin.mvp.uibinder.annotation.UiField;

import ru.xpoft.vaadin.KitchenOfficeNavigator;

import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class ArticleFormUi extends VerticalLayout implements IUiBindable, IUiInitializable {

	
	@UiField
	private TextField name;
	@UiField
	private TextField price;
	@UiField
	private TextField unit;
	@UiField
	private CssLayout imageContainer;
	
	
	@UiField
	private HorizontalLayout actionBar;
	@UiField
	private Label spacer;
	@UiField
	private Button cancel;
	@UiField
	private Button save;
	
	
	
	
	public ArticleFormUi() {
		KitchenOfficeNavigator.bindUIToComponent(this);
	}

	@Override
	public void init() {
		setMargin(true);
		setSpacing(true);
		actionBar.setExpandRatio(spacer, 1.0f);
	}

	public TextField getName() {
		return name;
	}

	public TextField getPrice() {
		return price;
	}

	public TextField getUnit() {
		return unit;
	}

	public CssLayout getImageContainer() {
		return imageContainer;
	}

	public Button getCancel() {
		return cancel;
	}

	public Button getSave() {
		return save;
	}
	
	
	
}
