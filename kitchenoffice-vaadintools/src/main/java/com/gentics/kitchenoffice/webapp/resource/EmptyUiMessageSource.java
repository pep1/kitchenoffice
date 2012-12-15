package com.gentics.kitchenoffice.webapp.resource;

import java.util.Locale;

import org.vaadin.mvp.uibinder.IUiMessageSource;


/**
 * An empty default UI message source returning just the input key.
 * 
 * @author tam
 */
public class EmptyUiMessageSource implements IUiMessageSource {

  public String getMessage(String key, Locale locale) {
    return key + "_" + locale;
  }

  public String getMessage(String key, Object[] args, Locale locale) {
    return key + "_" + locale;
  }

}
