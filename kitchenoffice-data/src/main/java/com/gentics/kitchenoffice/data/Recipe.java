package com.gentics.kitchenoffice.data;

import java.util.HashSet;
import java.util.Set;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;
import org.springframework.data.neo4j.annotation.RelatedToVia;
import org.springframework.util.Assert;

@NodeEntity
public class Recipe extends AbstractPersistable{
    
    @Indexed
    private String name;
    
    @Indexed
    private String description;
    
    private String text;
    
    private int prepTime;
    
    private int minPersons;
    
    private int maxPersons;
    
    @Fetch
    @RelatedTo(type = "HAS_IMAGE", direction = Direction.OUTGOING)
	private Image image = new Image();

    @Fetch
    @RelatedToVia(type = "IS_NEEDED_FOR", direction = Direction.BOTH)
    private Set<Incredient> incredients = new HashSet<Incredient>();
    
    @Fetch
    @RelatedTo(type = "RECIPE_HAS_COMMENT", direction = Direction.BOTH)
	private Set<Comment> comments = new HashSet<Comment>();
    
    public Recipe() {
   
    }

	public Recipe(String name, String description, int minPersons,
			int maxPersons) {
		super();
		this.name = name;
		this.description = description;
		this.minPersons = minPersons;
		this.maxPersons = maxPersons;

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getPrepTime() {
		return prepTime;
	}

	public void setPrepTime(int prepTime) {
		this.prepTime = prepTime;
	}

	public int getMinPersons() {
		return minPersons;
	}

	public void setMinPersons(int minPersons) {
		this.minPersons = minPersons;
	}

	public int getMaxPersons() {
		return maxPersons;
	}

	public void setMaxPersons(int maxPersons) {
		this.maxPersons = maxPersons;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public Set<Incredient> getIncredients() {
		return incredients;
	}

	public void setIncredients(Set<Incredient> incredients) {
		this.incredients = incredients;
	}
	
	public Set<Comment> getComments() {
		return comments;
	}

	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}

	public Incredient addArticle(Article article, double amount) {
		
		Assert.notNull(article, "article may not be null");
		
		Incredient in = new Incredient(article, this, amount);
		this.incredients.add(in);
		
		return in;
	}
	
	public Comment addComment(User user, String comment) {
		Assert.notNull(user, "User may not be null!");
		Assert.hasLength(comment, "comment may not be null or empty!");
		
		Comment c = new Comment(user, comment);
		
		comments.add(c);
		
		return c;
	}
	
	public double getTotalPrice() {
		
		double price = 0;
		
		for (Incredient incredient : incredients) {
			price += incredient.getArticle().getPrice() * incredient.getAmount();
		}
		
		return price;
	}
	
	@Override
	public String toString() {
		return String.format("Recipe{\n  name='%s',\n  incredients=%s,\n  price=%f\n, comments=%s\n}", name, incredients.toString(), getTotalPrice(), comments.toString());
	}

	
}