package com.gentics.kitchenoffice.webapp.view.form.field.ui;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.vaadin.mvp.uibinder.IUiBindable;
import org.vaadin.mvp.uibinder.annotation.UiField;

import ru.xpoft.vaadin.KitchenOfficeNavigator;

import com.gentics.kitchenoffice.data.Image;
import com.gentics.kitchenoffice.data.Incredient;
import com.gentics.kitchenoffice.webapp.util.WebappHelper;
import com.vaadin.ui.Button;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

@org.springframework.stereotype.Component
@Scope("prototype")
public class IngredientFieldSlotUi extends HorizontalLayout implements IUiBindable {

	@UiField
	private Embedded image;
	@UiField
	private VerticalLayout infoContainer;
	@UiField
	private Label name;
	@UiField
	private HorizontalLayout descriptionContainer;
	@UiField
	private HorizontalLayout nameContainer;
	@UiField
	private TextField amount;
	@UiField
	private Label unit;
	@UiField
	private Label price;
	
	@UiField
	private Button remove;
	@UiField
	private Button edit;
	
	@UiField
	private Label spacer;
	
	private Image im;
	
	@Autowired
	private WebappHelper helper;
	
	public IngredientFieldSlotUi() {
		
		KitchenOfficeNavigator.bindUIToComponent(this);
		setSizeFull();
		setSpacing(true);
		setExpandRatio(infoContainer, 1.0f);
		nameContainer.setExpandRatio(spacer, 1.0f);
		
	}
	
	public void setIngredient(Incredient in) {
		im = in.getArticle().getImage();
		name.setValue(in.getArticle().getName());
		amount.setValue(Double.toString(in.getAmount()));
		unit.setValue(in.getArticle().getUnit());
		price.setValue(Double.toString(in.getArticle().getPrice() * in.getAmount()));
	}
	
	@Override
	public void attach() {
		super.attach(); // Must call.

		image.setSource(helper.getImageThumbnail(im, 40));
	}
	
	public void setReadOnly(boolean readOnly) {
		amount.setReadOnly(readOnly);
		remove.setVisible(!readOnly);
		edit.setVisible(!readOnly);
	}

	public TextField getAmount() {
		return amount;
	}

	public Button getRemoveButton() {
		return remove;
	}

	public Button getEditButton() {
		return edit;
	}
	
	
}
