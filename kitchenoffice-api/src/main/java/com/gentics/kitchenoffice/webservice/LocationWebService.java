package com.gentics.kitchenoffice.webservice;

import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.gentics.kitchenoffice.data.event.Location;
import com.gentics.kitchenoffice.service.UserService;
import com.gentics.kitchenoffice.service.LocationService;

@Component
@Scope("singleton")
@Path("/locations")
public class LocationWebService {

	private static Logger log = Logger.getLogger(LocationWebService.class);

	@Autowired
	private UserService userService;

	@Autowired
	private LocationService locationService;

	@PostConstruct
	public void initialize() {
		log.debug("Initializing " + this.getClass().getSimpleName() + " instance ...");

	}

	@GET
	@PreAuthorize("hasRole('ROLE_USER')")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Location> getLocations(@QueryParam("page") Integer page, @QueryParam("size") Integer size, @QueryParam("search") String search) {

		log.debug("calling getLocations");

		if (page == null) {
			page = 0;
		}
		if (size == null) {
			size = 25;
		}

		return locationService.getLocationsByName(new PageRequest(page, size), search).getContent();
	}

	@GET
	@PreAuthorize("hasRole('ROLE_USER')")
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/lastused")
	public List<Location> getUserLastLocations(@QueryParam("page") Integer page, @QueryParam("size") Integer size, @QueryParam("search") String search) {

		log.debug("calling getLastUsedLocations");

		if (page == null) {
			page = 0;
		}
		if (size == null) {
			size = 25;
		}

		return locationService.getLastUsedLocations(new PageRequest(page, size), null, search).getContent();

	}
	
	@POST
	@PreAuthorize("hasRole('ROLE_USER')")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Location createEvent(Location location) {

		log.debug("calling createLocation");

		try {
			Assert.notNull(location);
			// TODO: Validation
			locationService.saveLocation(location);
			
			return location;

		} catch (ConstraintViolationException e) {

			String message = "";
			Iterator<?> iterator = e.getConstraintViolations().iterator();

			while (iterator.hasNext()) {
				ConstraintViolation<?> current = (ConstraintViolation<?>) iterator.next();
				message += current.getPropertyPath().toString() + ": ";
				message += current.getMessage() + " ";
			}

			throw new WebApplicationException(Response.status(Response.Status.NOT_ACCEPTABLE).entity(message).build());
		} catch (IllegalArgumentException e) {
			log.error("Failed to save location", e);
			throw new WebApplicationException(Response.status(Response.Status.NOT_ACCEPTABLE).entity("Failed to save location").build());
		}

	}

}
