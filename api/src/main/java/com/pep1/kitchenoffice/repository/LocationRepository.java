package com.pep1.kitchenoffice.repository;

import com.pep1.kitchenoffice.data.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.pep1.kitchenoffice.data.event.Location;

public interface LocationRepository extends GraphRepository<Location> {

	public Location findById(Long id);
	
	public Location findByName(String name);
	
	@Query("start locations=node:__types__(className=\"Location\") "
			+ "match locations<-[:HAS_LOCATION]-event "
			+ "where event.creator={0} "
			+ "return locations "
			+ "order by event.creationDate asc")
	public Page<Location> getLastUsedLocations(User user, Pageable pageable);

	@Query("start locations=node:__types__(className=\"Location\") "
			+ "match locations<-[:HAS_LOCATION]-event "
			+ "where event.creator={0} "
			+ "and locations.name =~ '.*{1}.*'"
			+ "return locations "
			+ "order by event.creationDate asc")
	public Page<Location> getLastUsedLocations(User user, String search, Pageable pageable);

	@Query("start locations=node:__types__(className=\"Location\") "
			+ "match locations<-[:HAS_LOCATION]-event "
			+ "return locations "
			+ "order by event.creationDate desc")
	public Page<Location> getLastUsedLocations(Pageable pageable);

	@Query("start locations=node:__types__(className=\"Location\") "
			+ "match locations<-[:HAS_LOCATION]-event "
			+ "where locations.name =~ '.*{0}.*'"
			+ "return locations "
			+ "order by event.creationDate desc")
	public Page<Location> getLastUsedLocations(String search, Pageable pageable);

	public Page<Location> findByNameLike(String name, Pageable pageable);

}
