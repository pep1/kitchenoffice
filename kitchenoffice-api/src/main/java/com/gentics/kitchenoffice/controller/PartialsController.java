package com.gentics.kitchenoffice.controller;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/partials")
public class PartialsController {
	
private static Logger log = Logger.getLogger(PartialsController.class);
	
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
	
	@RequestMapping(value = "/location/create", method = RequestMethod.GET)
	public String getLocationPage(Model model) {
		log.debug("location create partial called");
		return "partials/location/create";
	}

}
