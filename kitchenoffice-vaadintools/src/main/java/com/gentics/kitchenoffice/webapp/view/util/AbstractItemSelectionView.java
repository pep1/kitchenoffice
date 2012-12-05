package com.gentics.kitchenoffice.webapp.view.util;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.vaadin.mvp.uibinder.annotation.UiField;

import com.gentics.kitchenoffice.data.AbstractPersistable;
import com.gentics.kitchenoffice.webapp.container.SpringDataBeanItemContainer;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.ui.Table;

@Component
@Scope("prototype")
public abstract class AbstractItemSelectionView<A extends AbstractPersistable> extends KitchenOfficeView {
	
	private static Logger log = Logger.getLogger(AbstractItemSelectionView.class);
	
	protected Table table = new Table();
	
	@Autowired
	private ApplicationContext context;

	protected SpringDataBeanItemContainer<A> container;
	
	private Class<? extends SpringDataBeanItemContainer<A>> containerClazz;
	
	public AbstractItemSelectionView(Class<? extends SpringDataBeanItemContainer<A>> contClazz) {

		this.containerClazz = contClazz;
	}
	
	@PostConstruct
	public void initialize() {
		
		// get right repository bean
		container = context.getBean(containerClazz);
	}

	public void enter(ViewChangeEvent event) {

		log.debug("entering " + this.getClass().getSimpleName());

		A toSelect = null;

		// check if we can select a recipe specified by id in the parameters
		if (event.getParameters() != null) {

			String[] msgs = event.getParameters().split("/");

			if (msgs.length == 1 && msgs[0] instanceof String
					&& !((String) msgs[0]).isEmpty()) {
				try {
					toSelect = container.getItemById(Long.valueOf(msgs[0]));
				} catch (NumberFormatException e) {
					// no parseable id was given as parameter
					// set Uri Fragment back to selected item
					if(table.getValue() instanceof AbstractPersistable) {
						setURIFragmentByItem((AbstractPersistable)table.getValue(), false);
						return;
					}
				}
			}
		}

		// if there is no recipe specified by parameter, select the first item
		// in container and set the URI
		if (toSelect == null) {
			if (container.size() > 0) {
				table.select(container.firstItemId());
			} 
		}

		if (toSelect != null) {
			table.select(toSelect);
		}

	}
	
	public void setURIFragmentByItem(AbstractPersistable item, Boolean refresh) {
		
		if(Page.getCurrent() != null) {
			
			// set view to this view
			String uriFragment = "!" + this.getName();
			
			if(item != null && item.getId() != null) {
				// set display to that item
				uriFragment += "/" + item.getId();
			}
			
			Page.getCurrent().setUriFragment(uriFragment, refresh);
		}
	}
}
