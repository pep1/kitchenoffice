/*
 * 
 */
package com.pep1.kitchenoffice.webservice;

import com.pep1.kitchenoffice.data.event.Location;
import com.pep1.kitchenoffice.service.LocationService;
import com.pep1.kitchenoffice.service.UserService;
import com.pep1.kitchenoffice.webservice.filter.CacheAnnotations;
import org.neo4j.graphdb.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;
import org.springframework.util.NumberUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.util.List;

/**
 * The Class LocationWebService.
 * <p>
 * Provides location CRUD functionality.
 */
@RestController
@Scope("singleton")
@RequestMapping("/api/v1/locations")
@CacheAnnotations.NoCache
public class LocationWebService {

    /**
     * The log.
     */
    private static Logger log = LoggerFactory.getLogger(LocationWebService.class);

    /**
     * The user service.
     */
    @Autowired
    private UserService userService;

    /**
     * The location service.
     */
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
     * @param search the search
     * @return the locations
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(method = RequestMethod.GET)
    public List<Location> getLocations(Pageable pageable, @RequestParam("search") String search) {
        log.debug("calling getLocations");
        return locationService.findByNameLike(pageable, search).getContent();
    }

    /**
     * Gets the location.
     *
     * @param id the id
     * @return the location
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Location getLocation(@PathVariable("id") String id) {
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
     * @param id the id
     * @return the location
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/{id}/subscribe", method = RequestMethod.GET)
    public Location subscribeLocation(@PathVariable("id") String id) {
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
     * @param id the id
     * @return the location
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/{id}/unsubscribe", method = RequestMethod.GET)
    public Location unSubscribeLocation(@PathVariable("id") String id) {
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
     * @param search the search
     * @return the user last locations
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/lastused", method = RequestMethod.GET)
    public List<Location> getUserLastLocations(Pageable pageable, @RequestParam("search") String search) {
        log.debug("calling getLastUsedLocations");
        return locationService.getLastUsedLocations(pageable, null, search).getContent();
    }

    /**
     * Creates or updates a location.
     *
     * @param location the location
     * @return the location
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(method = RequestMethod.POST)
    public Location createOrUpdateLocation(@Valid Location location) {
        log.debug("calling createLocation");

        Assert.notNull(location);
        // TODO: Validation
        locationService.saveLocation(location);

        return location;
    }

}
