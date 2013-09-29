/*
 * 
 */
package com.gentics.kitchenoffice.webservice;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import com.gentics.kitchenoffice.data.user.User;
import com.gentics.kitchenoffice.service.UserService;

/**
 * The Class UserWebService.
 * 
 * Provides user CRUD functionality.
 */
@Component
@Scope("singleton")
@Path("/users")
public class UserWebService {
	
	/** The log. */
	private static Logger log = Logger.getLogger(UserWebService.class);
	
	/** The user service. */
	@Autowired
	private UserService userService;
	
	/**
	 * Gets the user object of logged in user.
	 *
	 * @return the me
	 */
	@GET
	@PreAuthorize("hasRole('ROLE_USER')")
	@Path("/me")
	@Produces(MediaType.APPLICATION_JSON)
	public User getMe(){
		log.debug("Calling user getMe");
		return userService.getUser();
	}
	
	/**
	 * Gets all users.
	 *
	 * @param page the page
	 * @param size the size
	 * @return the list
	 */
	@GET
	@PreAuthorize("hasRole('ROLE_USER')")
	@Produces(MediaType.APPLICATION_JSON)
	public List<User> findAll(@QueryParam("page") Integer page, @QueryParam("size") Integer size) {
		log.debug("Calling user findAll");

		if (page == null) {
			page = 0;
		}
		if (size == null) {
			size = 25;
		}

		return userService.findAll(new PageRequest(page, size));
	}
	
}
