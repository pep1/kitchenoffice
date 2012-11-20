package com.gentics.kitchenoffice.webapp.view;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.VaadinView;

import com.gentics.kitchenoffice.data.Recipe;
import com.gentics.kitchenoffice.service.KitchenOfficeUserService;
import com.gentics.kitchenoffice.webapp.container.RecipeContainer;
import com.gentics.kitchenoffice.webapp.view.form.RecipeForm;
import com.gentics.kitchenoffice.webapp.view.util.AbstractItemSelectionView;
import com.gentics.kitchenoffice.webapp.view.util.KitchenOfficeViewInterface;
import com.gentics.kitchenoffice.webapp.view.util.MenuEntrySortOrder;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Button.ClickEvent;

@Component
@Scope("prototype")
@VaadinView(value = RecipeView.NAME, cached = true)
@MenuEntrySortOrder(1)
public class RecipeView extends AbstractItemSelectionView<Recipe> implements ValueChangeListener, ClickListener {

	public static final String NAME = "recipes";

	private static final String VIEW_ROLE = KitchenOfficeUserService.ROLE_USER_NAME;

	public static final Object[] visibleColumns = new Object[] { "id", "name",
			"description" };

	private static Logger log = Logger.getLogger(RecipeView.class);

	@Autowired
	private RecipeForm form;

	private Button edit;
	private Button save;
	private Button cancel;
	private Button add;

	private HorizontalLayout footer = new HorizontalLayout();
	
	public RecipeView() {
		super(RecipeContainer.class);
	}

	@PostConstruct
	public void PostConstruct() {

		log.debug("initializing Recipe View : " + this.toString());

		buildLayout();

	}

	private void buildLayout() {

		HorizontalLayout splitter = new HorizontalLayout();

		table.setImmediate(true);
		table.setContainerDataSource(container);
		table.setVisibleColumns(visibleColumns);
		table.addValueChangeListener(this);
		table.setSelectable(true);

		splitter.addComponent(table);
		table.setSizeFull();

		splitter.addComponent(form.getLayout());
		form.getLayout().setSizeFull();
		form.getLayout().setWidth("320px");

		splitter.setExpandRatio(table, 1.0F);

		addComponent(splitter);
		splitter.setSizeFull();

		edit = new Button("Edit", this);
		save = new Button("Save", this);
		cancel = new Button("Cancel", this);
		add = new Button("Add", this);

		edit.setEnabled(false);
		edit.setDisableOnClick(true);
		save.setEnabled(false);
		save.setDisableOnClick(true);
		cancel.setEnabled(false);
		cancel.setDisableOnClick(true);
		add.setEnabled(true);
		add.setDisableOnClick(true);

		footer.addComponent(edit);
		footer.addComponent(save);
		footer.addComponent(cancel);
		footer.addComponent(add);
		this.addComponent(footer);
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

		if (table.getValue() != null) {
			edit.setEnabled(true);
		}

		add.setEnabled(true);
		cancel.setEnabled(false);
		save.setEnabled(false);

	}

	public void edit() {
		form.setReadOnly(false);

		add.setEnabled(false);
		cancel.setEnabled(true);
		save.setEnabled(true);
	}

	public void add() {

		Recipe newRecipe = new Recipe();
		Object itemId = container.addItem(newRecipe);
		form.setItemDataSource(container.getItem(itemId));
		form.setReadOnly(false);

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
