package org.kitchenoffice.webapp.container.connector;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.kitchenoffice.data.domain.AbstractPersistable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.vaadin.addons.lazyquerycontainer.Query;
import org.vaadin.addons.lazyquerycontainer.QueryDefinition;

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;

public class SpringDataQuery<A extends GraphRepository<? extends AbstractPersistable>> implements Query{
	
	private Logger log = Logger.getLogger(this.getClass());
	
	private QueryDefinition definition;
	
	private Class<? extends AbstractPersistable> pojoClass;
	
	private A repository;
	
	//private String criteria="";

	public SpringDataQuery(A repository, Class<? extends AbstractPersistable> pojoClass,
			QueryDefinition definition, Object[] sortPropertyIds,
			boolean[] sortStates) {
		
		this.repository = repository;
		this.definition = definition;
		this.pojoClass = pojoClass;
	}

	@Override
	public Item constructItem() {
		
		try {
			return new BeanItem<Object>(pojoClass.newInstance());
		} catch (InstantiationException e) {
			log.error("constructItem Error!\n" + e.getStackTrace());
		} catch (IllegalAccessException e) {
			log.error("constructItem Error!\n" + e.getStackTrace());
		}
		
		return null;
	}

	@Override
	public boolean deleteAllItems() {
		repository.deleteAll();
		return true;
	}

	@Override
	public List<Item> loadItems(int startIndex, int count) {

		int	page = startIndex/50;
		PageRequest p = new PageRequest(page, count);
		
		long start = System.currentTimeMillis();
		
		List<? extends AbstractPersistable> list = repository.findAll(p).getContent();
		List<Item> items=new ArrayList<Item>();
		
		for (Object object : list) {
			items.add(new BeanItem<Object>(object));
		}

		Long time = System.currentTimeMillis() - start;
		
		if(log.isDebugEnabled()) {
			log.debug("Loading " + count + " Objects took: " + time + "ms");
		}
		
		return items;
	}

	@Override
	public void saveItems(List<Item> addedItems, List<Item> modifiedItems, List<Item> removedItems) {
		
		long start = System.currentTimeMillis();
		
		List pojosToSave = new ArrayList();
        List pojosToRemove = new ArrayList();
        
        addPojosFromItemListToPojoList(addedItems, pojosToSave);
        addPojosFromItemListToPojoList(modifiedItems, pojosToSave);
        addPojosFromItemListToPojoList(removedItems, pojosToRemove);
        
        repository.save(pojosToSave);
        repository.delete(pojosToRemove);
        
        if(log.isDebugEnabled()) {
	        int itemsCount = modifiedItems.size() + removedItems.size() + addedItems.size();
	        Long time = System.currentTimeMillis() - start;
	        
	        log.debug("Saving " + itemsCount + " Objects took: " + time + "ms");
	    }
		
	}
	
	private void addPojosFromItemListToPojoList(List<Item> items, List pojos) {
        if (items != null) {
            for (Item item : items) {
                pojos.add(((BeanItem) item).getBean());
            }
        }
    }

	@Override
	public int size() {
		return (int)repository.count();
	}

}
