package com.gentics.kitchenoffice.service;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.gentics.kitchenoffice.data.Image;
import com.gentics.kitchenoffice.data.Thumbnail;
import com.gentics.kitchenoffice.data.event.Location;
import com.gentics.kitchenoffice.data.user.User;
import com.gentics.kitchenoffice.repository.LocationRepository;

@Service
public class LocationService {

	private static Logger log = LoggerFactory.getLogger(LocationService.class);

	@Autowired
	private LocationRepository locationRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private StorageService storageService;

	@Autowired
	private ImageService imageService;

	@Autowired
	TagService tagService;

	@PostConstruct
	public void initialize() {
		log.debug("initializing " + this.getClass().getSimpleName() + " instance ... ");
	}

	@Transactional
	public Page<Location> findAll(Pageable pageable) {
		Page<Location> output = locationRepository.findAll(pageable);
		injectTransientFields(output.getContent());
		return output;
	}

	@Transactional
	public Location findByName(String name) {
		Assert.hasLength(name);

		Location output = locationRepository.findByName(name);

		injectTransientFields(output);
		return output;
	}

	@Transactional
	public Page<Location> findByNameLike(Pageable pageable, String name) {

		Page<Location> output;

		if (StringUtils.hasLength(name) && name.length() > 2) {
			output = locationRepository.findByNameLike("*" + name + "*", pageable);
		} else {
			output = findAll(pageable);
		}

		injectTransientFields(output.getContent());
		return output;
	}

	@Transactional
	public Location getLocationById(Long id) {
		Assert.notNull(id);
		Location output = locationRepository.findById(id);
		injectTransientFields(output);
		return output;
	}

	@Transactional
	public Page<Location> getLastUsedLocations(Pageable pageable, User user, String search) {

		Page<Location> page = null;

		if (user == null) {
			if (StringUtils.hasLength(search) && search.length() > 2) {
				page = locationRepository.getLastUsedLocations(search, pageable);
			} else {
				page = locationRepository.getLastUsedLocations(pageable);
			}
		} else {
			if (StringUtils.hasLength(search) && search.length() > 2) {
				page = locationRepository.getLastUsedLocations(user, search, pageable);
			} else {
				page = locationRepository.getLastUsedLocations(user, pageable);
			}
		}

		return page;
	}

	/**
	 * Saves the location. If there is a new image appended, the image will be
	 * processed.
	 * 
	 * @param location
	 * @return the saved location
	 * @throws IOException
	 */
	@Transactional
	public Location saveLocation(Location location) throws IOException {

		if (!StringUtils.isEmpty(location.getImageUrl())) {
			// create new image object
			Image newImage = imageService.createFromUrl(location.getImageUrl());
			Image oldImage = location.getImage();

			// delete old image if there is one
			if (oldImage != null) { 
				imageService.removeImageObject(imageService.findById(oldImage.getId()));
			}

			// set it as location image
			location.setImage(newImage);
		}

		location = locationRepository.save(location);
		injectTransientFields(location);
		return location;
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

		location = locationRepository.save(location);
		injectTransientFields(location);
		return location;
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

		locationRepository.save(location);
		injectTransientFields(location);
		return location;
	}

	private void injectTransientFields(Location location) {
		Set<Location> helper = new HashSet<Location>();
		helper.add(location);
		injectTransientFields(helper);
	}

	private void injectTransientFields(Collection<Location> locations) {
		Image image;

		for (Location location : locations) {
			image = location.getImage();

			// image url
			if (image != null) {
				image.setUrl(storageService.getStorage().getStorableUrl(image));

				// thumbnails url
				for (Integer size : imageService.getSizes()) {
					Thumbnail thumb = new Thumbnail();
					thumb.setFileName(FilenameUtils.getBaseName(image.getFileName()) + "." + size + "." + imageService.getThumbExtension());
					thumb.setUrl(storageService.getStorage().getStorableUrl(thumb));

					image.getThumbs().put(size, thumb);
				}
			}
		}

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
