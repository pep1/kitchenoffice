package com.gentics.kitchenoffice.data.user;

import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.security.core.GrantedAuthority;

import com.gentics.kitchenoffice.data.AbstractPersistable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NodeEntity @NoArgsConstructor @AllArgsConstructor
public class Role extends AbstractPersistable implements GrantedAuthority {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4123551412665180604L;

	@Indexed @Getter @Setter
	private String roleName;

	@Override
	public String getAuthority() {
		return roleName;
	}

}
