package com.pep1.kitchenoffice.webservice.filter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;
 
public final class CacheAnnotations {
 
	/**
	 * Set the "Max-age" Cache header.
	 * 
	 * @see <a href='http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.9.3'>W3C Header
	 *      Field Definitions</a>
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.TYPE, ElementType.METHOD})
	public @interface CacheMaxAge {
		/**
		 * @return The amount of time to cache this resource.
		 */
		long time();
 
		/**
		 * @return The {@link TimeUnit} for the given {@link #time()}.
		 */
		TimeUnit unit();
	}
 
	/**
	 * Sets the cache header to the value "no cache"
	 * 
	 * @see <a href='http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.9.1'>W3c Header
	 *      Field Definitions</a>
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.TYPE, ElementType.METHOD})
	public @interface NoCache {
 
	}
 
	private CacheAnnotations() {
	}
}