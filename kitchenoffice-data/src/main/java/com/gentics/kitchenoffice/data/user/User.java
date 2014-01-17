package com.gentics.kitchenoffice.data.user;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;
import org.springframework.data.neo4j.support.index.IndexType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.gentics.kitchenoffice.data.AbstractPersistable;
import com.gentics.kitchenoffice.data.event.Location;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NodeEntity @NoArgsConstructor
public class User extends AbstractPersistable implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1479792543611842572L;

	@Indexed(indexType = IndexType.FULLTEXT, indexName = "userfirstnamesearch")
        @Getter @Setter
	private String firstName;

	@Indexed(indexType = IndexType.FULLTEXT, indexName = "userlastnamesearch")
        @Getter @Setter
	private String lastName;

	@Indexed(indexType = IndexType.FULLTEXT, indexName = "userusernamesearch")
        @Getter @Setter
	private String username;

	@JsonIgnore @Getter @Setter
	private String password;

        @Getter @Setter
	private String email;

	@JsonIgnore @Getter @Setter
	private boolean enabled;

	@Fetch
	@RelatedTo(type = "HAS_ROLES", direction = Direction.BOTH, elementClass = Role.class)
        @JsonIgnore @Getter @Setter
	private Set<Role> roles = new HashSet<Role>();
	
	@RelatedTo(type = "SUBSCRIBES", direction = Direction.BOTH, elementClass = Location.class)
	@Getter @Setter
	private Set<Location> locationSubscriptions= new HashSet<Location>();

	public User(String firstName, String lastName, String userName, boolean enabled) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = userName;
		this.enabled = enabled;
	}

	@Override
	@JsonIgnore
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return roles;
	}

	@Override
	@JsonIgnore
	public boolean isAccountNonExpired() {
		return isEnabled();
	}

	@Override
	@JsonIgnore
	public boolean isAccountNonLocked() {
		return isEnabled();
	}

	@Override
	@JsonIgnore
	public boolean isCredentialsNonExpired() {
		return isEnabled();
	}

}
