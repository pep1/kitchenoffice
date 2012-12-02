package com.gentics.kitchenoffice.webapp.view.form.field;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.vaadin.mvp.uibinder.IUiBindable;
import com.gentics.kitchenoffice.data.Incredient;
import com.gentics.kitchenoffice.webapp.view.form.field.ui.IngredientFieldSlotUi;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;



@org.springframework.stereotype.Component("IngredientFieldSlot")
@Scope("prototype")
public class IngredientFieldSlot extends CustomField<Incredient> implements IUiBindable{
	
	private final Incredient in;
	
	
	@Autowired
	IngredientFieldSlotUi comp;
	
	public IngredientFieldSlot(Incredient in) {
		this.in = in;
	}
	
	
	@Override
	protected Component initContent() {
		
		comp.setIngredient(in);
		
		return comp;
	}
	
	@Override
	public Class<? extends Incredient> getType() {
		return Incredient.class;
	}
	
	public void setReadOnly(boolean readOnly) {
		comp.setReadOnly(readOnly);
	}
	
	public IngredientFieldSlotUi getComponent(){
		return comp;
	}
	
	public Incredient getIngredient() {
		return in;
	}
	
	
}
