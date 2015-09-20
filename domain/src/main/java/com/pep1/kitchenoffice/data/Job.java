package com.pep1.kitchenoffice.data;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;
import org.springframework.data.neo4j.support.index.IndexType;

@NodeEntity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Job extends AbstractPersistable {

    @Fetch
    @RelatedTo(type = "HAS_IMAGE", direction = Direction.OUTGOING, elementClass = Image.class)
    private Image image;

    @Indexed(indexType = IndexType.FULLTEXT, indexName = "jobnamesearch")
    private String name;

    private String description;

    public Job(Image image, String name, String description) {
        super();
        this.image = image;
        this.name = name;
        this.description = description;
    }
}
