package com.gentics.kitchenoffice.data;

import java.util.HashSet;
import java.util.Set;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;
import org.springframework.data.neo4j.annotation.RelatedToVia;
import org.springframework.data.neo4j.support.index.IndexType;

@NodeEntity
public class Article extends AbstractPersistable {
  
	@Indexed(unique = true, indexType = IndexType.FULLTEXT, indexName = "articlesearch")
    private String name;

    private String unit;
    
    private double price;
    
    @Fetch
    @RelatedTo(type = "HAS_IMAGE", direction = Direction.OUTGOING)
	private Image image = new Image();
    
    @Fetch
    @RelatedToVia(type = "IS_NEEDED_IN", direction = Direction.BOTH)
    private Set<Ingredient> incredients = new HashSet<Ingredient>();
    
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
	
	public Set<Ingredient> getIncredients() {
		return incredients;
	}

	public void setIncredients(Set<Ingredient> incredients) {
		this.incredients = incredients;
	}

	@Override
	public String toString() {
		return String.format("Article{name='%s', price=%f}", name, price);
	}
}
