package com.gentics.kitchenoffice.webapp.resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.FieldPosition;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.ResourceBundle.Control;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.vaadin.mvp.uibinder.IUiMessageSource;


/**
 * Simple {@link ResourceBundle} based implementation of
 * {@link IUiMessageSource}.
 * 
 * @author tam
 */

@Component
@Scope("singleton")
public class ResourceBundleUiMessageSource implements IUiMessageSource {
	
	protected static final String BUNDLE_EXTENSION = "properties";
	
	protected static final Control UTF8_CONTROL = new UTF8Control();

  /** Map of resource bundles by locale */
  private transient Map<Locale, ResourceBundle> resourceBundles = new HashMap<Locale, ResourceBundle>();

  /** ResourceBundle base name */
  @Value("${i18n.basename}")
  private String baseName;
  
  public ResourceBundleUiMessageSource() {
  }

  /**
   * Constructor; takes a <code>baseName</code> of the message properties files.
   * 
   * @param baseName
   *          base name of message properties, e.g.
   *          <code>messages/Resources</code>
   */
  public ResourceBundleUiMessageSource(String baseName) {
    this.baseName = baseName;
  }

  public String getMessage(String key, Locale locale) {
    ResourceBundle bundle = getBundle(locale);
    if (bundle.containsKey(key)) {
      return bundle.getString(key);
    }
    return "{{ message missing: " + key + "}}";
  }

  public String getMessage(String key, Object[] args, Locale locale) {
    ResourceBundle bundle = getBundle(locale);
    if (bundle.containsKey(key)) {
      String template = bundle.getString(key);
      MessageFormat fmt = new MessageFormat(template, locale);
      StringBuffer messageBuffer = fmt.format(args, new StringBuffer(), new FieldPosition(0));
      return messageBuffer.toString();
    }
    return "{{ message missing: " + key + "}}";
  }

  /**
   * Lookup a bundle for the <code>locale</code>. If a resource bundle has been
   * loaded already, the "cached" instance is returned; otherwise the bundle is
   * loaded.
   * 
   * @param locale
   *          requested locale
   * @return A resource bundle for the locale
   */
  public ResourceBundle getBundle(Locale locale) {
    if (resourceBundles.containsKey(locale)) {
      return resourceBundles.get(locale);
    }
    // construct the resource bundle (synchronized to prevent multiple creation
    // of the same bundle
    synchronized (this) {
      if (resourceBundles.containsKey(locale)) {
        return resourceBundles.get(locale);
      }
      ResourceBundle bundle = ResourceBundle.getBundle(baseName, locale, UTF8_CONTROL);
      resourceBundles.put(locale, bundle);
      return bundle;
    }
  }
  
  protected static class UTF8Control extends Control {
      public ResourceBundle newBundle
          (String baseName, Locale locale, String format, ClassLoader loader, boolean reload)
              throws IllegalAccessException, InstantiationException, IOException
      {
          // The below code is copied from default Control#newBundle() implementation.
          // Only the PropertyResourceBundle line is changed to read the file as UTF-8.
          String bundleName = toBundleName(baseName, locale);
          String resourceName = toResourceName(bundleName, BUNDLE_EXTENSION);
          ResourceBundle bundle = null;
          InputStream stream = null;
          if (reload) {
              URL url = loader.getResource(resourceName);
              if (url != null) {
                  URLConnection connection = url.openConnection();
                  if (connection != null) {
                      connection.setUseCaches(false);
                      stream = connection.getInputStream();
                  }
              }
          } else {
              stream = loader.getResourceAsStream(resourceName);
          }
          if (stream != null) {
              try {
                  bundle = new PropertyResourceBundle(new InputStreamReader(stream, "UTF-8"));
              } finally {
                  stream.close();
              }
          }
          return bundle;
      }
  }

}
