package org.kitchenoffice.data.domain;

import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;

@NodeEntity
public class Article extends AbstractPersistable {
  
    @Indexed
    private String name;

    private String unit;
    
    private double price;
    
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
	
	@Override
	public String toString() {
		return String.format("Incredient{name='%s', price=%f}", name, price);
	}

}
