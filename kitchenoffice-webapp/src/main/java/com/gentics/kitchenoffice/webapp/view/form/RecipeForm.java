package com.gentics.kitchenoffice.webapp.view.form;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.gentics.kitchenoffice.data.Recipe;
import com.gentics.kitchenoffice.webapp.view.form.field.ImageField;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextField;

@Component
@Scope("prototype")
public class RecipeForm extends FieldGroup {

	private GridLayout layout;

	@Autowired
	private ImageField image;

	/**
	 * 
	 */
	private static final long serialVersionUID = 5777870393018611126L;

	public RecipeForm() {

	}

	@PostConstruct
	public void initialize() {
		layout = buildLayout();

		setReadOnly(true);
	}

	private GridLayout buildLayout() {
		layout = new GridLayout(2, 3);

		TextField name = new TextField("Name");
		name.setNullRepresentation("");
		TextField description = new TextField("Description");
		description.setNullRepresentation("");
		RichTextArea text = new RichTextArea("Recipe Text");
		text.setNullRepresentation("");

		this.bind(name, "name");
		this.bind(description, "description");
		this.bind(text, "text");
		this.bind(image, "image");

		// component, column, row
		layout.addComponent(name, 1, 0);
		layout.addComponent(description, 1, 1);
		layout.addComponent(text, 0, 2, 1, 2);
		layout.addComponent(image, 0, 0);

		return layout;

	}

	public GridLayout getLayout() {
		return layout;
	}
	
	@Override
	public void setItemDataSource(Item itemDataSource) {
		
		if(this.isModified()) {
			// if user made changes, diguard them
			this.discard();
		}
		
		super.setItemDataSource(itemDataSource);
		
		// and switch to readonly again, except its a new one we are switching to
		BeanItem<Recipe> item = (BeanItem<Recipe>) itemDataSource;
		if(item != null && !item.getBean().isNew()) {
			setReadOnly(true);
		}

	}

}
