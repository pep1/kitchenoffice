package com.gentics.kitchenoffice.webservice.filter;

import java.util.Collections;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;

import com.gentics.kitchenoffice.webservice.filter.CacheAnnotations.CacheMaxAge;
import com.gentics.kitchenoffice.webservice.filter.CacheAnnotations.NoCache;
import com.sun.jersey.api.model.AbstractMethod;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import com.sun.jersey.spi.container.ResourceFilter;
import com.sun.jersey.spi.container.ResourceFilterFactory;

public class CacheFilterFactory implements ResourceFilterFactory {

	@Override
	public List<ResourceFilter> create(AbstractMethod am) {
		if (am.isAnnotationPresent(CacheMaxAge.class)) {
			CacheMaxAge maxAge = am.getAnnotation(CacheMaxAge.class);
			return newCacheFilter("max-age: " + maxAge.unit().toSeconds(maxAge.time()));
		} else if (am.isAnnotationPresent(NoCache.class)) {
			return newCacheFilter("no-cache");
		} else if (am.getResource().isAnnotationPresent(CacheMaxAge.class)) {
			CacheMaxAge maxAge = am.getClass().getAnnotation(CacheMaxAge.class);
			return newCacheFilter("max-age: " + maxAge.unit().toSeconds(maxAge.time())); 
		} else if (am.getResource().isAnnotationPresent(NoCache.class)){
			return newCacheFilter("no-cache");
		} else {
			return Collections.emptyList();
		}
	}

	private List<ResourceFilter> newCacheFilter(String content) {
		return Collections.<ResourceFilter> singletonList(new CacheResponseFilter(content));
	}

	private static class CacheResponseFilter implements ResourceFilter, ContainerResponseFilter {
		private final String headerValue;

		CacheResponseFilter(String headerValue) {
			this.headerValue = headerValue;
		}

		@Override
		public ContainerRequestFilter getRequestFilter() {
			return null;
		}

		@Override
		public ContainerResponseFilter getResponseFilter() {
			return this;
		}

		@Override
		public ContainerResponse filter(ContainerRequest request, ContainerResponse response) {
			response.getHttpHeaders().putSingle(HttpHeaders.CACHE_CONTROL, headerValue);
			return response;
		}
	}
}