package org.vaadin.mvp.presenter;

import java.util.Locale;

import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import org.vaadin.mvp.eventbus.EventBus;
import org.vaadin.mvp.uibinder.IUiMessageSource;


/**
 * Interface that all presenters should implement.
 * 
 * @author tam
 * 
 * @param <V>
 *          View type
 * @param <E>
 *          Event bus type
 */
public interface IPresenter<V, E extends EventBus> {

  public void setEventBus(E eventBus);

  public E getEventBus();

  public void setView(V view);

  public V getView();

  public void bind();

  public void bindIfNeeded();

  public void setMessageSource(IUiMessageSource messageSource);

  public IUiMessageSource getMessageSource();

  public String getMessage(String key, Locale locale, Object... args);

  public String getMessage(String key, Locale locale);

  public void showNotification(String caption);

  public void showNotification(String caption, String description, Notification.Type type);
}
