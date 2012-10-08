package org.kitchenoffice.webapp.ui.form;

import com.vaadin.data.Item;
import com.vaadin.ui.Component;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.TextField;

public class RecipeFormFieldFactory extends DefaultFieldFactory{

	@Override
    public Field createField(Item item, Object propertyId,
            Component uiContext) {
		
		Field f = null;
		
		if ("name".equals(propertyId)) {
			f = new TextField("Name");
		} else if ("description".equals(propertyId)) {
			f = new TextField("Description");
		}
		
		return f;
	}
}
