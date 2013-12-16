/*
 * 
 */
package com.gentics.kitchenoffice.webservice;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.NumberUtils;

import com.gentics.kitchenoffice.data.event.Location;
import com.gentics.kitchenoffice.service.LocationService;
import com.gentics.kitchenoffice.service.UserService;
import com.gentics.kitchenoffice.webservice.filter.CacheAnnotations.NoCache;
import com.sun.jersey.api.NotFoundException;

/**
 * The Class LocationWebService.
 * 
 * Provides location CRUD functionality.
 */
@Component
@Scope("singleton")
@Path("/locations")
@NoCache
public class LocationWebService {

	/** The log. */
	private static Logger log = Logger.getLogger(LocationWebService.class);

	/** The user service. */
	@Autowired
	private UserService userService;

	/** The location service. */
	@Autowired
	private LocationService locationService;

	/**
	 * Initialize.
	 */
	@PostConstruct
	public void initialize() {
		log.debug("Initializing " + this.getClass().getSimpleName() + " instance ...");
	}

	/**
	 * Gets the locations.
	 * 
	 * @param page
	 *            the page
	 * @param size
	 *            the size
	 * @param search
	 *            the search
	 * @return the locations
	 */
	@GET
	@PreAuthorize("hasRole('ROLE_USER')")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Location> getLocations(@Context Pageable pageable, @QueryParam("search") String search) {
		log.debug("calling getLocations");
		return locationService.findByNameLike(pageable, search).getContent();
	}

	/**
	 * Gets the location.
	 * 
	 * @param id
	 *            the id
	 * @return the location
	 */
	@GET
	@Path("/{id}")
	@PreAuthorize("hasRole('ROLE_USER')")
	@Produces(MediaType.APPLICATION_JSON)
	public Location getLocation(@PathParam("id") String id) {
		Assert.notNull(id);
		Long parsedId = NumberUtils.parseNumber(id, Long.class);
		Assert.notNull(parsedId, "Id could not be parsed");

		Location location = locationService.getLocationById(parsedId);

		if (location == null) {
			throw new NotFoundException("Sorry, there is no location with id " + parsedId);
		}

		return location;
	}
	
	/**
	 * Subscribes the logged in user to this location.
	 * 
	 * @param id
	 *            the id
	 * @return the location
	 */
	@GET
	@Path("/{id}/subscribe")
	@PreAuthorize("hasRole('ROLE_USER')")
	@Produces(MediaType.APPLICATION_JSON)
	public Location subscribeLocation(@PathParam("id") String id) {
		Assert.notNull(id);
		Long parsedId = NumberUtils.parseNumber(id, Long.class);
		Assert.notNull(parsedId, "Id could not be parsed");

		Location location = locationService.getLocationById(parsedId);

		if (location == null) {
			throw new NotFoundException("Sorry, there is no location with id " + parsedId);
		}
		
		return locationService.subscribeToLocation(location);
	}
	
	/**
	 * Subscribes the logged in user to this location.
	 * 
	 * @param id
	 *            the id
	 * @return the location
	 */
	@GET
	@Path("/{id}/unsubscribe")
	@PreAuthorize("hasRole('ROLE_USER')")
	@Produces(MediaType.APPLICATION_JSON)
	public Location unSubscribeLocation(@PathParam("id") String id) {
		Assert.notNull(id);
		Long parsedId = NumberUtils.parseNumber(id, Long.class);
		Assert.notNull(parsedId, "Id could not be parsed");

		Location location = locationService.getLocationById(parsedId);

		if (location == null) {
			throw new NotFoundException("Sorry, there is no location with id " + parsedId);
		}
		
		return locationService.unSubscribeToLocation(location);
	}

	/**
	 * Gets the user last locations.
	 * 
	 * @param page
	 *            the page
	 * @param size
	 *            the size
	 * @param search
	 *            the search
	 * @return the user last locations
	 */
	@GET
	@PreAuthorize("hasRole('ROLE_USER')")
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/lastused")
	public List<Location> getUserLastLocations(@Context Pageable pageable, @QueryParam("search") String search) {
		log.debug("calling getLastUsedLocations");
		return locationService.getLastUsedLocations(pageable, null, search).getContent();
	}

	/**
	 * Creates or updates a location.
	 * 
	 * @param location
	 *            the location
	 * @return the location
	 */
	@POST
	@PreAuthorize("hasRole('ROLE_USER')")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Location createOrUpdateLocation(@Valid Location location) {

		log.debug("calling createLocation");

		Assert.notNull(location);
		// TODO: Validation
		locationService.saveLocation(location);

		return location;
	}

}
