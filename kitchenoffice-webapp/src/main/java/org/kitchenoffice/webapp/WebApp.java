package org.kitchenoffice.webapp;

import org.apache.log4j.Logger;
import org.dellroad.stuff.vaadin.SpringContextApplication;
import org.kitchenoffice.data.domain.Article;
import org.kitchenoffice.data.domain.Recipe;
import org.kitchenoffice.data.repository.ArticleRepository;
import org.kitchenoffice.data.repository.CommentRepository;
import org.kitchenoffice.data.repository.MealRepository;
import org.kitchenoffice.data.repository.RecipeRepository;
import org.kitchenoffice.data.repository.UserRepository;
import org.kitchenoffice.webapp.container.connector.SpringDataQueryFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.data.neo4j.support.node.Neo4jHelper;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.vaadin.addons.lazyquerycontainer.LazyQueryContainer;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
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
	private ArticleRepository articleRepository;

	@Autowired
	private MealRepository mealRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private Neo4jTemplate template;

	private Window window;

	private static Logger log = Logger
			.getLogger("org.kitchenoffice.webapp.WebApp");

	public WebApp() {

	}

	@Override
	protected void initSpringApplication(ConfigurableWebApplicationContext arg0) {

		log.debug("initializing Webapp");
		
		setTheme("runo");

		window = new Window("Spring Neo4j Test WebApp");
		setMainWindow(window);

		log.debug("cleaning up database");
		cleanUpGraph();
		

		log.debug("creating new articles");
		for (int i = 0; i < 50; i++) {
			createSomeRecipes();
		}
		
		SpringDataQueryFactory<RecipeRepository> queryDefinition = new SpringDataQueryFactory<RecipeRepository>(
				recipeRepository, Recipe.class);
		final LazyQueryContainer container = new LazyQueryContainer(
				queryDefinition, false, 50);

		final Table table = new Table();

		table.setContainerDataSource(container);

		table.addContainerProperty("id", String.class, "");
		table.addContainerProperty("name", String.class, "");
		table.addContainerProperty("description", String.class, "");
		table.addContainerProperty("totalPrice", String.class, "");
		table.setSizeFull();

		window.addComponent(table);
		table.setSizeFull();

		table.setWriteThrough(true);

		final Button edit = new Button("Edit");
		edit.addListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				table.setEditable(true);
				edit.setEnabled(false);
			}
		});

		final Button save = new Button("Save");
		save.addListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				table.commit();
				container.commit();
				table.setEditable(false);
				edit.setEnabled(true);
			}
		});

		final Button cancel = new Button("Cancel");
		cancel.addListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				table.discard();
				container.discard();
				table.setEditable(false);
				edit.setEnabled(true);
			}
		});

		window.addComponent(edit);
		window.addComponent(save);
		window.addComponent(cancel);

	}

	private void createSomeRecipes() {

		Recipe r = new Recipe("Tortellini mit Tomatensoße", "lecker", 1, 5);
		recipeRepository.save(r);

		Article i1 = new Article("Tortellini Pasta", "Package, 500g", 0.89);
		articleRepository.save(i1);
		Article i2 = new Article("Tomaten in der Dose, gestückelt",
				"Dose, 400ml", 2.0);
		articleRepository.save(i2);
		Article i3 = new Article("Zwiebeln", "Package", 0.89);
		articleRepository.save(i3);
		Article i4 = new Article("Parmesan, Grana", "Package", 0.89);
		articleRepository.save(i4);

		// saving after each adding seems to be necessary
		r.addArticle(i1, 1);
		recipeRepository.save(r);
		r.addArticle(i2, 2);
		recipeRepository.save(r);
		r.addArticle(i3, 0.5);
		recipeRepository.save(r);
		r.addArticle(i4, 0.5);
		recipeRepository.save(r);

		Recipe r2 = new Recipe("Nudeln mit Tomatensoße", "lecker", 1, 10);

		Article i5 = new Article("Nudeln Pasta, irgendeine Sorte",
				"Package, 500g", 0.89);
		articleRepository.save(i5);

		r2.addArticle(i5, 1);
		recipeRepository.save(r2);
		r2.addArticle(i2, 1);
		recipeRepository.save(r2);
		r2.addArticle(i3, 1);
		recipeRepository.save(r2);
		r2.addArticle(i4, 1);
		recipeRepository.save(r2);

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
