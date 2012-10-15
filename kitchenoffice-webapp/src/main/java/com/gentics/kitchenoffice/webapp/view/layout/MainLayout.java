package com.gentics.kitchenoffice.webapp.view.layout;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;


@Component
@Scope("prototype")
public class MainLayout extends VerticalLayout{
	
	private Label header = new Label("<h1>KitchenOffice</h1>", ContentMode.HTML);
	
	private TabSheet tabSheet = new TabSheet();
	
	
	public MainLayout() {
		
		this.addComponent(header);
		
		this.addComponent(tabSheet);
		this.setExpandRatio(tabSheet, 1.0F);
		
	}


	public Label getHeader() {
		return header;
	}


	public TabSheet getTabSheet() {
		return tabSheet;
	}
	
	
	
}
