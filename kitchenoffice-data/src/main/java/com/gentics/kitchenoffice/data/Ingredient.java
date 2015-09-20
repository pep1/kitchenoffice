package com.gentics.kitchenoffice.data;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.RelationshipEntity;
import org.springframework.data.neo4j.annotation.StartNode;

@RelationshipEntity @NoArgsConstructor
public class Ingredient extends AbstractPersistable{
	
    /**
     *
     */
    private static final long serialVersionUID = -8100798945864077147L;

    @Fetch
    @StartNode
    @Getter @Setter
    private Recipe recipe;

    @Fetch
    @EndNode
    @Getter @Setter
    private Article article;
    
    @Getter @Setter
    private double amount;
    
    public Ingredient(Article article, Recipe recipe, double amount) {
	super();
	this.article = article;
	this.recipe = recipe;
	this.amount = amount;
    }

    @Override
    public String toString() {
	return String.format("Incredient{name='%s', price=%s, recipe=%s}", article.getName(), String.valueOf(amount * article.getPrice()), recipe.getName());
    }

}
