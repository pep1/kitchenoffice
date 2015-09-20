package com.pep1.kitchenoffice.data;

import com.pep1.kitchenoffice.adapter.DateAdapter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.neo4j.annotation.GraphProperty;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.support.index.IndexType;
import org.springframework.util.Assert;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;

@NodeEntity
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Tag extends AbstractPersistable {

    @XmlJavaTypeAdapter(DateAdapter.class)
    @GraphProperty(propertyType = Long.class)
    @XmlAttribute(name = "timstamp")
    private Date timeStamp = new Date();

    @Indexed(indexType = IndexType.FULLTEXT, indexName = "tagnamesearch")
    private String name;

    public Tag(String tagName) {
        Assert.hasText(tagName);

        timeStamp = new Date();
        name = tagName;
    }

}
