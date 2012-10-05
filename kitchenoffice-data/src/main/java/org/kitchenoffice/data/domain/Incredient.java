package org.kitchenoffice.data.domain;

import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.RelationshipEntity;
import org.springframework.data.neo4j.annotation.StartNode;

@RelationshipEntity(type="IS_NEEDED_FOR")
public class Incredient extends AbstractPersistable{
	
	@Fetch
	@StartNode
    private Recipe recipe;
	
	@Fetch
	@EndNode
    private Article article;
    
    private double amount;
    
    public Incredient(){
    	
    }

	public Incredient(Article article, Recipe recipe, double amount) {
		super();
		this.article = article;
		this.recipe = recipe;
		this.amount = amount;
	}

	public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}

	public Recipe getRecipe() {
		return recipe;
	}

	public void setRecipe(Recipe recipe) {
		this.recipe = recipe;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
    
	@Override
	public String toString() {
		return String.format("Incredient{name='%s', price=%s, recipe=%s}", article.getName(), String.valueOf(amount * article.getPrice()), recipe.getName());
	}
    
    
}
