package org.kitchenoffice.data.repository;

import org.kitchenoffice.data.domain.User;
import org.springframework.data.neo4j.repository.GraphRepository;

public interface UserRepository extends GraphRepository<User>{

}
