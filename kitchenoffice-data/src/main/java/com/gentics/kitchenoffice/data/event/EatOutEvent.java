package com.gentics.kitchenoffice.data.event;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

@NodeEntity
public class EatOutEvent extends Event {
	
	
	@Fetch
    @RelatedTo(type = "HAS_LOCATION", direction = Direction.BOTH)
	private Location location = new Location();
	
	public EatOutEvent() {
		super();
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

}
