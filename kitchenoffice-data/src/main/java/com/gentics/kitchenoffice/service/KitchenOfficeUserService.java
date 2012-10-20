package com.gentics.kitchenoffice.service;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.gentics.kitchenoffice.data.Role;
import com.gentics.kitchenoffice.data.User;
import com.gentics.kitchenoffice.repository.RoleRepository;
import com.gentics.kitchenoffice.repository.UserRepository;

@Service
@Scope("singleton")
public class KitchenOfficeUserService implements UserDetailsService {
	
	private static Logger log = Logger.getLogger(KitchenOfficeUserService.class);
	
	private static final String ROLE_USER_NAME = "ROLE_USER";
	
	private static final String ROLE_ADMIN_NAME = "ROLE_ADMIN";
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	RoleRepository roleRepository;

	
	@PostConstruct
	public void initialize() {
		log.debug("initializing " + this.getClass() + "...");

		// initially create two roles
		checkAndCreateRoles();
		
	}
	
	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		
		log.debug("loadbyUsername called, username: " + username);
		
		User user = userRepository.findUserByUsername(username);
		
		if(user == null) {
			// if user is not found in database, create one with specified username
			return createUserByUsername(username);
		}
		
		return user;
	}
	
	
	private void checkAndCreateRoles() {
		
		if(roleRepository.findByRoleName(ROLE_USER_NAME) == null) {
			Role role = new Role(ROLE_USER_NAME);
			roleRepository.save(role);
		}
		
		if(roleRepository.findByRoleName(ROLE_ADMIN_NAME) == null) {
			Role role = new Role(ROLE_ADMIN_NAME);
			roleRepository.save(role);
		}
		
	}
	
	private User createUserByUsername(String username) {
		
		User user = new User();
		user.setUsername(username);
		user.setEnabled(true);
		
		user = userRepository.save(user);
		
		user.getRoles().add(roleRepository.findByRoleName(ROLE_USER_NAME));
		
		return userRepository.save(user);
		
	}

}
