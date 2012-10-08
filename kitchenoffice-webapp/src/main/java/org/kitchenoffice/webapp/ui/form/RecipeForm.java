package org.kitchenoffice.webapp.ui.form;

import org.vaadin.addons.lazyquerycontainer.LazyQueryContainer;

import com.vaadin.data.Item;
import com.vaadin.ui.Button;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;

public class RecipeForm extends Form {
	
	private String[] props = new String[] {"name", "description"};
	
	private LazyQueryContainer container;
	
	private Table table;

	private Button edit;
	
	private Button save;
	
	private Button cancel;
	
	private Button add;
	
	private HorizontalLayout footer = new HorizontalLayout();
	/**
	 * 
	 */
	private static final long serialVersionUID = 5777870393018611126L;

	public RecipeForm( LazyQueryContainer container, Table table ) {
		
		this.table = table;
		this.container = container;
		
		setVisibleItemProperties(props);
		
		setImmediate(true);
		setReadOnly(true);
		setWriteThrough(false);
		setInvalidCommitted(false);
		this.setFormFieldFactory(new RecipeFormFieldFactory());
		
		edit = new Button("Edit", this, "edit");

		save = new Button("Save", this, "commit");

		cancel = new Button("Cancel", this, "discard");
		
		add = new Button("Add", this, "add");
		
		footer.addComponent(edit);
		footer.addComponent(save);
		footer.addComponent(cancel);
		footer.addComponent(add);
		
		this.setFooter(footer);
		
	}
	
	public void edit() {
		this.setReadOnly(false);
	}
		
	public void add() {
		Object itemId = container.addItem();
		setItemDataSource(container.getItem(itemId));
		setReadOnly(false);
		
		table.setCurrentPageFirstItemId(itemId);
	}
	
	@Override
	public void commit() {
		super.commit();
		container.commit();
		
		this.setReadOnly(true);
	}
	
	@Override
	public void discard() {
		super.discard();
		
		this.setReadOnly(true);
	}
	
	@Override
	public void setItemDataSource(Item item) {
		super.setItemDataSource(item);
		this.setReadOnly(true);
	}
	
	@Override
    protected void attachField(Object propertyId, Field field) {
		
		for(int i = 0; i < props.length; i++) {
			if(props[i].equals(propertyId)) {
				this.getLayout().addComponent(field);
			}
			
		}
	}

}
