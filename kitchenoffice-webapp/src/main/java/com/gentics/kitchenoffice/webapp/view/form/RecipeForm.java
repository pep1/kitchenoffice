package com.gentics.kitchenoffice.webapp.view.form;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.gentics.kitchenoffice.data.Incredient;
import com.gentics.kitchenoffice.data.Recipe;
import com.gentics.kitchenoffice.webapp.view.form.field.ImageField;
import com.gentics.kitchenoffice.webapp.view.form.field.IngredientField;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

@Component
@Scope("prototype")
public class RecipeForm extends FieldGroup {

	private GridLayout layout;

	@Autowired
	private ImageField image;
	
	@Autowired
	private IngredientField inField;

	/**
	 * 
	 */
	private static final long serialVersionUID = 5777870393018611126L;

	public RecipeForm() {
		super();
	}


	@PostConstruct
	public void initialize() {
		layout = buildLayout();

	}

	private GridLayout buildLayout() {
		layout = new GridLayout(3, 3);
		layout.setSpacing(true);
		//layout.setMargin(new MarginInfo(false, true, false, true));

		TextField name = new TextField("Name");
		name.setWidth("100%");
		name.setNullRepresentation("");
		name.setInputPrompt("Please enter the recipe name here");
		TextField description = new TextField("Description");
		description.setWidth("100%");
		description.setNullRepresentation("");
		description.setInputPrompt("Please enter a short description here");
		RichTextArea text = new RichTextArea("Recipe Text");
		text.setWidth("100%");
		text.setHeight("100%");
		text.setNullRepresentation("Aloha would be nice here :)");

		this.bind(name, "name");
		this.bind(description, "description");
		this.bind(text, "text");
		this.bind(image, "image");
		this.bind(inField, "incredients");
		
		image.setWidth("180px");
		image.setHeight("180px");
		inField.setWidth("350px");
		layout.setRowExpandRatio(2, 1.0f);
		layout.setColumnExpandRatio(1, 1.0f);

		// component, column, row
		layout.addComponent(name, 1, 0);
		layout.addComponent(description, 1, 1);
		layout.addComponent(text, 0, 2, 1, 2);
		layout.addComponent(inField, 2, 0, 2, 2);
		layout.addComponent(image, 0, 0, 0, 1);
		

		return layout;

	}

	public GridLayout getLayout() {
		return layout;
	}
	
	@Override
	public void commit() throws CommitException{
		
		BeanItem<Recipe> item = (BeanItem<Recipe>) getItemDataSource();
		
		for(Incredient in : item.getBean().getIncredients()) {
			in.setRecipe(item.getBean());
		}
		
		super.commit();
	}
	
	@Override
	public void setItemDataSource(Item itemDataSource) {
		
		if(this.isModified()) {
			// if user made changes, discard them
			this.discard();
		}
		
		super.setItemDataSource(itemDataSource);
		
		// and switch to readonly again, except its a new one we are switching to
		BeanItem<Recipe> item = (BeanItem<Recipe>) getItemDataSource();
		if(item != null && !item.getBean().isNew()) {
			setReadOnly(true);
		}

	}

}
