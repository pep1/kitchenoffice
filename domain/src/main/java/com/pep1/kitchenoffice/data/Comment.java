package com.pep1.kitchenoffice.data;

import com.pep1.kitchenoffice.adapter.DateAdapter;
import com.pep1.kitchenoffice.data.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.GraphProperty;
import org.springframework.data.neo4j.annotation.NodeEntity;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;

@NodeEntity
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Comment extends AbstractPersistable {

    @XmlJavaTypeAdapter(DateAdapter.class)
    @GraphProperty(propertyType = Long.class)
    @XmlAttribute(name = "timeStamp")
    @NotNull
    private Date timeStamp;

    @Fetch
    @NotNull
    private User user;

    @NotBlank(message = "Comment text should not be blank")
    @Size(min = 2, message = "Comment should have at least 2 characters")
    private String text;

    public Comment(User user, String text) {
        super();
        this.timeStamp = new Date();
        this.user = user;
        this.text = text;
    }
}
