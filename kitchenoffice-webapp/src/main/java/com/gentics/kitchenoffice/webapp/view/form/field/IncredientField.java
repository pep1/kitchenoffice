package com.gentics.kitchenoffice.webapp.view.form.field;

import java.util.Set;

import org.springframework.context.annotation.Scope;

import com.gentics.kitchenoffice.data.Incredient;
import com.gentics.kitchenoffice.webapp.view.component.IncredientComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.VerticalLayout;

@org.springframework.stereotype.Component
@Scope("prototype")
public class IncredientField extends CustomField<Set<Incredient>> {

	VerticalLayout layout = new VerticalLayout();

	@Override
	protected Component initContent() {

		layout.setSpacing(true);

		Set<Incredient> set = getValue();

		if (set != null) {
			for (Incredient item : set) {
				layout.addComponent(new IncredientComponent(item.getArticle()
						.getName()));
			}
		}

		return layout;
	}

	@Override
	public Class getType() {
		return Set.class;
	}

}
