package com.gentics.kitchenoffice.webapp.container.util;

import java.io.Serializable;

public interface ItemChangedListener extends Serializable {
  public void itemChanged(ItemChangedEvent event);
}
