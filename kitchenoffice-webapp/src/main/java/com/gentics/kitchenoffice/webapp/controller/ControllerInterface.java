package com.gentics.kitchenoffice.webapp.controller;

import java.io.Serializable;

import com.gentics.kitchenoffice.data.AbstractPersistable;
import com.gentics.kitchenoffice.webapp.container.SpringDataBeanItemContainer;

public interface ControllerInterface extends Serializable {

	public SpringDataBeanItemContainer<? extends AbstractPersistable> getContainer();
}
