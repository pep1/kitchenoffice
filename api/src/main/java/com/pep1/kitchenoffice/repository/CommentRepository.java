package com.pep1.kitchenoffice.repository;

import org.springframework.data.neo4j.repository.GraphRepository;

import com.pep1.kitchenoffice.data.Comment;

public interface CommentRepository extends GraphRepository<Comment>{

}
