package com.gentics.kitchenoffice.repository;

import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.gentics.kitchenoffice.data.Tag;

public interface TagRepository extends GraphRepository<Tag> {

	public Tag findByTag(String tag);
	
	@Query("start tag=node({0}) " + 
			" match tag<-[:HAS_TAG]-objects" + 
			" return distinct objects")
	public List<Object> findTaggedObjects(Tag tag);
	
	@Query("start tag=node({0}) " + 
			" match tag<-[:HAS_TAG]-objects" + 
			" return count(distinct objects)")
	public Long countTaggedObjects(Tag tag);
}
