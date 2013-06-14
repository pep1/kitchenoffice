package com.gentics.kitchenoffice.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.gentics.kitchenoffice.service.KitchenOfficeUserService;


@Controller
public class ViewController {
	
	private static Logger log = Logger.getLogger(ViewController.class);
	
	@Autowired
	private KitchenOfficeUserService userService;

	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView getIndexRootpage(Model model) {
		log.debug("index page called");
		
		ModelAndView modelAndView = new ModelAndView("index");
		modelAndView.addObject("user", userService.getUser());
		
		return modelAndView;
	}
	
	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public ModelAndView getHomepage(Model model) {
		
		return getIndexRootpage(model);
	}
	
	@RequestMapping(value = "/event/*", method = RequestMethod.GET)
	public ModelAndView getEventpage(Model model) {
		
		return getIndexRootpage(model);
	}
	
}
