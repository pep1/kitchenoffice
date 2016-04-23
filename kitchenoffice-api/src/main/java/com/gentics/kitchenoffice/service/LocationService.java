package com.gentics.kitchenoffice.service;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.gentics.kitchenoffice.data.event.Location;
import com.gentics.kitchenoffice.data.user.User;
import com.gentics.kitchenoffice.repository.LocationRepository;

@Service
public class LocationService {

	private static Logger log = LoggerFactory.getLogger(LocationService.class);

	@Autowired
	private LocationRepository locationRepository;

	@Autowired
	private CrowdUserService userService;

	@Autowired
	TagService tagService;

	@PostConstruct
	public void initialize() {
		log.debug("initializing " + this.getClass().getSimpleName() + " instance ... ");
	}

	public Page<Location> findAll(Pageable pageable) {
		return locationRepository.findAll(pageable);
	}

	public Location findByName(String name) {
		Assert.hasLength(name);
		return locationRepository.findByName(name);
	}

	public Page<Location> findByNameLike(Pageable pageable, String name) {
		if (StringUtils.hasLength(name) && name.length() > 2) {
			return locationRepository.findByNameLike("*" + name + "*", pageable);
		} else {
			return findAll(pageable);
		}
	}

	public Location getLocationById(Long id) {
		Assert.notNull(id);
		return locationRepository.findById(id);
	}

	public Page<Location> getLastUsedLocations(Pageable pageable, User user, String search) {
		if (user == null) {
			if (StringUtils.hasLength(search) && search.length() > 2) {
				return locationRepository.getLastUsedLocations(search, pageable);
			} else {
				return locationRepository.getLastUsedLocations(pageable);
			}
		} else {
			if (StringUtils.hasLength(search) && search.length() > 2) {
				return locationRepository.getLastUsedLocations(user, search, pageable);
			} else {
				return locationRepository.getLastUsedLocations(user, pageable);
			}
		}
	}

	public Location saveLocation(Location location) {
		return locationRepository.save(location);
	}

	@Transactional
	public Location subscribeToLocation(Location location) {
		return subscribeToLocation(location, userService.getUser());
	}

	@Transactional
	public Location subscribeToLocation(Location location, User user) {

		Assert.notNull(location);
		Assert.notNull(user);

		if (location.getSubscribers().contains(user)) {
			throw new IllegalStateException("User is already in the list of subscribers of location "
					+ location.getName());
		} else {
			location.getSubscribers().add(user);
		}

		return locationRepository.save(location);
	}

	@Transactional
	public Location unSubscribeToLocation(Location location) {
		return unSubscribeToLocation(location, userService.getUser());
	}

	@Transactional
	public Location unSubscribeToLocation(Location location, User user) {

		Assert.notNull(location);
		Assert.notNull(user);

		if (location.getSubscribers().contains(user)) {
			location.getSubscribers().remove(user);
		} else {
			throw new IllegalStateException("User is not in the list of subscribers of location " + location.getName());
		}

		return locationRepository.save(location);
	}

	/*
	 * public Location addTagToLocation(Location location, String tagString) {
	 * Assert.notNull(location); Assert.hasText(tagString);
	 * 
	 * Tag tag = tagService.findByName(tagString);
	 * 
	 * if(tag == null) { tag = new Tag(); tag.setName(tagString);
	 * tag.setTimeStamp(new Date());
	 * 
	 * tag = tagService.save(tag); }
	 * 
	 * if(location.getTags().contains(tag)) { throw new
	 * IllegalStateException("Location " + location.getName() +
	 * " already has this tag: " + tag.getName()); }
	 * 
	 * location.getTags().add(tag); locationRepository.save(location);
	 * 
	 * return location; }
	 * 
	 * public Location removeTagFromLocation(Location location, String
	 * tagString) { Assert.notNull(location); Assert.hasText(tagString);
	 * 
	 * Tag tag = tagService.findByName(tagString);
	 * 
	 * if(tag == null) { tag = new Tag(); tag.setName(tagString);
	 * tag.setTimeStamp(new Date());
	 * 
	 * tag = tagService.save(tag); }
	 * 
	 * if(!location.getTags().contains(tag)) { throw new
	 * IllegalStateException("Location: " + location.getName() +
	 * " is not tagged with this tag: " + tag.getName()); }
	 * 
	 * location.getTags().remove(tag); locationRepository.save(location);
	 * tagService.checkAndCleanUp(tag);
	 * 
	 * return location; }
	 */
}
