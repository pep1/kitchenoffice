package org.kitchenoffice.data.repository;

import org.kitchenoffice.data.domain.Comment;
import org.springframework.data.neo4j.repository.GraphRepository;

public interface CommentRepository extends GraphRepository<Comment>{

}
