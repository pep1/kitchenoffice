package com.gentics.kitchenoffice.webapp;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.DiscoveryNavigator;

import com.gentics.kitchenoffice.webapp.view.RecipeView;
import com.gentics.kitchenoffice.webapp.view.layout.MainLayout;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.Navigator.SimpleViewDisplay;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;

/**
 * The Application's "main" class
 */
@SuppressWarnings("serial")
@Component
@Scope("request")
@Theme("runo")
@Title("KitchenOffice WebApp")
public class WebAppUI extends UI
{
	
	@Autowired
    private transient ApplicationContext applicationContext;
	
	@Autowired
	private MainLayout layout;
	
	private static Logger log = Logger.getLogger(WebAppUI.class);
	
	
	MainLayout main = new MainLayout();

    @Override
    protected void init(VaadinRequest request) {
        
    	log.debug("initializing WebApp instance");
    	
        setContent(layout);
        
        //SimpleViewDisplay display = new SimpleViewDisplay();
        //setContent(display);
        setSizeFull();

        DiscoveryNavigator navigator = new DiscoveryNavigator(applicationContext, UI.getCurrent(), layout, "com.gentics.kitchenoffice.webapp.view");

        // Navigate to view
        navigator.navigateTo(RecipeView.NAME);
    	
    }

}
