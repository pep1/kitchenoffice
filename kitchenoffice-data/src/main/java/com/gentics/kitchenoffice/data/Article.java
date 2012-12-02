package com.gentics.kitchenoffice.data;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

@NodeEntity
public class Article extends AbstractPersistable {
  
    @Indexed
    private String name;

    private String unit;
    
    private double price;
    
    @Fetch
    @RelatedTo(type = "HAS_IMAGE", direction = Direction.OUTGOING)
	private Image image = new Image();
    
    public Article() {
    	
    }

	public Article(String name, String unit, double price) {
		super();
		this.name = name;
		this.unit = unit;
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
	
	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}
	
	
	@Override
	public String toString() {
		return String.format("Article{name='%s', price=%f}", name, price);
	}
}
