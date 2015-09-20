package com.pep1.kitchenoffice.data;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.RelationshipEntity;
import org.springframework.data.neo4j.annotation.StartNode;

@RelationshipEntity
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Ingredient extends AbstractPersistable {

    @Fetch
    @StartNode
    private Recipe recipe;

    @Fetch
    @EndNode
    private Article article;

    private double amount;

    public Ingredient(Article article, Recipe recipe, double amount) {
        super();
        this.article = article;
        this.recipe = recipe;
        this.amount = amount;
    }
}
