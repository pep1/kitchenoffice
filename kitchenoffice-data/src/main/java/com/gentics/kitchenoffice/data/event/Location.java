package com.gentics.kitchenoffice.data.event;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

import com.gentics.kitchenoffice.data.AbstractPersistable;
import com.gentics.kitchenoffice.data.Image;

@NodeEntity
public class Location extends AbstractPersistable{
	
	@Indexed(unique = true)
	private String location;
	
	private String locationPage;
	
	private String description;
	
	@Fetch
    @RelatedTo(type = "HAS_IMAGE", direction = Direction.OUTGOING)
	private Image image;
	
	public Location()  {
		super();
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getLocationPage() {
		return locationPage;
	}

	public void setLocationPage(String locationPage) {
		this.locationPage = locationPage;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}
}
