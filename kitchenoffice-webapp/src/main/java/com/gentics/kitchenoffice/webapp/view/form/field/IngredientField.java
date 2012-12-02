package com.gentics.kitchenoffice.webapp.view.form.field;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;

import com.gentics.kitchenoffice.data.Article;
import com.gentics.kitchenoffice.data.Incredient;
import com.gentics.kitchenoffice.data.Recipe;
import com.gentics.kitchenoffice.service.ArticleService;
import com.gentics.kitchenoffice.webapp.container.ArticleSelectContainer;
import com.gentics.kitchenoffice.webapp.view.form.ArticleForm;
import com.gentics.kitchenoffice.webapp.view.form.field.ui.IngredientFieldUi;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@org.springframework.stereotype.Component
@Scope("prototype")
public class IngredientField extends CustomField<Set<Incredient>> implements ClickListener {

	private static Logger log = Logger.getLogger(IngredientField.class);
	
	private Recipe recipe;
	
	@Autowired
	private IngredientFieldUi layout;
	
	@Autowired
	private ArticleSelectContainer articeSelectContainer;
	
	@Autowired
	private ArticleService articleService;
	
	@Autowired
	private ApplicationContext context;
	
	private ArticleForm articleForm;
	private Window addWindow;
	
	public IngredientField() {
	}

	@Override
	protected Component initContent() {
		
		layout.setSpacing(true);
		layout.setSizeFull();
		
		return layout;
	}
	
	@PostConstruct
	public void postConstruct() {
		// add new article
		layout.getAddComponent().getAddButton().addClickListener(this);
		
		// add new Ingredient
		layout.getAddComponent().getSaveButton().addClickListener(this);
		
		ComboBox input = layout.getAddComponent().getInput();
		input.setContainerDataSource(articeSelectContainer);
		input.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		input.setItemCaptionPropertyId("name");
		
	}
	
	private void fillLayout(boolean refreshArticles) {
		
		Set<Incredient> set = getValue();
		
		long start = System.currentTimeMillis();

		VerticalLayout container = layout.getIngredientContainer();
		container.removeAllComponents();
		
		if (set != null) {
			for (Incredient item : set) {
				
				if(refreshArticles) {
					item.setArticle(articleService.refresh(item.getArticle()));
				}
				
				final IngredientFieldSlot slot = (IngredientFieldSlot)context.getBean("IngredientFieldSlot", item);
				
				slot.getComponent().getEditButton().addClickListener(new ClickListener() {
					
					@Override
					public void buttonClick(ClickEvent event) {
						openArticleAddWindow(slot.getIngredient().getArticle());
					}
				});
				
				slot.getComponent().getRemoveButton().addClickListener(new ClickListener() {
					
					@Override
					public void buttonClick(ClickEvent event) {
						removeIngredient(slot.getIngredient());
					}
				});
				
				container.addComponent(slot);
			}
		}
		
		long end = System.currentTimeMillis() - start;
		log.debug("Filling Layout with " + getValue().size() + " Elements took " + end + " ms");
	}

	@Override
	public void setInternalValue(Set<Incredient> set) {
		super.setInternalValue(set);
		
		layout.getAddComponent().reset();

		if(getUI() != null) {
			fillLayout(false);
		}
	}
	
	@Override
	public Class getType() {
		return Set.class;
	}
	
	@Override
	public void setReadOnly(boolean readOnly) {
		super.setReadOnly(readOnly);
		
		Iterator<Component> it = layout.getIngredientContainer().getComponentIterator();
		
		while(it.hasNext()) {
			IngredientFieldSlot slot = (IngredientFieldSlot) it.next();
			slot.setReadOnly(readOnly);
		}
		
		if(readOnly) {
			layout.getAddComponent().setVisible(false);
		}else {
			layout.getAddComponent().setVisible(true);
		}
	}
	
	@Override
	public void commit(){
		
		Set<Incredient> newValues = new HashSet<Incredient>();
		
		// FIXME: doesnt work
		// replace values with possible changed input values
		for(Incredient in : getValue()) {
			
			Iterator<Component> it = layout.getIngredientContainer().getComponentIterator();
			
			while(it.hasNext()) {
				
				IngredientFieldSlot slot = (IngredientFieldSlot) it.next();
				
				if(in.equals(slot.getIngredient())) {
					in.setAmount(Double.parseDouble(slot.getComponent().getAmount().getValue()));
					break;
				}
			}
			newValues.add(in);
		}
		
		setValue(newValues);
		
		super.commit();
		
	}
	
	private void openArticleAddWindow(Article article) {
		
		String caption;
		
		if(article == null) {
			article = new Article();
			caption = "Add Article";
		} else {
			caption = "Edit Article";
		}
		
		articleForm = context.getBean(ArticleForm.class);
		
		addWindow = new Window(caption);
		addWindow.center();
		addWindow.setModal(true);
		
		BeanItem<Article> item = new BeanItem<Article>(article);
		
		articleForm.setItemDataSource(item);
		articleForm.getLayout().getCancel().addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				addWindow.close();
			}
		});
		
		articleForm.getLayout().getSave().addClickListener(this);
		addWindow.setContent(articleForm.getLayout());
		
		getUI().addWindow(addWindow);
		
	}

	private void saveNewArticle() {
		
		if(articleForm.isValid()) {
			log.debug("Saving Article");
			
			try {
				articleForm.commit();
			} catch (CommitException e) {
				log.error("something went wrong while comitting article form");
				e.printStackTrace();
			}
			
			BeanItem<Article> articleToSave = (BeanItem<Article>) articleForm.getItemDataSource();
			
			// in case the user edited an existing article
			boolean refreshIngredientlist = false;
			if(!articleToSave.getBean().isNew()) {
				refreshIngredientlist = true;
			}
			
			articleService.save(articleToSave.getBean());
			articeSelectContainer.refresh();
			if(refreshIngredientlist) {
				fillLayout(true);
			}
			
			addWindow.close();
		}
		
	}
	
	private void removeIngredient(Incredient in) {
		this.getValue().remove(in);
		
		fillLayout(false);
	}
	

	private void addNewIngredient() {
		
		if(layout.getAddComponent().getInput().getValue() != null &&
				layout.getAddComponent().getAmount().getValue() != null) {
			
			if(this.getValue().contains(layout.getAddComponent().getInput().getValue())) {
				Notification.show("Article already added", Notification.Type.WARNING_MESSAGE);
			}
			
			try{
				Article article = (Article) layout.getAddComponent().getInput().getValue();
				Double amount = Double.parseDouble(layout.getAddComponent().getAmount().getValue());
				
				Incredient in = new Incredient();
				in.setArticle(article);
				in.setAmount(amount);
				
				getValue().add(in);
				
				fillLayout(false);
				
				layout.getAddComponent().reset();
				
			} catch (NumberFormatException e) {
				Notification.show("Please specify correct amount number", Notification.Type.WARNING_MESSAGE);
			}
		} else {
			Notification.show("Please select an article and specify amount", Notification.Type.WARNING_MESSAGE);
		}
		
	}

	@Override
	public void buttonClick(ClickEvent event) {

		if(event.getButton() == layout.getAddComponent().getAddButton()) {
			openArticleAddWindow(null);
		} else if(event.getButton() == layout.getAddComponent().getSaveButton()) {
			addNewIngredient();
		} else if(articleForm != null && event.getButton() == articleForm.getLayout().getSave()) {
			saveNewArticle();
		} 
		
	}

	public void setRecipe(Recipe bean) {
		this.recipe = recipe;
	}
}
