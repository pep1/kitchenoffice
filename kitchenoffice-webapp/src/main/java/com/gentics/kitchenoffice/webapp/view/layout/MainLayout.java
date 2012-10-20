package com.gentics.kitchenoffice.webapp.view.layout;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

@org.springframework.stereotype.Component
@Scope("prototype")
public class MainLayout extends VerticalLayout implements ViewDisplay {
	
	private static Logger log = Logger.getLogger(MainLayout.class);

	private Label header = new Label("<h1>Kitchen Office <span style='color: red; font-size: small;'>alpha</span></h1>", ContentMode.HTML);

	private Panel panel = new Panel();

	public MainLayout() {
		
		setSpacing(true);
		setMargin(true);

		addComponent(header);
		
		panel.setSizeFull();

		addComponent(panel);
		setExpandRatio(panel, 1.0F);
		
		Authentication test = SecurityContextHolder.getContext().getAuthentication();
		
		log.debug("Name: " + test.getName());
		log.debug("Details: " + test.getDetails());
		log.debug("Principal: " + test.getPrincipal());
		log.debug("isAuthenticated: " + test.isAuthenticated());
		log.debug("Credentials: " + test.getCredentials());
		log.debug("Authorities: " + test.getAuthorities());

	}

	public Label getHeader() {
		return header;
	}

	public Panel getPanel() {
		return panel;
	}

	@Override
	public void showView(View view) {
		if (view instanceof Component) {
			
			panel.removeAllComponents();
			panel.addComponent((Component) view);
			
		} else {
			throw new IllegalArgumentException("View is not a component: "
					+ view);
		}
	}

}
