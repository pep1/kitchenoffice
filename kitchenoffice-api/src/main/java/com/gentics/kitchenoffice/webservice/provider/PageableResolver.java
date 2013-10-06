package com.gentics.kitchenoffice.webservice.provider;

import java.lang.reflect.Type;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.core.spi.component.ComponentContext;
import com.sun.jersey.core.spi.component.ComponentScope;
import com.sun.jersey.server.impl.inject.AbstractHttpContextInjectable;
import com.sun.jersey.spi.inject.Injectable;
import com.sun.jersey.spi.inject.InjectableProvider;

@Provider
// http://blog.broc.seib.net/2013/01/jersey-provider-example.html
public class PageableResolver extends AbstractHttpContextInjectable<Pageable> implements
		InjectableProvider<Context, Type> {

	public final String PAGE_NUMBER_PARAM = "page";

	public final String PAGE_SIZE_PARAM = "limit";

	public final String PAGE_SORT_PROPERTY_PARAM = "orderby";

	public final String PAGE_SORT_DIRECTION_PARAM = "dir";

	public final int DEFAULT_PAGE_NUMBER = 0;

	public final int DEFAULT_PAGE_SIZE = 25;

	@Override
	public ComponentScope getScope() {
		return ComponentScope.PerRequest;
	}

	@Override
	public Injectable<Pageable> getInjectable(ComponentContext ic, Context a, Type c) {
		if (c.equals(Pageable.class)) {
			return this;
		}
		return null;
	}

	@Override
	public Pageable getValue(HttpContext c) {

		String sortBy = stringParam(c, PAGE_SORT_PROPERTY_PARAM, null);
		Direction dir = Direction.fromStringOrNull(stringParam(c, PAGE_SORT_DIRECTION_PARAM, Direction.ASC.toString()));

		if (sortBy != null && dir != null) {
			return new PageRequest(intParam(c, PAGE_NUMBER_PARAM, DEFAULT_PAGE_NUMBER), intParam(c, PAGE_SIZE_PARAM,
					DEFAULT_PAGE_SIZE), new Sort(new Order(dir, sortBy)));
		} else {
			return new PageRequest(intParam(c, PAGE_NUMBER_PARAM, DEFAULT_PAGE_NUMBER), intParam(c, PAGE_SIZE_PARAM,
					DEFAULT_PAGE_SIZE));
		}
	}

	private int intParam(HttpContext c, String name, int defaultValue) {
		MultivaluedMap<String, String> parms = c.getRequest().getQueryParameters();
		if (parms.containsKey(name)) {
			return new Integer(parms.getFirst(name));
		} else {
			return defaultValue;
		}
	}

	private String stringParam(HttpContext c, String name, String defaultValue) {
		MultivaluedMap<String, String> parms = c.getRequest().getQueryParameters();
		if (parms.containsKey(name)) {
			return new String(parms.getFirst(name));
		} else {
			return defaultValue;
		}
	}
}
