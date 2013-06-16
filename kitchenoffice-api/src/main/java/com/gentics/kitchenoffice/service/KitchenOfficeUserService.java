package com.gentics.kitchenoffice.service;

import java.util.Collection;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.jasig.cas.client.validation.Assertion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.security.cas.userdetails.AbstractCasAssertionUserDetailsService;

import com.gentics.kitchenoffice.data.user.Role;
import com.gentics.kitchenoffice.data.user.User;
import com.gentics.kitchenoffice.repository.RoleRepository;
import com.gentics.kitchenoffice.repository.UserRepository;

@Service("KitchenOfficeUserService")
@Scope("singleton")
public class KitchenOfficeUserService extends
		AbstractCasAssertionUserDetailsService {

	private static Logger log = Logger
			.getLogger(KitchenOfficeUserService.class);

	public static final String ROLE_USER_NAME = "ROLE_USER";

	public static final String ROLE_ADMIN_NAME = "ROLE_ADMIN";

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@PostConstruct
	public void initialize() {

		log.debug("initializing " + this.getClass().getSimpleName() + " instance ...");
		
		// initially create two roles
		checkAndCreateRoles();

	}

	@Override
	protected UserDetails loadUserDetails(Assertion assertion) {
		
		log.debug("loading user details of cas assertion: " + assertion.getPrincipal()
				.getName());

		User user = userRepository.findUserByUsername(assertion.getPrincipal()
				.getName());

		if (user == null) {
			// if user is not found in database, create one with specified
			// username
			user = createUserByUsername(assertion.getPrincipal().getName());
		}

		user.setFirstName((String) assertion.getPrincipal().getAttributes()
				.get("firstname"));
		user.setLastName((String) assertion.getPrincipal().getAttributes()
				.get("lastname"));

		Object email = assertion.getPrincipal().getAttributes().get("email");

		if (email instanceof String) {
			
			String emailString = ((String)email).replace("[", "");
			emailString = emailString.replace("]", "");
			String[] emails = emailString.split(","); 
			user.setEmail(emails[0]);
		}

		// persist fetched user.
		return userRepository.save(user);
	}

	private void checkAndCreateRoles() {

		if (roleRepository.findByRoleName(ROLE_USER_NAME) == null) {
			Role role = new Role(ROLE_USER_NAME);
			roleRepository.save(role);
		}

		if (roleRepository.findByRoleName(ROLE_ADMIN_NAME) == null) {
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

	public final boolean hasRole(String role) {
		boolean hasRole = false;
		UserDetails userDetails = getUser();
		if (userDetails != null) {
			Collection<? extends GrantedAuthority> authorities = userDetails
					.getAuthorities();
			if (isRolePresent(authorities, role)) {
				hasRole = true;
			}
		}
		return hasRole;
	}

	/**
	 * Get info about currently logged in user
	 * 
	 * @return User if found in the context, null otherwise
	 */
	public User getUser() {
		Object principal = SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();

		User userDetails = null;
		if (principal instanceof User) {
			userDetails = (User) principal;
		}
		return userDetails;
	}

	/**
	 * Check if a role is present in the authorities of current user
	 * 
	 * @param authorities
	 *            all authorities assigned to current user
	 * @param role
	 *            required authority
	 * @return true if role is present in list of authorities assigned to
	 *         current user, false otherwise
	 */
	private boolean isRolePresent(
			Collection<? extends GrantedAuthority> authorities, String role) {
		boolean isRolePresent = false;
		for (GrantedAuthority grantedAuthority : authorities) {
			isRolePresent = grantedAuthority.getAuthority().equals(role);
			if (isRolePresent)
				break;
		}
		return isRolePresent;
	}

}
