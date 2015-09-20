package com.pep1.kitchenoffice.data.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pep1.kitchenoffice.data.AbstractPersistable;
import com.pep1.kitchenoffice.data.event.Location;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;
import org.springframework.data.neo4j.support.index.IndexType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@NodeEntity
@NoArgsConstructor
@Getter
@Setter
public class User extends AbstractPersistable implements UserDetails {

    @Indexed(indexType = IndexType.FULLTEXT, indexName = "userfirstnamesearch")
    private String firstName;

    @Indexed(indexType = IndexType.FULLTEXT, indexName = "userlastnamesearch")
    private String lastName;

    @Indexed(indexType = IndexType.FULLTEXT, indexName = "userusernamesearch")
    private String username;

    @JsonIgnore
    private String password;

    private String email;

    @JsonIgnore
    private boolean enabled;

    @Fetch
    @RelatedTo(type = "HAS_ROLES", direction = Direction.BOTH, elementClass = Role.class)
    @JsonIgnore
    private Set<Role> roles = new HashSet<Role>();

    @RelatedTo(type = "SUBSCRIBES", direction = Direction.BOTH, elementClass = Location.class)
    private Set<Location> locationSubscriptions = new HashSet<Location>();

    public User(String firstName, String lastName, String userName, boolean enabled) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = userName;
        this.enabled = enabled;
    }

    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @JsonIgnore
    public boolean isAccountNonExpired() {
        return isEnabled();
    }

    @JsonIgnore
    public boolean isAccountNonLocked() {
        return isEnabled();
    }

    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return isEnabled();
    }

}
