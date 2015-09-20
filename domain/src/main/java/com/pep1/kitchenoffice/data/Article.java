package com.pep1.kitchenoffice.data;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.*;
import org.springframework.data.neo4j.support.index.IndexType;

import java.util.HashSet;
import java.util.Set;

@NodeEntity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Article extends AbstractPersistable {

    @Indexed(indexType = IndexType.FULLTEXT, indexName = "articlenamesearch")
    private String name;

    private String unit;

    private double price;

    @Fetch
    @RelatedTo(type = "HAS_IMAGE", direction = Direction.OUTGOING, elementClass = Image.class)
    private Image image = new Image();

    @Fetch
    @RelatedToVia(type = "IS_NEEDED_IN", direction = Direction.BOTH, elementClass = Ingredient.class)
    private Set<Ingredient> incredients = new HashSet<Ingredient>();

    public Article(String name, String unit, double price) {
        super();
        this.name = name;
        this.unit = unit;
        this.price = price;
    }
}
