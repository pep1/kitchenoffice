package com.gentics.kitchenoffice.webapp.view;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.gentics.kitchenoffice.service.KitchenOfficeUserService;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Notification;


@Component
@Scope("prototype")
public class SecurityViewChangeListener implements ViewChangeListener{
	
	private static Logger log = Logger.getLogger(SecurityViewChangeListener.class);
	
	@Autowired
	KitchenOfficeUserService userService;

	@Override
	public boolean beforeViewChange(ViewChangeEvent event) {
		
		if (event.getNewView() instanceof KitchenOfficeViewInterface) {
			
			boolean hasViewRights = false;
			
			long start = System.currentTimeMillis();
			
			log.debug("entering a security view with view role: " + ((KitchenOfficeViewInterface)event.getNewView()).getViewRole());
			// check if the logged in user is in the view role of this view
			if(userService.hasRole(((KitchenOfficeViewInterface)event.getNewView()).getViewRole())) {
				
				// user has the view role
				log.debug("User is in specified view Role");
	            hasViewRights = true;
			} 
			
			if(!hasViewRights) {
				
				log.warn("User is not in specified view Role");
				// Redirect to login here, but for now:
	            Notification.show("Permission denied",
	                    Notification.Type.ERROR_MESSAGE);
			}
			
			log.debug("check view rights took " + (System.currentTimeMillis() - start) + " ms");
			
			return hasViewRights;
            

        } else {
            return true;
        }
	}

	@Override
	public void afterViewChange(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}

}
