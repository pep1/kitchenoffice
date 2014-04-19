package com.gentics.kitchenoffice.service.authentication;

import org.jasig.cas.client.validation.Assertion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.security.cas.userdetails.AbstractCasAssertionUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.gentics.kitchenoffice.data.user.User;
import com.gentics.kitchenoffice.repository.RoleRepository;
import com.gentics.kitchenoffice.repository.UserRepository;
import com.gentics.kitchenoffice.service.UserService;

@Component("CasAuthenticationProvider")
@Scope("singleton")
public class CasAuthenticationProvider extends AbstractCasAssertionUserDetailsService {
	
	private static Logger log = LoggerFactory.getLogger(CasAuthenticationProvider.class);

	@Value("${webapp.job.defaults}")
	private String[] defaultJobIds;

	@Value("${webapp.security.adminemails}")
	private String[] adminEmails;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Override
	protected UserDetails loadUserDetails(Assertion assertion) {

		log.debug("loading user details of cas assertion: " + assertion.getPrincipal().getName());

		User user = userRepository.findByUsername(assertion.getPrincipal().getName());

		if (user == null) {
			// if user is not found in database, create one with specified
			// username
			user = createUserByUsername(assertion.getPrincipal().getName());
		}

		user.setFirstName((String) assertion.getPrincipal().getAttributes().get("firstname"));
		user.setLastName((String) assertion.getPrincipal().getAttributes().get("lastname"));

		Object email = assertion.getPrincipal().getAttributes().get("email");

		if (email != null && email instanceof String) {
			String emailString = ((String) email).replace("[", "");
			emailString = emailString.replace("]", "");
			String[] emails = emailString.split(",");
			user.setEmail(emails[0]);
		}

		// persist fetched user.
		return userRepository.save(user);
	}

	@Transactional
	private User createUserByUsername(String username) {

		User user = new User();
		user.setUsername(username);
		user.setEnabled(true);

		user = userRepository.save(user);

		user.getRoles().add(roleRepository.findByRoleName(UserService.ROLE_USER_NAME));

		return userRepository.save(user);
	}

}
