package com.gentics.kitchenoffice.repository;

import org.springframework.data.neo4j.repository.GraphRepository;

import com.gentics.kitchenoffice.data.user.User;

public interface UserRepository extends GraphRepository<User> {

	public User findByUsername(String username);

	public Integer countByEmail(String email);

	public Integer countByUsername(String username);

}
