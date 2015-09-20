package com.pep1.kitchenoffice.repository;

import com.pep1.kitchenoffice.data.user.Role;
import org.springframework.data.neo4j.repository.GraphRepository;

public interface RoleRepository extends GraphRepository<Role> {
	
	public Role findByRoleName(String role);
}
