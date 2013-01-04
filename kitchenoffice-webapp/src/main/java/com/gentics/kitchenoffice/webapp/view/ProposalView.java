package com.gentics.kitchenoffice.webapp.view;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.vaadin.mvp.uibinder.IUiBindable;
import org.vaadin.mvp.uibinder.IUiInitializable;
import org.vaadin.mvp.uibinder.annotation.UiField;

import ru.xpoft.vaadin.VaadinView;

import com.gentics.kitchenoffice.data.event.CookEvent;
import com.gentics.kitchenoffice.data.event.EatOutEvent;
import com.gentics.kitchenoffice.data.event.LocaleResourceName;
import com.gentics.kitchenoffice.data.event.OrderEvent;
import com.gentics.kitchenoffice.data.event.PathName;
import com.gentics.kitchenoffice.service.EventService;
import com.gentics.kitchenoffice.service.KitchenOfficeUserService;
import com.gentics.kitchenoffice.webapp.controller.EventController;
import com.gentics.kitchenoffice.webapp.resource.ResourceBundleUiMessageSource;
import com.gentics.kitchenoffice.webapp.view.component.EventSelect;
import com.gentics.kitchenoffice.webapp.view.util.KitchenOfficeView;
import com.gentics.kitchenoffice.webapp.view.util.MenuEntrySortOrder;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextField;

@Component
@Scope("prototype")
@VaadinView(value = ProposalView.NAME, cached = true)
@MenuEntrySortOrder(2)
public class ProposalView extends KitchenOfficeView implements IUiBindable,
		IUiInitializable, ValueChangeListener {

	private static Logger log = Logger.getLogger(ProposalView.class);

	private static final String VIEW_ROLE = KitchenOfficeUserService.ROLE_USER_NAME;

	public static final String NAME = "proposal";

	@UiField
	private HorizontalLayout typeDateSelection;

	@UiField
	private CssLayout selectContainer;

	@UiField
	private ComboBox eventTypeSelect;

	@UiField
	private PopupDateField eventDateSelection;

	@UiField
	private TextField eventMaxPersonsInput;

	@Autowired
	private EventController controller;

	@Autowired
	private ResourceBundleUiMessageSource ms;

	private EventSelect select;

	public ProposalView() {

	}

	@PostConstruct
	public void initialize() {

	}

	@Override
	public void init() {

		log.debug("initializing view " + this.toString());

		setSizeFull();
		setSpacing(true);
		setExpandRatio(selectContainer, 1.0f);

		eventMaxPersonsInput.setConverter(Integer.class);

		eventDateSelection.setValue(new Date());
		eventDateSelection.setDateFormat("yyyy-MM-dd hh:mm");
		eventDateSelection.setResolution(Resolution.MINUTE);
		eventDateSelection.setShowISOWeekNumbers(true);

		eventTypeSelect.setImmediate(true);
		for (Class<? extends com.gentics.kitchenoffice.data.event.Event> event : controller
				.getService().getAvailableEvents()) {
			LocaleResourceName annotation = event
					.getAnnotation(LocaleResourceName.class);
			String resourceKey = annotation.value();

			eventTypeSelect.addItem(ms.getMessage(resourceKey, VaadinSession
					.getCurrent().getLocale()));
			
		}

		eventTypeSelect.setConverter(Class.class);
		eventTypeSelect.setInvalidAllowed(false);
		eventTypeSelect.setTextInputAllowed(false);
		eventTypeSelect.addValueChangeListener(this);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		log.debug("entering " + this.getClass().getSimpleName());
		
		selectContainer.removeAllComponents();

		Class<? extends com.gentics.kitchenoffice.data.event.Event> toSelect = null;

		// check if we can select a recipe specified by id in the parameters
		if (event.getParameters() != null) {

			String[] msgs = event.getParameters().split("/");

			if (msgs.length == 1 && msgs[0] instanceof String
					&& !((String) msgs[0]).isEmpty()) {

				String eventType = (String) msgs[0];

				for (Class<? extends com.gentics.kitchenoffice.data.event.Event> availableEvent : controller
						.getService().getAvailableEvents()) {
					PathName annotation = availableEvent
							.getAnnotation(PathName.class);
					String path = annotation.value();
					if (path != null && path.equals(eventType)) {
						toSelect = availableEvent;
						break;
					}
				}
			}
		}

		// if no toSelect could be found default Uri Fragment
		if (toSelect == null) {
			if (Page.getCurrent() != null) {
				// set view to this view
				String uriFragment = "!" + this.getName();
				Page.getCurrent().setUriFragment(uriFragment, false);
				eventTypeSelect.select(eventTypeSelect.getNullSelectionItemId());
			}

			return;
		}

		if (toSelect != null) {
			LocaleResourceName annotation = toSelect
					.getAnnotation(LocaleResourceName.class);
			String resourceKey = annotation.value();
			
			eventTypeSelect.select(eventTypeSelect.getNullSelectionItemId());
			
			eventTypeSelect.select(ms.getMessage(resourceKey, VaadinSession
					.getCurrent().getLocale()));
		}
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
		
		if(eventTypeSelect.getConvertedValue() == null) {
			return;
		}
		
		log.debug("Selected event: " + eventTypeSelect.getConvertedValue()
				+ " " + eventTypeSelect.getConvertedValue().getClass());

		if (Page.getCurrent() != null) {
			
			// set view to this view
			String uriFragment = "!" + this.getName();

			PathName annotation = (PathName) ((Class) eventTypeSelect
					.getConvertedValue()).getAnnotation(PathName.class);
			String pathName = annotation.value();

			if (pathName != null) {
				uriFragment += "/" + pathName;
			}

			Page.getCurrent().setUriFragment(uriFragment, false);

			selectContainer.removeAllComponents();
			try {
				
				select = null;
				select = controller
				.getNewEventSelect((Class<? extends com.gentics.kitchenoffice.data.event.Event>) eventTypeSelect
						.getConvertedValue());
				
				selectContainer.addComponent(select);
				select.setSizeFull();
				
			} catch(NoSuchBeanDefinitionException e ) {
				Notification.show("No Select found for event type " + eventTypeSelect.getConvertedValue(), Notification.Type.WARNING_MESSAGE);
			} catch (BeansException e) {
				Notification.show("No Select found for event type " + eventTypeSelect.getConvertedValue(), Notification.Type.WARNING_MESSAGE);
			} catch (ClassNotFoundException e) {
				Notification.show("No Select found for event type " + eventTypeSelect.getConvertedValue(), Notification.Type.WARNING_MESSAGE);
			}
		}
	}

}
