package com.gentics.kitchenoffice.webapp.view.component;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.vaadin.mvp.uibinder.IUiInitializable;
import org.vaadin.mvp.uibinder.annotation.UiField;

import ru.xpoft.vaadin.KitchenOfficeNavigator;

import com.gentics.kitchenoffice.data.Recipe;
import com.gentics.kitchenoffice.webapp.controller.RecipeController;
import com.gentics.kitchenoffice.webapp.view.component.table.ImageColumnGenerator;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

@Component("CookEventSelect")
@Scope("prototype")
public class RecipeSelect extends EventSelect implements IUiInitializable, ValueChangeListener {
	
	public static final Object[] visibleColumns = new Object[] { "thumb", "id", "name" };
	public static final String[] columnHeaders = new String[] { "", "id", "Name" };

	@Autowired
	private RecipeController controller;
	
	@Autowired
	private ImageColumnGenerator imageColumnGenerator;
	
	@UiField
	private Table table;
	@UiField
	private HorizontalLayout recipeContainer;
	@UiField
	private Embedded recipeImage;
	@UiField
	private VerticalLayout infoContainer;
	@UiField
	private Label recipeName;
	@UiField
	private Label recipeDescription;
	
	public RecipeSelect() {
		KitchenOfficeNavigator.bindUIToComponent(this);
	}
	
	@PostConstruct
	public void postConstruct() {
		
		table.setWidth("300px");
		table.setHeight("100%");
		table.setImmediate(true);
		
		table.setContainerDataSource(controller.getContainer());
		
		table.addGeneratedColumn("thumb", imageColumnGenerator);
		table.setVisibleColumns(visibleColumns);
		table.setColumnHeaders(columnHeaders);
		table.setColumnExpandRatio("name", 1.0f);
		
		table.setSelectable(true);
		table.addValueChangeListener(this);
	}

	@Override
	public void init() {
		setSizeFull();
	}

	@Override
	public void valueChange(ValueChangeEvent event) {
		BeanItem<Recipe> item = controller.getContainer().getItem(table.getValue());
		
		if(item == null) {
			return;
		}
		
		Recipe recipe = item.getBean();
		
		recipeName.setValue(recipe.getName());
		recipeDescription.setValue(recipe.getDescription());
		
	}
	
	public Recipe getValue() {
		return (Recipe) table.getValue();
	}
	
}
