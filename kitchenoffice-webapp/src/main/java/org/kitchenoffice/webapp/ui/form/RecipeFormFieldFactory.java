package org.kitchenoffice.webapp.ui.form;

import com.vaadin.data.Item;
import com.vaadin.ui.Component;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextField;

public class RecipeFormFieldFactory extends DefaultFieldFactory{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7676425179724777145L;

	@Override
    public Field createField(Item item, Object propertyId,
            Component uiContext) {
		
		Field f = null;
		
		if ("name".equals(propertyId)) {
			f = new TextField("Name");
			f.setWidth("400px");
			((TextField)f).setNullRepresentation("");
			((TextField)f).setInputPrompt("Please enter name");
			
			
		} else if ("description".equals(propertyId)) {
			f = new RichTextArea("Description");
			f.setWidth("400px");
			((RichTextArea)f).setNullRepresentation("");
		}
		
		return f;
	}
}
