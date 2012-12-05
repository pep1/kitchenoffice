package com.gentics.kitchenoffice.webapp.view;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.vaadin.mvp.uibinder.IUiBindable;

import ru.xpoft.vaadin.VaadinView;

import com.gentics.kitchenoffice.service.KitchenOfficeUserService;
import com.gentics.kitchenoffice.webapp.view.util.KitchenOfficeView;
import com.gentics.kitchenoffice.webapp.view.util.KitchenOfficeViewInterface;
import com.gentics.kitchenoffice.webapp.view.util.MenuEntrySortOrder;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

@Component
@Scope("prototype")
@VaadinView(value = ProposalView.NAME, cached = true)
@MenuEntrySortOrder(2)
public class ProposalView extends KitchenOfficeView implements IUiBindable{

	private static Logger log = Logger.getLogger(ProposalView.class);
	
	private static final String VIEW_ROLE = KitchenOfficeUserService.ROLE_USER_NAME;
	
	public static final String NAME = "proposal";
	
	public ProposalView(){
		
	}
	
	@PostConstruct
	public void initialize() {
		
	}

	@Override
	public void enter(ViewChangeEvent event) {
		
	}

	@Override
	public String getViewRole() {
		return VIEW_ROLE;
	}

	@Override
	public String getName() {
		return NAME;
	}

}
