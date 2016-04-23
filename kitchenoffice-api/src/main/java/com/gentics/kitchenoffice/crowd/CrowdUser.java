package com.gentics.kitchenoffice.crowd;

import com.atlassian.crowd.integration.soap.SOAPPrincipal;
import com.atlassian.crowd.integration.springsecurity.user.CrowdUserDetails;
import com.gentics.kitchenoffice.data.user.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @author Leonard Osang <leonard.osang@audi.de>
 * @since 4/22/16
 */
public class CrowdUser extends CrowdUserDetails {

    @Getter
    @Setter
    private User kitchenUser;

    public CrowdUser(SOAPPrincipal principal, GrantedAuthority[] authorities) {
        super(principal, authorities);
    }

    public CrowdUser(SOAPPrincipal principal, Collection<GrantedAuthority> authorities) {
        super(principal, authorities);
    }
}
