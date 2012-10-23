package com.gentics.kitchenoffice.webapp.container;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.neo4j.conversion.EndResult;
import org.springframework.stereotype.Component;

import com.gentics.kitchenoffice.data.Meal;
import com.gentics.kitchenoffice.repository.MealRepository;
import com.gentics.kitchenoffice.webapp.container.util.ItemChangedEvent;
import com.gentics.kitchenoffice.webapp.container.util.ItemChangedListener;
import com.gentics.kitchenoffice.webapp.container.util.Watcher;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;


@Component
@Scope("session")
public class MealContainer extends BeanItemContainer<Meal> implements ItemChangedListener{
	
	private static Logger log = Logger.getLogger(MealContainer.class);

	@Autowired
	MealRepository repository;
	
	private Watcher watcher = new Watcher();
	
	List<Meal> toSave = new ArrayList<Meal>();
	List<Meal> toRemove = new ArrayList<Meal>();

	public MealContainer() throws IllegalArgumentException {
		super(Meal.class);
	}
	
	@PostConstruct
    public void PostConstruct() throws SecurityException, NoSuchMethodException {
		
		refresh();
		
		watcher.addListener(this);
	}
	
	public void refresh() {
		
		this.removeAllItems();
		
		EndResult<Meal> result = repository.findAll();
		
		for (Meal a : result) {
			this.addItem(a);
		}
		
	}
	
	
	public BeanItem<Meal> addItem(Meal itemId) {
        BeanItem<Meal> item =  addItem((Object) itemId);
        
        if(itemId.isNew()) {
        	toSave.add(itemId);
        }
        
        watcher.watch(item);
        
        return item;
    }

    public boolean removeItem(Meal itemId) {
		boolean value = removeItem((Object)itemId);
		
		if(value && !itemId.isNew()) {
			toRemove.add(itemId);
		}
		
		return value;
	}	
    
    public void commit () {
    	
    	long start = System.currentTimeMillis();
    	
    	repository.delete(toRemove);
    	repository.save(toSave);
    	
    	int amount = toRemove.size() + toSave.size();
    	
    	long time = System.currentTimeMillis() - start;
    	log.debug("Commit of " + amount +  " took " + time + " ms");
    	
    	toRemove.clear();
    	toSave.clear();
    	
    	refresh();
    }

	@Override
	public void itemChanged(ItemChangedEvent event) {
		
		BeanItem<Meal> item = (BeanItem<Meal>) event.getItem();
		
		if(!toSave.contains(item.getBean())) {
			toSave.add(item.getBean());
		}
	}

		
	

}
