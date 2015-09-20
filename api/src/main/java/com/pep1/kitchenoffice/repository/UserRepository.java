package com.pep1.kitchenoffice.repository;

import org.springframework.data.neo4j.repository.GraphRepository;

import com.pep1.kitchenoffice.data.user.User;

public interface UserRepository extends GraphRepository<User>{
	
	public User findUserByUsername(String username);

}
