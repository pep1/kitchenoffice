package com.gentics.kitchenoffice.service;

import com.atlassian.crowd.exception.InvalidAuthenticationException;
import com.atlassian.crowd.exception.InvalidAuthorizationTokenException;
import com.atlassian.crowd.exception.InvalidTokenException;
import com.atlassian.crowd.exception.UserNotFoundException;
import com.atlassian.crowd.integration.soap.SOAPPrincipal;
import com.atlassian.crowd.integration.springsecurity.CrowdSSOTokenInvalidException;
import com.atlassian.crowd.integration.springsecurity.user.CrowdDataAccessException;
import com.atlassian.crowd.integration.springsecurity.user.CrowdUserDetails;
import com.atlassian.crowd.integration.springsecurity.user.CrowdUserDetailsService;
import com.atlassian.crowd.service.GroupMembershipManager;
import com.atlassian.crowd.service.UserManager;
import com.atlassian.crowd.user.UserAuthoritiesProvider;
import com.gentics.kitchenoffice.crowd.CrowdUser;
import com.gentics.kitchenoffice.data.Job;
import com.gentics.kitchenoffice.data.user.User;
import com.gentics.kitchenoffice.repository.JobRepository;
import com.gentics.kitchenoffice.repository.RoleRepository;
import com.gentics.kitchenoffice.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CrowdUserService implements CrowdUserDetailsService {

    private static Logger log = LoggerFactory.getLogger(CrowdUserService.class);

    @Value("${webapp.job.defaults}")
    private String[] defaultJobIds;

    @Value("${webapp.security.adminemails}")
    private String[] adminEmails;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    JobRepository jobRepository;

    @PostConstruct
    @Transactional
    public void initialize() {

        log.debug("initializing " + this.getClass().getSimpleName() + " instance ...");

        // check and create default jobs
        checkAndCreateDefaultJobs();
    }

    private UserManager userManager;
    private GroupMembershipManager groupMembershipManager;
    private String authorityPrefix = "";
    private Iterable<Map.Entry<String, String>> groupToAuthorityMappings;
    private String adminAuthority = "ROLE_ADMIN";
    private UserAuthoritiesProvider userAuthoritiesProvider;

    public CrowdUserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
        try {
            SOAPPrincipal e = this.userManager.getUser(username);
            CrowdUser crowdUser = new CrowdUser(e, this.getAuthorities(e.getName()));

            User user = userRepository.findUserByUsername(crowdUser.getUsername());

            if (user == null) {
                // if user is not found in database, create one with specified
                // username
                user = createUserByUsername(crowdUser.getUsername());
            }

            user.setFirstName(crowdUser.getFirstName());
            user.setLastName(crowdUser.getLastName());
            user.setEmail(crowdUser.getEmail());
            userRepository.save(user);

            crowdUser.setKitchenUser(user);

            return crowdUser;
        } catch (InvalidAuthorizationTokenException var3) {
            throw new CrowdDataAccessException(var3);
        } catch (RemoteException var4) {
            throw new CrowdDataAccessException(var4);
        } catch (UserNotFoundException var5) {
            throw new UsernameNotFoundException("Could not find principal in Crowd with username: " + username, var5);
        } catch (InvalidAuthenticationException var6) {
            throw new CrowdDataAccessException(var6);
        }
    }

    public CrowdUser loadUserByToken(String token) throws CrowdSSOTokenInvalidException, DataAccessException {
        try {
            SOAPPrincipal e = this.userManager.getUserFromToken(token);

            CrowdUser crowdUser = new CrowdUser(e, this.getAuthorities(e.getName()));
            User user = userRepository.findUserByUsername(crowdUser.getUsername());

            if (user == null) {
                throw new IllegalStateException("User with username " + crowdUser.getUsername() + " not found in local db!");
            }

            crowdUser.setKitchenUser(user);

            return crowdUser;
        } catch (InvalidAuthorizationTokenException var3) {
            throw new CrowdDataAccessException(var3);
        } catch (RemoteException var4) {
            throw new CrowdDataAccessException(var4);
        } catch (UsernameNotFoundException var5) {
            throw new CrowdDataAccessException(var5);
        } catch (InvalidTokenException var6) {
            throw new CrowdSSOTokenInvalidException(var6.getMessage(), var6);
        } catch (InvalidAuthenticationException var7) {
            throw new CrowdDataAccessException(var7);
        } catch (UserNotFoundException var8) {
            throw new CrowdDataAccessException(var8);
        }
    }

    Collection<GrantedAuthority> getAuthorities(String username) throws InvalidAuthorizationTokenException, RemoteException, UserNotFoundException, InvalidAuthenticationException {
        if (this.userAuthoritiesProvider == null) {
            if (this.groupToAuthorityMappings == null) {
                List userGroups1 = this.groupMembershipManager.getMemberships(username);
                return this.generateAuthoritiesFromGroupNames(userGroups1);
            } else {
                return this.generateAuthorityFromMap(username);
            }
        } else {
            ArrayList userGroups = new ArrayList();
            Iterator i$ = this.userAuthoritiesProvider.getAuthorityNames(username).iterator();

            while (i$.hasNext()) {
                String authority = (String) i$.next();
                userGroups.add(new SimpleGrantedAuthority(authority));
            }

            return userGroups;
        }
    }

    private Set<GrantedAuthority> generateAuthorityFromMap(String username) throws RemoteException, InvalidAuthenticationException, InvalidAuthorizationTokenException {
        HashSet authorities = new HashSet();
        Iterator i$ = this.groupToAuthorityMappings.iterator();

        while (i$.hasNext()) {
            Map.Entry groupToAuthorityMapEntry = (Map.Entry) i$.next();
            if (this.groupMembershipManager.isMember(username, (String) groupToAuthorityMapEntry.getKey())) {
                authorities.add(new SimpleGrantedAuthority((String) groupToAuthorityMapEntry.getValue()));
            }
        }

        return authorities;
    }

    private List<GrantedAuthority> generateAuthoritiesFromGroupNames(List userGroups) {
        ArrayList authorities = new ArrayList();
        Iterator iterator = userGroups.iterator();

        while (iterator.hasNext()) {
            authorities.add(new SimpleGrantedAuthority(this.getAuthorityPrefix() + iterator.next()));
        }

        return authorities;
    }

    public String getAuthorityPrefix() {
        return this.authorityPrefix;
    }

    public void setAuthorityPrefix(String authorityPrefix) {
        this.authorityPrefix = authorityPrefix;
    }

    public Iterable<Map.Entry<String, String>> getGroupToAuthorityMappings() {
        return this.groupToAuthorityMappings;
    }

    public void setGroupToAuthorityMappings(Iterable<Map.Entry<String, String>> groupToAuthorityMappings) {
        this.groupToAuthorityMappings = groupToAuthorityMappings;
    }

    public void setUserAuthoritiesProvider(UserAuthoritiesProvider userAuthoritiesProvider) {
        this.userAuthoritiesProvider = userAuthoritiesProvider;
    }

    public String getAdminAuthority() {
        return this.adminAuthority;
    }

    public void setAdminAuthority(String adminAuthority) {
        this.adminAuthority = adminAuthority;
    }

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    public void setGroupMembershipManager(GroupMembershipManager groupMembershipManager) {
        this.groupMembershipManager = groupMembershipManager;
    }

    private User createUserByUsername(String username) {

        User user = new User();
        user.setUsername(username);
        user.setEnabled(true);

        return userRepository.save(user);
    }

    private void checkAndCreateDefaultJobs() {
        for (String jobId : defaultJobIds) {
            if (jobRepository.findByName(jobId) == null) {
                log.debug("Creating initial job with name: " + jobId);
                Job newJob = new Job();
                newJob.setName(jobId);
                jobRepository.save(newJob);
            }
        }
    }

    public final boolean hasRole(String role) {
        boolean hasRole = false;
        UserDetails userDetails = getUser();
        if (userDetails != null) {
            Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
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
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User userDetails = null;
        if (principal instanceof CrowdUser) {
            userDetails = ((CrowdUser) principal).getKitchenUser();
        }
        return userDetails;
    }

    public List<User> findAll(Pageable pageable) {
        Assert.notNull(pageable);
        return userRepository.findAll(pageable).getContent();
    }

    /**
     * Check if a role is present in the authorities of current user
     *
     * @param authorities all authorities assigned to current user
     * @param role        required authority
     * @return true if role is present in list of authorities assigned to
     * current user, false otherwise
     */
    private boolean isRolePresent(Collection<? extends GrantedAuthority> authorities, String role) {
        boolean isRolePresent = false;
        for (GrantedAuthority grantedAuthority : authorities) {
            isRolePresent = grantedAuthority.getAuthority().equals(role);
            if (isRolePresent) {
                break;
            }
        }
        return isRolePresent;
    }

    public User getRefreshedUser() {
        return userRepository.findOne(getUser().getId());
    }

}
