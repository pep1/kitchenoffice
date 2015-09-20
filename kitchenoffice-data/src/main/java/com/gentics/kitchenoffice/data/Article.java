package com.gentics.kitchenoffice.data;

import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;
import org.springframework.data.neo4j.annotation.RelatedToVia;
import org.springframework.data.neo4j.support.index.IndexType;

@NodeEntity
public class Article extends AbstractPersistable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7839328132356893992L;

	@Indexed(indexType = IndexType.FULLTEXT, indexName = "articlenamesearch")
	@Getter @Setter
    private String name;

	@Getter @Setter
    private String unit;
    
	@Getter @Setter
    private double price;
    
    @Fetch
    @RelatedTo(type = "HAS_IMAGE", direction = Direction.OUTGOING, elementClass = Image.class)
	@Getter @Setter
	private Image image = new Image();
    
    @Fetch
    @RelatedToVia(type = "IS_NEEDED_IN", direction = Direction.BOTH, elementClass = Ingredient.class)
	@Getter @Setter
    private Set<Ingredient> incredients = new HashSet<Ingredient>();
    
    public Article() {
    	
    }

	public Article(String name, String unit, double price) {
		super();
		this.name = name;
		this.unit = unit;
		this.price = price;
	}

	@Override
	public String toString() {
		return String.format("Article{name='%s', price=%f}", name, price);
	}
}
