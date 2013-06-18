package com.gentics.kitchenoffice.service;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.gentics.kitchenoffice.data.event.Location;
import com.gentics.kitchenoffice.data.user.User;
import com.gentics.kitchenoffice.repository.LocationRepository;


@Service
public class LocationService {
	
	private static Logger log = Logger
			.getLogger(LocationService.class);
	
	@Autowired
	LocationRepository locationRepository;
	
	@PostConstruct
	public void initialize() {
		log.debug("initializing " + this.getClass().getSimpleName() + " instance ... ");
	}
	
	public Page<Location> getLocations(Pageable pageable) {
		return locationRepository.findAll(pageable);
	}
	
	public Page<Location> getLastUsedLocations(Pageable pageable, User user) {
		if(user == null) {
			return locationRepository.getLastUsedLocations(pageable);
		} else {
			return locationRepository.getLastUsedLocations(user, pageable);
		}
	}
}
