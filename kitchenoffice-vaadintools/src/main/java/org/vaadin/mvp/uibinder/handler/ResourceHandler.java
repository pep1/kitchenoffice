package org.vaadin.mvp.uibinder.handler;

import com.vaadin.server.ThemeResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.mvp.uibinder.UiConstraintException;


public class ResourceHandler implements TargetHandler {

  /** Logger */
  private static final Logger logger = LoggerFactory.getLogger(ResourceHandler.class);
  
  private ComponentHandler ch;
  
  public ResourceHandler(ComponentHandler ch) {
    this.ch = ch;
  }

  public String getTargetNamespace() {
    return "urn:org.vaadin.mvp.uibinder.resource";
  }

  public void handleElementOpen(String uri, String name) {
    // ignore
  }

  public void handleElementClose() {
    // ignore
  }

  public void handleAttribute(String name, Object value) throws UiConstraintException {
    logger.info("Create resource : {}", value);
    ThemeResource res = new ThemeResource(value.toString());
    ch.handleAttribute(name, res);
  }

}
