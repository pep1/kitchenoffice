package com.gentics.kitchenoffice.data;

import java.io.Serializable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.data.neo4j.annotation.GraphId;


@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class AbstractPersistable implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3244769429406745303L;
	
	@GraphId
	@Getter @Setter(AccessLevel.PROTECTED)
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
