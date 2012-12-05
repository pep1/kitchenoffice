package com.gentics.kitchenoffice.repository;

import org.springframework.data.neo4j.repository.GraphRepository;

import com.gentics.kitchenoffice.data.user.Role;

public interface RoleRepository extends GraphRepository<Role> {
	
	public Role findByRoleName(String role);
}
