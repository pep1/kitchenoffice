package com.gentics.kitchenoffice.webservice;

import java.util.List;

import javax.annotation.PostConstruct;
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

import com.gentics.kitchenoffice.data.event.Location;
import com.gentics.kitchenoffice.service.KitchenOfficeUserService;
import com.gentics.kitchenoffice.service.LocationService;

@Component
@Scope("singleton")
@Path("/locations")
public class LocationWebService {

	private static Logger log = Logger.getLogger(LocationWebService.class);

	@Autowired
	private KitchenOfficeUserService userService;

	@Autowired
	private LocationService locationService;

	@PostConstruct
	public void initialize() {
		log.debug("Initializing " + this.getClass().getSimpleName()
				+ " instance ...");

	}

	@GET
	@PreAuthorize("hasRole('ROLE_USER')")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Location> getLocations(@QueryParam("page") Integer page,
			@QueryParam("size") Integer size) {

		log.debug("calling getEvents");

		if (page == null) {
			page = 0;
		}
		if (size == null) {
			size = 25;
		}

		return locationService.getLocations(new PageRequest(page, size)).getContent();
	}

	@GET
	@PreAuthorize("hasRole('ROLE_USER')")
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/lastused")
	public List<Location> getUserLastLocations(@QueryParam("page") Integer page,
			@QueryParam("size") Integer size) {

		log.debug("calling getEvents");

		if (page == null) {
			page = 0;
		}
		if (size == null) {
			size = 25;
		}

		return locationService.getLastUsedLocations(new PageRequest(page, size), null).getContent();
	}
	
}
