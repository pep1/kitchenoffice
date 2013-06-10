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

import com.gentics.kitchenoffice.data.user.User;

@NodeEntity
public class Recipe extends AbstractPersistable{
    
    @Indexed
    private String name;
    
    @Indexed
    private String description;
    
    private String text;
    
    private int prepTime;
    
    private int maxPersons;
    
    @Fetch
    @RelatedTo(type = "IS_CREATED_BY", direction = Direction.BOTH)
	private User creator = new User();
    
    @Fetch
    @RelatedTo(type = "HAS_IMAGE", direction = Direction.BOTH)
	private Image image = new Image();

    @Fetch
    @RelatedToVia(type = "IS_NEEDED_FOR", direction = Direction.BOTH)
    private Set<Ingredient> incredients = new HashSet<Ingredient>();
    
    @Fetch
    @RelatedTo(type = "RECIPE_HAS_COMMENT", direction = Direction.BOTH)
	private Set<Comment> comments = new HashSet<Comment>();
    
    @Fetch
    @RelatedTo(type = "HAS_TAG", direction = Direction.BOTH)
	private Set<Tag> tags = new HashSet<Tag>();
    
    public Recipe() {
   
    }

	public Recipe(String name, String description,
			int maxPersons) {
		super();
		this.name = name;
		this.description = description;
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

	public User getCreator() {
		return creator;
	}

	public void setCreator(User user) {
		this.creator = user;
	}

	public Set<Ingredient> getIncredients() {
		return incredients;
	}

	public void setIncredients(Set<Ingredient> incredients) {
		this.incredients = incredients;
	}
	
	public Set<Comment> getComments() {
		return comments;
	}

	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}

	public Set<Tag> getTags() {
		return tags;
	}

	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}

	public Ingredient addArticle(Article article, double amount) {
		
		Assert.notNull(article, "article may not be null");
		
		Ingredient in = new Ingredient(article, this, amount);
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
		
		for (Ingredient incredient : incredients) {
			price += incredient.getArticle().getPrice() * incredient.getAmount();
		}
		
		return price;
	}
	
	@Override
	public String toString() {
		return String.format("Recipe{\n  name='%s',\n  incredients=%s,\n  price=%f\n, comments=%s\n}", name, incredients.toString(), getTotalPrice(), comments.toString());
	}

	
}
