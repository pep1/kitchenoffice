package org.kitchenoffice.webapp;

import org.apache.log4j.Logger;
import org.dellroad.stuff.vaadin.SpringContextApplication;
import org.kitchenoffice.data.domain.Recipe;
import org.kitchenoffice.data.repository.RecipeRepository;
import org.kitchenoffice.webapp.container.connector.SpringDataQueryFactory;
import org.kitchenoffice.webapp.ui.form.RecipeForm;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.data.neo4j.support.node.Neo4jHelper;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.vaadin.addons.lazyquerycontainer.LazyQueryContainer;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.Table;
import com.vaadin.ui.Window;

/**
 * The Application's "main" class
 */
@SuppressWarnings("serial")
public class WebApp extends SpringContextApplication implements
		BeanFactoryAware, InitializingBean, DisposableBean {

	@Autowired
	private RecipeRepository recipeRepository;
	
	@Autowired
	private Neo4jTemplate template;

	private Window window;

	private static Logger log = Logger
			.getLogger("org.kitchenoffice.webapp.WebApp");
	
	private Table table = new Table();
	
	private RecipeForm form;

	public WebApp() {

	}

	@Override
	protected void initSpringApplication(ConfigurableWebApplicationContext context) {

		log.debug("initializing Webapp instance");
		
		setTheme("runo");

		window = new Window("Kitchenoffice WebApp");
		setMainWindow(window);
		
		SpringDataQueryFactory<RecipeRepository> queryDefinition = new SpringDataQueryFactory<RecipeRepository>(
				recipeRepository, Recipe.class);
		final LazyQueryContainer container = new LazyQueryContainer(
				queryDefinition, false, 50);
		
		table.setImmediate(true);

		table.setContainerDataSource(container);
		
		table.addContainerProperty("id", String.class, "");
		table.addContainerProperty("name", String.class, "");
		table.addContainerProperty("description", String.class, "");
		table.addContainerProperty("totalPrice", String.class, "");
		
		table.addListener(new Property.ValueChangeListener() {
            public void valueChange(ValueChangeEvent event) {
                Object id = table.getValue();
                form.setItemDataSource(id == null ? null : table
                        .getItem(id));
            }
        });
		
		table.setWriteThrough(true);
		table.setSelectable(true);
		table.setSizeFull();
		table.setHeight("200px");
		window.addComponent(table);
		
		form = new RecipeForm(container, table);
		form.setSizeFull();
		window.addComponent(form);

	}

	public void cleanUpGraph() {
		Neo4jHelper.cleanDb(template);
	}

	@Override
	public void destroy() throws Exception {
		log.debug("Webapp Instance Destroy called");

	}

	@Override
	public void setBeanFactory(BeanFactory arg0) throws BeansException {
		log.debug("setBeanFactory called");

	}

	@Override
	public void afterPropertiesSet() throws Exception {
		log.debug("afterPropertiesSet called");

	}

}
