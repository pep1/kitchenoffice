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
import org.springframework.util.Assert;

import com.gentics.kitchenoffice.data.user.User;
import lombok.Getter;
import lombok.Setter;

@NodeEntity
public class Recipe extends AbstractPersistable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6500169279341515356L;

	@Indexed(indexType = IndexType.FULLTEXT, indexName = "recipenamesearch")
	@Getter @Setter
	private String name;

	@Getter @Setter
	private String description;

	@Getter @Setter
	private String text;

	@Getter @Setter
	private int prepTime;

	@Getter @Setter
	private int maxPersons;

	@Fetch
	@RelatedTo(type = "IS_CREATED_BY", direction = Direction.BOTH, elementClass = User.class)
	@Getter @Setter
	private User creator = new User();

	@Fetch
	@RelatedTo(type = "HAS_IMAGE", direction = Direction.BOTH, elementClass = Image.class)
	@Getter @Setter
	private Image image = new Image();

	@Fetch
	@RelatedToVia(type = "IS_NEEDED_FOR", direction = Direction.BOTH, elementClass = Ingredient.class)
	@Getter @Setter
	private Set<Ingredient> incredients = new HashSet<Ingredient>();

	@Fetch
	@RelatedTo(type = "RECIPE_HAS_COMMENT", direction = Direction.BOTH, enforceTargetType = true, elementClass = Comment.class)
	@Getter @Setter
	private Set<Comment> comments = new HashSet<Comment>();

	@Fetch
	@RelatedTo(type = "HAS_TAG", direction = Direction.BOTH, enforceTargetType = true, elementClass = Tag.class)
	@Getter @Setter
	private Set<Tag> tags = new HashSet<Tag>();

	public Recipe() {

	}

	public Recipe(String name, String description, int maxPersons) {
		this.name = name;
		this.description = description;
		this.maxPersons = maxPersons;

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
		return String.format("Recipe{\n  name='%s',\n  incredients=%s,\n  price=%f\n, comments=%s\n}", name,
				incredients.toString(), getTotalPrice(), comments.toString());
	}

}
