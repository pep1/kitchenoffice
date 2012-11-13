package com.gentics.kitchenoffice.webapp.container;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.data.neo4j.conversion.EndResult;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.gentics.kitchenoffice.data.AbstractPersistable;
import com.gentics.kitchenoffice.webapp.container.util.ItemChangedEvent;
import com.gentics.kitchenoffice.webapp.container.util.ItemChangedListener;
import com.gentics.kitchenoffice.webapp.container.util.Watcher;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;


@SuppressWarnings("serial")
@Component
@Scope("prototype")
public class SpringDataBeanItemContainer<A extends AbstractPersistable> extends
		BeanItemContainer<A> implements ItemChangedListener {

	private static Logger log = Logger
			.getLogger(SpringDataBeanItemContainer.class);

	private GraphRepository<A> repository;
	
	@Autowired
	private ApplicationContext context;

	private Class<? extends GraphRepository<A>> repositoryClazz;
	
	private Watcher watcher = new Watcher();

	private List<A> toSave = new ArrayList<A>();
	private List<A> toRemove = new ArrayList<A>();

	public SpringDataBeanItemContainer(Class<? super A> type)
			throws IllegalArgumentException {
		super(type);
	}

	public SpringDataBeanItemContainer(Class<A> type, Class<? extends GraphRepository<A>> repName)
			throws IllegalArgumentException {
		super(type);

		Assert.notNull(repName, "repository name may not be null");
		
		this.repositoryClazz = repName;
	}
	
	@PostConstruct
    public void PostConstruct() throws InstantiationException{
		
		// get right repository bean
		repository = context.getBean(repositoryClazz);
		
		// on initialize fill container
		refresh();
				
		watcher.addListener(this);
		
	}

	public void refresh() {

		this.removeAllItems();

		EndResult<A> result = repository.findAll();

		for (A a : result) {
			this.addItem(a);
		}

	}

	public BeanItem<A> addItem(A itemId) {

		BeanItem<A> item = addItem((Object) itemId);

		if (itemId.isNew()) {
			toSave.add(itemId);
		}

		watcher.watch(item);

		return item;
	}

	public boolean removeItem(A itemId) {
		boolean value = removeItem((Object) itemId);

		if (value && !itemId.isNew()) {
			toRemove.add(itemId);
		}

		return value;
	}

	public void commit() {

		long start = System.currentTimeMillis();

		repository.delete(toRemove);
		repository.save(toSave);

		int amount = toRemove.size() + toSave.size();

		long time = System.currentTimeMillis() - start;
		log.debug("Commit of " + amount + " took " + time + " ms");

		toRemove.clear();
		toSave.clear();

		refresh();
	}

	@Override
	public void itemChanged(ItemChangedEvent event) {

		BeanItem<A> item = (BeanItem<A>) event.getItem();

		if (!toSave.contains(item.getBean())) {
			toSave.add(item.getBean());
		}
	}

	public A getItemById(Long id) {

		for (A item : this.getItemIds()) {

			if (item.getId() == id) {
				return item;
			}
		}

		return null;
	}

}
