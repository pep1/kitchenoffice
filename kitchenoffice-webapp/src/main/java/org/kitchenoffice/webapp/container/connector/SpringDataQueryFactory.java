package org.kitchenoffice.webapp.container.connector;

import org.kitchenoffice.data.domain.AbstractPersistable;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.vaadin.addons.lazyquerycontainer.Query;
import org.vaadin.addons.lazyquerycontainer.QueryDefinition;
import org.vaadin.addons.lazyquerycontainer.QueryFactory;

public class SpringDataQueryFactory<A extends GraphRepository<? extends AbstractPersistable>>
		implements QueryFactory {

	private A repository;

	private QueryDefinition definition;

	private Class<? extends AbstractPersistable> pojoClass;

	public SpringDataQueryFactory(A repository, Class<? extends AbstractPersistable> pojoClass) {
		super();
		this.repository = repository;
		this.pojoClass = pojoClass;
	}

	@Override
	public Query constructQuery(Object[] sortPropertyIds, boolean[] sortStates) {
		return new SpringDataQuery<A>(repository, pojoClass, definition,
				sortPropertyIds, sortStates);
	}

	@Override
	public void setQueryDefinition(QueryDefinition definition) {
		this.definition = definition;
	}

}
