package com.gentics.kitchenoffice.service;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.gentics.kitchenoffice.data.Tag;
import com.gentics.kitchenoffice.data.event.Location;
import com.gentics.kitchenoffice.data.user.User;
import com.gentics.kitchenoffice.repository.LocationRepository;


@Service
public class LocationService {
	
	private static Logger log = Logger
			.getLogger(LocationService.class);
	
	@Autowired
	LocationRepository locationRepository;
	
	@Autowired
	TagService tagService;
	
	@PostConstruct
	public void initialize() {
		log.debug("initializing " + this.getClass().getSimpleName() + " instance ... ");
	}
	
	public Page<Location> getLocations(Pageable pageable) {
		return locationRepository.findAll(pageable);
	}
	
	public Page<Location> getLocationsByName(Pageable pageable, String name) {
		if(StringUtils.hasLength(name) && name.length() > 2) {
			return locationRepository.findByNameLike("*" + name + "*", pageable);
		} else {
			return getLocations(pageable);
		}
	}
	
	public Page<Location> getLastUsedLocations(Pageable pageable, User user, String search) {
		if(user == null) {
			if(StringUtils.hasLength(search) && search.length() > 2) {
				return locationRepository.getLastUsedLocations(search, pageable);
			} else {
				return locationRepository.getLastUsedLocations(pageable);
			}
		} else {
			if(StringUtils.hasLength(search) && search.length() > 2) {
				return locationRepository.getLastUsedLocations(user, search, pageable);
			} else {
				return locationRepository.getLastUsedLocations(user, pageable);
			}
		}
	}
	
	public Location saveLocation(Location location) {
		return locationRepository.save(location);
	}
	
	public Location addTagToLocation(Location location, String tagString) {
		Assert.hasText(tagString);
		
		Tag tag = tagService.findByTag(tagString); 
		
		if(tag == null) {
			tag = new Tag();
			tag.setTag(tagString);
			tag.setTimeStamp(new Date());
			
			tag = tagService.save(tag);
		}
		
		if(location.getTags().contains(tag)) {
			throw new IllegalStateException("Location already has this tag: " + tag.getTag());
		}
		
		location.getTags().add(tag);
		locationRepository.save(location);
		
		return location;
	}
	
	public Location removeTagFromLocation(Location location, String tagString) {
		Assert.hasText(tagString);
		
		Tag tag = tagService.findByTag(tagString); 
		
		if(tag == null) {
			tag = new Tag();
			tag.setTag(tagString);
			tag.setTimeStamp(new Date());
			
			tag = tagService.save(tag);
		}
		
		if(!location.getTags().contains(tag)) {
			throw new IllegalStateException("Location is not tagged with this tag: " + tag.getTag());
		}
		
		location.getTags().remove(tag);
		locationRepository.save(location);
		
		return location;
	}
}
