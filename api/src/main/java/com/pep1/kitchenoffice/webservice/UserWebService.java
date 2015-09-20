/*
 * 
 */
package com.pep1.kitchenoffice.webservice;

import com.pep1.kitchenoffice.data.user.User;
import com.pep1.kitchenoffice.service.UserService;
import com.pep1.kitchenoffice.webservice.filter.CacheAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * The Class UserWebService.
 * <p>
 * Provides user CRUD functionality.
 */
@RestController
@Scope("singleton")
@RequestMapping("/api/v1/users")
@CacheAnnotations.NoCache
public class UserWebService {

    /**
     * The log.
     */
    private static Logger log = LoggerFactory.getLogger(UserWebService.class);

    /**
     * The user service.
     */
    @Autowired
    private UserService userService;

    /**
     * Gets the user object of logged in user.
     *
     * @return the me
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/me", method = RequestMethod.GET)
    public User getMe() {
        log.debug("Calling user getMe");
        return userService.getRefreshedUser();
    }

    /**
     * Get all users.
     *
     * @return the list
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(method = RequestMethod.GET)
    public List<User> findAll(Pageable pageable) {
        log.debug("Calling user findAll");
        return userService.findAll(pageable);
    }

}
