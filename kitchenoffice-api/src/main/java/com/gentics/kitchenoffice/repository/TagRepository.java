package com.gentics.kitchenoffice.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.gentics.kitchenoffice.data.Tag;

public interface TagRepository extends GraphRepository<Tag> {

	public Tag findByName(String name);
	
	public List<Tag> findByNameLike(String name, Pageable pageable);
	
	@Query("start tag=node({0}) " + 
			" match tag<-[:HAS_TAG]-objects" + 
			" return distinct objects")
	public List<Object> findTaggedObjects(Tag tag);
	
	@Query("start tag=node({0}) " + 
			" match tag<-[:HAS_TAG]-objects" + 
			" return count(distinct objects)")
	public Long countTaggedObjects(Tag tag);
	
	@Query("start tags=node:__types__(className=\"com.gentics.kitchenoffice.data.Tag\")" + 
			" match tags<-[r:HAS_TAG]-objects" +
			" with tags, count(objects) as counter" + 
			" where counter = 0" + 
			" return distinct tags")
	public List<Tag> findWithoutRelation();
}
