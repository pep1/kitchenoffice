package com.gentics.kitchenoffice.webapp.view;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.vaadin.mvp.uibinder.IUiBindable;
import org.vaadin.mvp.uibinder.IUiInitializable;
import org.vaadin.mvp.uibinder.annotation.UiField;

import ru.xpoft.vaadin.VaadinView;

import com.gentics.kitchenoffice.data.event.LocaleResourceName;
import com.gentics.kitchenoffice.resourcemessage.ResourceMessage;
import com.gentics.kitchenoffice.service.EventService;
import com.gentics.kitchenoffice.service.KitchenOfficeUserService;
import com.gentics.kitchenoffice.webapp.resource.ResourceBundleUiMessageSource;
import com.gentics.kitchenoffice.webapp.view.util.KitchenOfficeView;
import com.gentics.kitchenoffice.webapp.view.util.MenuEntrySortOrder;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextField;

@Component
@Scope("prototype")
@VaadinView(value = ProposalView.NAME, cached = true)
@MenuEntrySortOrder(2)
public class ProposalView extends KitchenOfficeView implements IUiBindable, IUiInitializable, ValueChangeListener{

	private static Logger log = Logger.getLogger(ProposalView.class);
	
	private static final String VIEW_ROLE = KitchenOfficeUserService.ROLE_USER_NAME;
	
	public static final String NAME = "proposal";
	
	@UiField
	private HorizontalLayout typeDateSelection;
	
	@UiField
	private ComboBox eventTypeSelect;
	
	@UiField
	private PopupDateField eventDateSelection;
	
	@UiField
	private TextField eventMaxPersonsInput;
	
	
	@Autowired
	private EventService eventservice;
	
	@Autowired
	private ResourceBundleUiMessageSource ms;
	
	public ProposalView(){
		
	}
	
	@PostConstruct
	public void initialize() {
		
		
	}
	
	@Override
	public void init() {
		
		eventMaxPersonsInput.setConverter(Integer.class);
		
		eventDateSelection.setValue(new Date());
		eventDateSelection.setDateFormat("yyyy-MM-dd hh:mm");
		eventDateSelection.setResolution(Resolution.MINUTE);
		eventDateSelection.setShowISOWeekNumbers(true);
		
		//BeanItemContainer<String> eventContainer = new BeanItemContainer<String>(String.class);
		
		eventTypeSelect.setImmediate(true);
		for(Class<? extends com.gentics.kitchenoffice.data.event.Event> event  : eventservice.getAvailableEvents()) {
			LocaleResourceName annotation = event.getAnnotation(LocaleResourceName.class);
			String resourceKey = annotation.value();
			
			eventTypeSelect.addItem(ms.getMessage(resourceKey, VaadinSession.getCurrent().getLocale()));
			eventTypeSelect.select(ms.getMessage(resourceKey, VaadinSession.getCurrent().getLocale()));
		}
		
		eventTypeSelect.setConverter(Class.class);
		eventTypeSelect.setNullSelectionAllowed(false);
		eventTypeSelect.setInvalidAllowed(false);
		eventTypeSelect.setTextInputAllowed(false);
		eventTypeSelect.addValueChangeListener(this);
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

	@Override
	public void valueChange(ValueChangeEvent event) {
		log.debug("Selected event: " + eventTypeSelect.getConvertedValue() + " " + eventTypeSelect.getConvertedValue().getClass());
	}

}
