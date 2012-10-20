package com.gentics.kitchenoffice.data;

import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.security.core.GrantedAuthority;

@NodeEntity
public class Role extends AbstractPersistable implements GrantedAuthority{
	
	@Indexed
	private String roleName;
	
	public Role() {
		
	}
	
	public Role(String role) {
		this.roleName = role;
	}

	@Override
	public String getAuthority() {
		return roleName;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String role) {
		this.roleName = role;
	}
	
}
