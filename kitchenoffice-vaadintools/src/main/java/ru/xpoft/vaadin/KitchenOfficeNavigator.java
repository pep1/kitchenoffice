package ru.xpoft.vaadin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;
import org.vaadin.mvp.uibinder.IUiBindable;
import org.vaadin.mvp.uibinder.UiBinder;
import org.vaadin.mvp.uibinder.UiBinderException;
import org.vaadin.mvp.uibinder.resource.ResourceBundleUiMessageSource;

import com.gentics.kitchenoffice.webapp.view.util.KitchenOfficeView;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.server.Page;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.UI;

public class KitchenOfficeNavigator extends Navigator implements
		ViewCacheContainer {

	protected class ViewCache implements Serializable {
		private final String name;
		private final String beanName;
		private final Class<? extends View> clazz;
		private final boolean cached;

		ViewCache(String name, String beanName, Class<? extends View> clazz,
				boolean cached) {
			this.name = name;
			this.beanName = beanName;
			this.clazz = clazz;
			this.cached = cached;
		}

		public String getName() {
			return name;
		}

		public String getBeanName() {
			return beanName;
		}

		public Class<? extends View> getClazz() {
			return clazz;
		}

		public boolean isCached() {
			return cached;
		}
	}

	private static Logger logger = Logger
			.getLogger(KitchenOfficeNavigator.class);
	protected static final List<ViewCache> views = Collections
			.synchronizedList(new ArrayList<ViewCache>());
	private final Map<String, View> viewScoped = Collections
			.synchronizedMap(new HashMap<String, View>());

	private static ResourceBundleUiMessageSource ms = new ResourceBundleUiMessageSource("i18n/Resources");
	
	private static UiBinder binder = new UiBinder(ms);
	
	private Boolean isTemplateCache;

	public KitchenOfficeNavigator(UI ui, ComponentContainer display, Boolean isTemplateCache) {
		super(ui, display);
		
		this.isTemplateCache =isTemplateCache;

		if (views.isEmpty()) {
			logger.debug("discovery views from spring context");

			long start = Calendar.getInstance().getTimeInMillis();
			String[] beansName = SpringApplicationContext
					.getApplicationContext().getBeanDefinitionNames();
			for (String beanName : beansName) {
				Class beanClass = SpringApplicationContext
						.getApplicationContext().getType(beanName);
				if (beanClass.isAnnotationPresent(VaadinView.class)
						&& View.class.isAssignableFrom(beanClass)) {
					VaadinView vaadinView = (VaadinView) beanClass
							.getAnnotation(VaadinView.class);
					String viewName = vaadinView.value();
					boolean viewCached = vaadinView.cached();

					ViewCache viewCache = new ViewCache(viewName, beanName,
							beanClass, viewCached);
					views.add(viewCache);
				}
			}

			long end = Calendar.getInstance().getTimeInMillis();
			logger.debug("time: " + (end - start) + "ms");
		} else {
			logger.debug("discovery views from cache");
		}

		addCachedBeans();
	}

	public void addBeanView(String viewName, Class<? extends View> viewClass) {
		addBeanView(viewName, viewClass, false);
	}

	/**
	 * Add bean manually
	 * 
	 * @param viewName
	 * @param viewClass
	 * @param cached
	 */
	public void addBeanView(String viewName, Class<? extends View> viewClass,
			boolean cached) {
		// Check parameters
		if (viewName == null || viewClass == null) {
			throw new IllegalArgumentException(
					"view and viewClass must be non-null");
		}

		String[] beanNames = SpringApplicationContext.getApplicationContext()
				.getBeanNamesForType(viewClass);
		if (beanNames.length != 0) {
			throw new IllegalArgumentException(
					"cant't resolve bean name for class :"
							+ viewClass.getName());
		}

		removeView(viewName);
		addBeanView(viewName, beanNames[0], viewClass, cached);
	}

	protected void addCachedBeans() {
		for (ViewCache view : views) {
			addBeanView(view.name, view.beanName, view.clazz, view.cached);
		}
	}

	/**
	 * It's prefer to use BeanName instead of Class Because bean can be wrapper.
	 * Like SpringSecurity Caused by:
	 * org.springframework.beans.factory.BeanNotOfRequiredTypeException: Bean
	 * named 'testView' must be of type [...testView], but was actually of type
	 * [$Proxy26]
	 * 
	 */
	protected void addBeanView(String viewName, String beanName,
			Class<? extends View> viewClass, boolean cached) {
		addProvider(new SpringViewProvider(viewName, beanName, viewClass,
				cached, this));
	}

	@Override
	public void navigateTo(String navigationState) {
		// We can't bind NULL
		if (navigationState == null) {
			navigationState = "";
		}

		// fix Vaadin
		if (navigationState.startsWith("!")) {
			super.navigateTo(navigationState.substring(1));
		} else {
			super.navigateTo(navigationState);
		}
	}

	/**
	 * Better way
	 * 
	 * @param name
	 * @param beanName
	 * @param cached
	 * @return
	 */
	public View getView(String name, String beanName, boolean cached) {
		
		if (cached && isTemplateCache) {
			if (viewScoped.containsKey(name)) {
				return viewScoped.get(name);
			}

			KitchenOfficeView view = (KitchenOfficeView) SpringApplicationContext
					.getApplicationContext().getBean(beanName);

			bindUIToView(view);

			viewScoped.put(name, view);

			return view;
		}
		
		KitchenOfficeView view = (KitchenOfficeView) SpringApplicationContext
				.getApplicationContext().getBean(beanName);
		
		bindUIToView(view);

		return view;
	}
	
	private void bindUIToView(KitchenOfficeView view) {
		
		if (view instanceof IUiBindable) {
			try {
				long start = System.currentTimeMillis();
				// apply UiBinder
				view = binder.bind(view.getClass().getName(), view, Page
						.getCurrent().getWebBrowser().getLocale(), null);

				logger.debug("ui binding took "
						+ (System.currentTimeMillis() - start) + " ms");

			} catch (UiBinderException e) {
				logger.error("something went wrong with the UiBinding...");
				e.printStackTrace();
			}
		}
	}
	
	public static void bindUIToComponent(Component component, Locale locale) {
		
		if (component instanceof IUiBindable) {
			try {
				long start = System.currentTimeMillis();
				// apply UiBinder
				component = binder.bind(component.getClass().getName(), component, locale, null);

				logger.debug("ui binding took "
						+ (System.currentTimeMillis() - start) + " ms");

			} catch (UiBinderException e) {
				logger.error("something went wrong with the UiBinding...");
				e.printStackTrace();
			}
		}
	}
	
}
