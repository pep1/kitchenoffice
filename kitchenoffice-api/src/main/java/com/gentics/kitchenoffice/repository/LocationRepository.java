package com.gentics.kitchenoffice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.gentics.kitchenoffice.data.event.Location;
import com.gentics.kitchenoffice.data.user.User;

public interface LocationRepository extends GraphRepository<Location> {

	public Location findById(Long id);
	
	@Query("start locations=node:__types__(className=\"com.gentics.kitchenoffice.data.event.Location\") "
			+ "match locations<-[:HAS_LOCATION]-event "
			+ "where event.creator={0} "
			+ "return locations "
			+ "order by event.creationDate asc")
	public Page<Location> getLastUsedLocations(User user, Pageable pageable);

	@Query("start locations=node:__types__(className=\"com.gentics.kitchenoffice.data.event.Location\") "
			+ "match locations<-[:HAS_LOCATION]-event "
			+ "where event.creator={0} "
			+ "and locations.name =~ '.*{1}.*'"
			+ "return locations "
			+ "order by event.creationDate asc")
	public Page<Location> getLastUsedLocations(User user, String search, Pageable pageable);

	@Query("start locations=node:__types__(className=\"com.gentics.kitchenoffice.data.event.Location\") "
			+ "match locations<-[:HAS_LOCATION]-event "
			+ "return locations "
			+ "order by event.creationDate desc")
	public Page<Location> getLastUsedLocations(Pageable pageable);

	@Query("start locations=node:__types__(className=\"com.gentics.kitchenoffice.data.event.Location\") "
			+ "match locations<-[:HAS_LOCATION]-event "
			+ "where locations.name =~ '.*{0}.*'"
			+ "return locations "
			+ "order by event.creationDate desc")
	public Page<Location> getLastUsedLocations(String search, Pageable pageable);

	public Page<Location> findByNameLike(String name, Pageable pageable);

}
