package com.pep1.kitchenoffice.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.annotation.GraphId;

import java.io.Serializable;


@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class AbstractPersistable implements Serializable {

    @GraphId
    @Getter
    @Setter(AccessLevel.PROTECTED)
    private Long id;

    @JsonIgnore
    public boolean isNew() {

        return null == getId();
    }

    @Override
    public int hashCode() {

        int hashCode = 17;

        hashCode += null == getId() ? 0 : getId().hashCode() * 31;

        return hashCode;

    }

    @Override
    public boolean equals(Object obj) {

        if (null == obj) {
            return false;
        }

        if (this == obj) {
            return true;
        }

        if (!getClass().equals(obj.getClass())) {
            return false;
        }

        AbstractPersistable that = (AbstractPersistable) obj;

        return null == this.getId() ? false : this.getId().equals(that.getId());
    }
}
