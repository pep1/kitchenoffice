package com.gentics.kitchenoffice.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class ViewController {
	
	private static Logger log = Logger.getLogger(ViewController.class);

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String getIndexRootpage(Model model) {
		log.debug("index page called");
		return "index";
	}
	
}
