package com.gentics.kitchenoffice.webapp;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.xpoft.vaadin.KitchenOfficeNavigator;

import com.gentics.kitchenoffice.webapp.view.HomeView;
import com.gentics.kitchenoffice.webapp.view.StandardErrorView;
import com.gentics.kitchenoffice.webapp.view.layout.MainLayout;
import com.gentics.kitchenoffice.webapp.view.util.SecurityViewChangeListener;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;

/**
 * The Application's "main" class
 */
@SuppressWarnings("serial")
@Component
@Scope("request")
@Theme("kitchenoffice-webapp-theme")
@Title("KitchenOffice WebApp")
public class WebAppUI extends UI {
	
	@Value("${webapp.templatecache}")
	private Boolean isTemplateCache;

	@Autowired
	private MainLayout layout;

	@Autowired
	private SecurityViewChangeListener viewChangeListener;

	private KitchenOfficeNavigator navigator;

	private static Logger log = Logger.getLogger(WebAppUI.class);

	@Override
	protected void init(VaadinRequest request) {


		log.debug("initializing WebApp instance");
		
		log.debug("Locale is " + this.getLocale().toString());
		log.debug("Template cache: " + isTemplateCache);

		setContent(layout);
		setSizeFull();

		initializeNavigator();

	}

	private void initializeNavigator() {

		navigator = new KitchenOfficeNavigator(this, layout.getPanel(), isTemplateCache);

		navigator.addViewChangeListener(viewChangeListener);

		navigator.setErrorView(StandardErrorView.class);

		//check if there is a URI Fragment set

		if (Page.getCurrent() != null
				&& Page.getCurrent().getFragment() != null
				&& !Page.getCurrent().getFragment().isEmpty()) {
			// Navigate to view specified by fragment
			navigator.navigateTo(Page.getCurrent().getFragment());
		} else {
			// Navigate to standard view
			navigator.navigateTo(HomeView.NAME);
		}
	}

}
