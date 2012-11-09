package com.gentics.kitchenoffice.webapp.view;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.VaadinView;

import com.gentics.kitchenoffice.service.KitchenOfficeUserService;
import com.gentics.kitchenoffice.webapp.view.layout.HomeLayout;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;


@Component
@Scope("prototype")
@VaadinView(value = HomeView.NAME, cached = true)
@MenuEntrySortOrder(0)
public class HomeView extends HomeLayout implements KitchenOfficeView {
	
	private static Logger log = Logger.getLogger(HomeView.class);
	
	private static final String VIEW_ROLE = KitchenOfficeUserService.ROLE_USER_NAME;
	
	public static final String NAME = "home";
	
	
	@PostConstruct
	public void initialize() {

		log.debug("initializing Home View");

	}


	@Override
	public void enter(ViewChangeEvent event) {
		
	}

	@Override
	public String getViewRole() {
		return VIEW_ROLE;
	}

}
