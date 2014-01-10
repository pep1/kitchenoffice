package com.gentics.kitchenoffice.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.view.feed.AbstractRssFeedView;

import com.gentics.kitchenoffice.data.event.Feedable;
import com.sun.syndication.feed.rss.Channel;
import com.sun.syndication.feed.rss.Content;
import com.sun.syndication.feed.rss.Item;

public class RSSEventViewer extends AbstractRssFeedView {

	private static Logger log = LoggerFactory.getLogger(RSSEventViewer.class);

	public static final String FEED_CONTENT_KEY = "feedContent";

	@PostConstruct
	public void initialize() {
		log.debug("initializing " + this.getClass().getSimpleName() + " instance ...");
	}

	@Override
	protected void buildFeedMetadata(Map<String, Object> model, Channel feed, HttpServletRequest request) {

		feed.setTitle("Kitchenoffice Event Feed");
		feed.setDescription("Experimental Food Organization for your Office!");
		feed.setLink("http://food.office");

		super.buildFeedMetadata(model, feed, request);
	}

	@Override
	protected List<Item> buildFeedItems(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		@SuppressWarnings("unchecked")
		List<Feedable> listContent = (List<Feedable>) model.get(FEED_CONTENT_KEY);
		List<Item> items = new ArrayList<Item>(listContent.size());

		for (Feedable tempContent : listContent) {

			Item item = new Item();

			Content content = new Content();
			content.setValue(tempContent.getContent());
			item.setContent(content);

			item.setTitle(tempContent.getTitle());
			item.setLink("http://" + request.getServerName() + request.getContextPath() + tempContent.getLink());
			item.setPubDate(tempContent.getPubDate());

			items.add(item);
		}

		return items;
	}

}