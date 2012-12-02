package com.gentics.kitchenoffice.webapp.view.form;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import com.gentics.kitchenoffice.data.Article;
import com.gentics.kitchenoffice.data.Recipe;
import com.gentics.kitchenoffice.webapp.view.form.field.ImageField;
import com.gentics.kitchenoffice.webapp.view.form.ui.ArticleFormUi;
import com.gentics.kitchenoffice.webapp.view.layout.MainLayout;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

@org.springframework.stereotype.Component
@Scope("prototype")
public class ArticleForm extends FieldGroup {
	
	private static Logger log = Logger.getLogger(ArticleForm.class);
	
	private ArticleFormUi layout;

	@Autowired
	private ImageField image;
	
	public ArticleForm() {
		super();
	}
	
	@PostConstruct
	public void initialize() {
		
		log.debug("initializing new " + this.getClass().getSimpleName() + " instance..");
		
		buildLayout();
	}

	private VerticalLayout buildLayout() {
		
		layout = new ArticleFormUi();
		
		layout.getImageContainer().addComponent(image);
		
		this.bind(layout.getName(), "name");
		this.bind(layout.getUnit(), "unit");
		this.bind(layout.getPrice(), "price");
		this.bind(image, "image");
		
		return layout;
	}
	
	public ArticleFormUi getLayout() {
		return layout;
	}
	
	@Override
	public void setItemDataSource(Item itemDataSource) {
		
		if(this.isModified()) {
			// if user made changes, discard them
			this.discard();
		}
		
		super.setItemDataSource(itemDataSource);

	}

}
