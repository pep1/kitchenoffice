package org.vaadin.mvp.presenter;

import java.util.Locale;

import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import org.vaadin.mvp.eventbus.EventBus;
import org.vaadin.mvp.uibinder.IUiMessageSource;

/**
 * Abstract base class for presenters.
 * 
 * @author tam
 *
 * @param <V> Type of the view for this presenter
 * @param <E> Type of the event bus used by the presenter
 */
public abstract class BasePresenter<V, E extends EventBus> implements IPresenter<V, E> {

  private boolean bound = false;

  protected E eventBus = null;
  protected V view = null;

  /** Optional reference to the Application and MessageSource */
  protected IUiMessageSource messageSource;  
  
  public void setEventBus(E eventBus) {
    this.eventBus = eventBus;
  }

  public E getEventBus() {
    return this.eventBus;
  }

  public void setView(V view) {
    this.view = view;
  }

  public V getView() {
    return this.view;
  }

  public void bind() {
    // override in implementation
  }

  public void bindIfNeeded() {
    if (!bound) {
      this.bind();
      this.bound = true;
    }
  }

  public IUiMessageSource getMessageSource() {
    return messageSource;
  }

  public void setMessageSource(IUiMessageSource messageSource) {
    this.messageSource = messageSource;
  }


  /**
   * Returns the message for argument <code>key</code> or a default indicating
   * message is missing.
   * 
   * @param key
   *          Message key
   * @return Message translation in current locale
   */  
  public String getMessage(String key, Locale locale) {
    return getMessage(key, locale, (Object[]) null);
  }

  /**
   * Returns the message for argument <code>key</code> parameterized with
   * <code>args</code> or a default indicating message is missing.
   * 
   * @param key
   *          Message key
   * @param args
   *          Message parameters
   * @return Message translation in current locale
   */
  public String getMessage(String key, Locale locale, Object... args) {
    return messageSource.getMessage(key, args, locale);
  }
  
  public void showNotification(String caption) {
    this.showNotification(caption,null,null);
  }
  
  public void showNotification(String caption, String description, Notification.Type type) {
    Notification.Type theType = type!=null?type : Notification.Type.HUMANIZED_MESSAGE;
    Notification notification = new Notification(caption, description, theType);
    notification.setHtmlContentAllowed(true);

    notification.show(UI.getCurrent().getPage());
  }  
}
