package com.pep1.kitchenoffice.data;

import com.pep1.kitchenoffice.data.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.*;
import org.springframework.data.neo4j.support.index.IndexType;
import org.springframework.util.Assert;

import java.util.HashSet;
import java.util.Set;

@NodeEntity
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Recipe extends AbstractPersistable {

    @Indexed(indexType = IndexType.FULLTEXT, indexName = "recipenamesearch")
    private String name;

    private String description;

    private String text;

    private int prepTime;

    private int maxPersons;

    @Fetch
    @RelatedTo(type = "IS_CREATED_BY", direction = Direction.BOTH, elementClass = User.class)
    private User creator = new User();

    @Fetch
    @RelatedTo(type = "HAS_IMAGE", direction = Direction.BOTH, elementClass = Image.class)
    private Image image = new Image();

    @Fetch
    @RelatedToVia(type = "IS_NEEDED_FOR", direction = Direction.BOTH, elementClass = Ingredient.class)
    private Set<Ingredient> incredients = new HashSet<Ingredient>();

    @Fetch
    @RelatedTo(type = "RECIPE_HAS_COMMENT", direction = Direction.BOTH, enforceTargetType = true, elementClass = Comment.class)
    private Set<Comment> comments = new HashSet<Comment>();

    @Fetch
    @RelatedTo(type = "HAS_TAG", direction = Direction.BOTH, enforceTargetType = true, elementClass = Tag.class)
    private Set<Tag> tags = new HashSet<Tag>();


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

}
