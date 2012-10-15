package com.gentics.kitchenoffice.webapp.container;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.neo4j.conversion.EndResult;
import org.springframework.stereotype.Component;

import com.gentics.kitchenoffice.data.Recipe;
import com.gentics.kitchenoffice.repository.RecipeRepository;
import com.gentics.kitchenoffice.webapp.container.util.ItemChangedEvent;
import com.gentics.kitchenoffice.webapp.container.util.ItemChangedListener;
import com.gentics.kitchenoffice.webapp.container.util.Watcher;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;


@Component
@Scope("session")
public class RecipeContainer extends BeanItemContainer<Recipe> implements ItemChangedListener{
	
	private static Logger log = Logger.getLogger(RecipeContainer.class);

	@Autowired
	RecipeRepository repository;
	
	private Watcher watcher = new Watcher();
	
	List<Recipe> toSave = new ArrayList<Recipe>();
	List<Recipe> toRemove = new ArrayList<Recipe>();

	public RecipeContainer() throws IllegalArgumentException {
		super(Recipe.class);
	}
	
	@PostConstruct
    public void PostConstruct() throws SecurityException, NoSuchMethodException {
		
		refresh();
		
		watcher.addListener(this);
	}
	
	public void refresh() {
		
		this.removeAllItems();
		
		EndResult<Recipe> result = repository.findAll();
		
		for (Recipe a : result) {
			this.addItem(a);
		}
		
	}
	
	
	public BeanItem<Recipe> addItem(Recipe itemId) {
        BeanItem<Recipe> item =  addItem((Object) itemId);
        
        if(itemId.isNew()) {
        	toSave.add(itemId);
        }
        
        watcher.watch(item);
        
        return item;
    }

    public boolean removeItem(Recipe itemId) {
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
		
		BeanItem<Recipe> item = (BeanItem<Recipe>) event.getItem();
		
		if(!toSave.contains(item.getBean())) {
			toSave.add(item.getBean());
		}
	}

		
	

}
