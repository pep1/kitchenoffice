package com.gentics.kitchenoffice.webapp.view;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.vaadin.mvp.uibinder.IUiBindable;
import org.vaadin.mvp.uibinder.IUiInitializable;
import org.vaadin.mvp.uibinder.annotation.UiField;

import ru.xpoft.vaadin.VaadinView;

import com.gentics.kitchenoffice.data.Recipe;
import com.gentics.kitchenoffice.service.KitchenOfficeUserService;
import com.gentics.kitchenoffice.webapp.container.RecipeContainer;
import com.gentics.kitchenoffice.webapp.view.component.table.ImageColumnGenerator;
import com.gentics.kitchenoffice.webapp.view.form.RecipeForm;
import com.gentics.kitchenoffice.webapp.view.form.field.IngredientFieldSlot;
import com.gentics.kitchenoffice.webapp.view.util.AbstractItemSelectionView;
import com.gentics.kitchenoffice.webapp.view.util.MenuEntrySortOrder;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.VerticalLayout;

@Component
@Scope("prototype")
@VaadinView(value = RecipeView.NAME, cached = true)
@MenuEntrySortOrder(1)
public class RecipeView extends AbstractItemSelectionView<Recipe> implements ValueChangeListener, ClickListener, IUiBindable, IUiInitializable {

	public static final String NAME = "recipes";

	private static final String VIEW_ROLE = KitchenOfficeUserService.ROLE_USER_NAME;

	public static final Object[] visibleColumns = new Object[] { "thumb", "id", "name" };
	public static final String[] columnHeaders = new String[] { "", "id", "Name" };

	private static Logger log = Logger.getLogger(RecipeView.class);

	@Autowired
	private RecipeForm form;
	@UiField
	private HorizontalLayout splitter;
	@UiField
	private VerticalLayout formLayout;
	@UiField
	private Button edit;
	@UiField
	private Button save;
	@UiField
	private Button cancel;
	@UiField
	private Button add;
	@UiField
	private CssLayout formContainer;
	@UiField
	private CssLayout tableContainer;
	
	@Autowired
	private ApplicationContext context;
	
	public RecipeView() {
		super(RecipeContainer.class);
	}

	@PostConstruct
	public void PostConstruct() {
		log.debug("initializing Recipe View : " + this.toString());

	}
	

	@Override
	public void init() {
		
		setSizeFull();
		
		table.setWidth("300px");
		table.setHeight("100%");
		table.setImmediate(true);
		table.setContainerDataSource(container);
		table.addGeneratedColumn("thumb", (ImageColumnGenerator)context.getBean(ImageColumnGenerator.class));
		table.setVisibleColumns(visibleColumns);
		table.setColumnHeaders(columnHeaders);
		table.setColumnExpandRatio("name", 1.0f);
		
		table.setSelectable(true);
		table.addValueChangeListener(this);
		
		tableContainer.addComponent(table);
		
		edit.addClickListener(this);
		save.addClickListener(this);
		cancel.addClickListener(this);
		add.addClickListener(this);

		edit.setEnabled(false);
		edit.setDisableOnClick(true);
		save.setEnabled(false);
		save.setDisableOnClick(true);
		cancel.setEnabled(false);
		cancel.setDisableOnClick(true);
		add.setEnabled(true);
		add.setDisableOnClick(true);
		
		
		formContainer.addComponent(form.getLayout());
		form.getLayout().setSizeFull();
		
		splitter.setExpandRatio(formLayout, 1.0f);
		formLayout.setExpandRatio(formContainer, 1.0f);
		
	}

	public String getViewRole() {
		return VIEW_ROLE;
	}

	@Override
	public void valueChange(ValueChangeEvent event) {

		BeanItem<Recipe> item = container.getItem(table.getValue());
		
		if(item == null) {
			return;
		}

		// set URI fragment to the new recipe id but don't refresh
		setURIFragmentByItem(item.getBean(), false);

		form.setItemDataSource(item);

		add.setEnabled(true);
		cancel.setEnabled(false);
		save.setEnabled(false);
		edit.setEnabled(true);
	}

	public void edit() {
		form.setReadOnly(false);

		add.setEnabled(false);
		cancel.setEnabled(true);
		save.setEnabled(true);
	}

	public void add() {

		Recipe newRecipe = new Recipe();
		container.addItem(newRecipe);
		// form.setItemDataSource will be executed in table value change handling

		log.debug("added bean item, container size now is: " + container.size());

		table.select(newRecipe);

		edit.setEnabled(false);
		cancel.setEnabled(true);
		save.setEnabled(true);
	}

	public void save() {
		try {

			form.commit();
		} catch (CommitException e) {
			log.error("Something went wrong with committing: " + e.getMessage());
			e.printStackTrace();
		}

		container.commit();

		form.setReadOnly(true);

		edit.setEnabled(true);
		cancel.setEnabled(false);
		add.setEnabled(true);
	}

	public void cancel() {
		form.discard();

		BeanItem<Recipe> item = (BeanItem<Recipe>) form.getItemDataSource();

		if (item.getBean().isNew()) {
			container.removeItem(item.getBean());
		}

		form.setReadOnly(true);

		add.setEnabled(true);
		edit.setEnabled(true);
		cancel.setEnabled(false);
		save.setEnabled(false);
	}

	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == save) {
			save();
		} else if (event.getButton() == edit) {
			edit();
		} else if (event.getButton() == add) {
			add();
		} else if (event.getButton() == cancel) {
			cancel();
		}

	}

	@Override
	public String getName() {
		return NAME;
	}

}
