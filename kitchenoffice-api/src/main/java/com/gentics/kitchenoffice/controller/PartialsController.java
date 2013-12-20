package com.gentics.kitchenoffice.controller;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/partials")
public class PartialsController {

	private static Logger log = LoggerFactory.getLogger(PartialsController.class);

	@PostConstruct
	public void initialize() {
		log.debug("Initilializing PartialsController..");
	}

	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String getHome(Model model) {
		log.debug("home partial called");
		return "partials/home";
	}

	@RequestMapping(value = "/event/create", method = RequestMethod.GET)
	public String getCreatePage(Model model) {
		log.debug("event create partial called");
		return "partials/event/create";
	}

	@RequestMapping(value = "/event/details", method = RequestMethod.GET)
	public String getDetailsPage(Model model) {
		log.debug("event details partial called");
		return "partials/event/details";
	}

	@RequestMapping(value = "/location/create", method = RequestMethod.GET)
	public String getLocationCreatePage(Model model) {
		log.debug("location create partial called");
		return "partials/location/create";
	}

	@RequestMapping(value = "/location/edit", method = RequestMethod.GET)
	public String getLocationEditPage(Model model) {
		log.debug("location edit partial called");
		return "partials/location/edit";
	}

	@RequestMapping(value = "/location/details", method = RequestMethod.GET)
	public String getLocationDetailsPage(Model model) {
		log.debug("location edit partial called");
		return "partials/location/details";
	}

	@RequestMapping(value = "/location/list", method = RequestMethod.GET)
	public String getLocationlistPage(Model model) {
		log.debug("location edit partial called");
		return "partials/location/list";
	}

}
