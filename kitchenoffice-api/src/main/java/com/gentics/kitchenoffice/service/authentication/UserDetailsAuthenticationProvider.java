package com.gentics.kitchenoffice.service.authentication;

import java.util.Locale;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.gentics.kitchenoffice.data.user.User;
import com.gentics.kitchenoffice.exception.EmailExistsAlreadyException;
import com.gentics.kitchenoffice.exception.UserExistsAlreadyException;
import com.gentics.kitchenoffice.repository.RoleRepository;
import com.gentics.kitchenoffice.repository.UserRepository;
import com.gentics.kitchenoffice.service.UserService;

@Component("UserDetailsAuthenticationProvider")
@Scope("singleton")
public class UserDetailsAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider implements
		UserDetailsManager, MessageSourceAware {

	private static Logger log = LoggerFactory.getLogger(UserDetailsAuthenticationProvider.class);

	private static final String USER_NOT_FOUND_PASSWORD = "userNotFoundPassword";

	private MessageSource messages;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@PostConstruct
	public void initialize() {
		log.debug("initializing " + this.getClass().getSimpleName() + " instance ...");
	}

	public User register(User user) throws UserExistsAlreadyException, EmailExistsAlreadyException {

		Assert.notNull(user);

		if (userExists(user.getUsername())) {
			throw new UserExistsAlreadyException(messages.getMessage("register.usernamealreadyexists",
					new Object[] { user.getUsername() }, Locale.GERMAN));
		}

		if (userService.emailExists(user.getEmail())) {
			throw new EmailExistsAlreadyException(messages.getMessage("register.emailalreadyexists",
					new Object[] { user.getEmail() }, Locale.GERMAN));
		}

		// user.setRegDate(new Date());

		// encrypt password
		user.setPassword(passwordEncoder.encode(user.getPassword()));

		// give role user
		user.getAuthorities().add(roleRepository.findByRoleName(UserService.ROLE_USER_NAME));

		// set locked to false
		user.setEnabled(true);

		// save
		return userRepository.save(user);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = userRepository.findByUsername(username);

		if (user == null) {
			throw new UsernameNotFoundException("No user found with given username: " + username);
		}

		return user;
	}

	@Override
	public void createUser(UserDetails user) {
		register((User) user);
	}

	@Override
	public void updateUser(UserDetails user) {
		userRepository.save((User) user);
	}

	@Override
	public void deleteUser(String username) {
		userRepository.delete(userRepository.findByUsername(username));
	}

	@Override
	public void changePassword(String oldPassword, String newPassword) {

		Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();

		if (currentUser == null) {
			throw new AccessDeniedException("Can't change password as no Authentication object found in context "
					+ "for current user.");
		}

		User user = userRepository.findByUsername(currentUser.getName());

		// check if the old password is really the old one
		if (!passwordEncoder.matches(newPassword, oldPassword)) {
			throw new BadCredentialsException(messages.getMessage("credentials.invalid", null, Locale.GERMAN));
		}

		user.setPassword(newPassword);
		userRepository.save(user);
	}

	@Override
	public boolean userExists(String username) {
		return userRepository.countByUsername(username) != 0;
	}

	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails,
			UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {

		if (authentication.getCredentials() == null) {
			logger.debug("Authentication failed: no credentials provided");

			throw new BadCredentialsException(messages.getMessage("login.badcredentials", null, null));
		}

		String presentedPassword = authentication.getCredentials().toString();

		if (!passwordEncoder.matches(presentedPassword, userDetails.getPassword())) {
			logger.debug("Authentication failed: password does not match stored value");

			throw new BadCredentialsException("Authentication Failed: Bad Credentials");
		}
	}

	@Override
	protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication)
			throws AuthenticationException {
		UserDetails loadedUser;

		try {
			loadedUser = loadUserByUsername(username);
		} catch (UsernameNotFoundException notFound) {
			if (authentication.getCredentials() != null) {
				String presentedPassword = authentication.getCredentials().toString();
				passwordEncoder.matches(USER_NOT_FOUND_PASSWORD, presentedPassword);
			}
			throw notFound;
		} catch (Exception repositoryProblem) {
			throw new AuthenticationServiceException(repositoryProblem.getMessage(), repositoryProblem);
		}

		if (loadedUser == null) {
			throw new AuthenticationServiceException(
					"UserDetailsService returned null, which is an interface contract violation");
		}
		return loadedUser;
	}

}
