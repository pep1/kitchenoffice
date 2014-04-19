package com.gentics.kitchenoffice.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gentics.kitchenoffice.data.user.User;
import com.gentics.kitchenoffice.exception.EmailExistsAlreadyException;
import com.gentics.kitchenoffice.exception.UserExistsAlreadyException;
import com.gentics.kitchenoffice.service.EventService;
import com.gentics.kitchenoffice.service.UserService;
import com.gentics.kitchenoffice.service.authentication.UserDetailsAuthenticationProvider;

@Controller
public class ViewController implements MessageSourceAware {

	private static Logger log = LoggerFactory.getLogger(ViewController.class);

	@Autowired
	private UserService userService;
	
	@Autowired
	private UserDetailsAuthenticationProvider authProvider;

	@Autowired
	private EventService eventService;

	private MessageSource messages;

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

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String getLoginpage(Model model) {
		log.debug("login page called");

		User user = new User();
		model.addAttribute("user", user);

		return "login";
	}

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String getRegisterPage(ModelMap model) {
		log.debug("register page called");

		User user = new User();
		model.addAttribute("user", user);

		return "register";
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ModelAndView register(final @Valid @ModelAttribute User user, final BindingResult result,
			final SessionStatus status, Model uiModel, HttpServletRequest httpServletRequest, Locale locale,
			RedirectAttributes redirectAttributes) {
		log.debug("register action called");

		ModelAndView modelAndView = null;

		if (result.hasErrors()) {
			modelAndView = new ModelAndView("register");
			user.setPassword("");
			modelAndView.addObject("user", user);
			return modelAndView;
		} else {
			try {
				authProvider.register(user);

				modelAndView = new ModelAndView("redirect:login");
				redirectAttributes.addFlashAttribute("message",
						messages.getMessage("register.successfull", new Object[] { user.getUsername() }, locale));

				return modelAndView;

			} catch (DataIntegrityViolationException e) {

				log.info("registering failed: " + e.getMessage());

				modelAndView = new ModelAndView("register");
				user.setPassword("");

				result.addError(new FieldError("user", "username", e.getMessage()));
				modelAndView.addObject("user", user);
				return modelAndView;

			} catch (UserExistsAlreadyException e) {

				log.info("registering failed: " + e.getMessage());

				modelAndView = new ModelAndView("register");
				user.setPassword("");
				user.setUsername("");

				result.addError(new FieldError("user", "username", e.getMessage()));
				modelAndView.addObject("user", user);
				return modelAndView;

			} catch (EmailExistsAlreadyException e) {

				log.info("registering failed: " + e.getMessage());

				modelAndView = new ModelAndView("register");
				user.setPassword("");
				user.setEmail("");

				result.addError(new FieldError("user", "email", e.getMessage()));
				modelAndView.addObject("user", user);
				return modelAndView;
			}
		}
	}

	@RequestMapping(value = "/event/**", method = RequestMethod.GET)
	public ModelAndView getEventpage(Model model) {
		return getIndexRootpage(model);
	}

	@RequestMapping(value = "/event/rss", method = RequestMethod.GET)
	public ModelAndView getEventRSS(Model model) {

		ModelAndView mav = new ModelAndView();

		mav.setViewName("rssViewer");
		mav.addObject(
				RSSEventViewer.FEED_CONTENT_KEY,
				eventService.findEvents(
						new PageRequest(0, eventService.getRssItemCount(), new Sort(new Order(Direction.DESC,
								"creationDate")))).getContent());

		return mav;
	}

	@RequestMapping(value = "/location/**", method = RequestMethod.GET)
	public ModelAndView getLocationpage(Model model) {
		return getIndexRootpage(model);
	}

	@Override
	public void setMessageSource(MessageSource messageSource) {
		this.messages = messageSource;
	}

}
