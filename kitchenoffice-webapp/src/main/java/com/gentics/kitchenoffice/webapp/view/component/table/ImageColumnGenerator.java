package com.gentics.kitchenoffice.webapp.view.component.table;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.gentics.kitchenoffice.data.Recipe;
import com.gentics.kitchenoffice.webapp.util.WebappHelper;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;

@Component
@Scope("prototype")
public class ImageColumnGenerator implements ColumnGenerator{
	
	private static Logger log = Logger.getLogger(ImageColumnGenerator.class);
	
	@Autowired
	private WebappHelper helper;

	@Override
	public Object generateCell(Table source, Object itemId, Object columnId) {
		
		Embedded embed = new Embedded();
		embed.setStyleName("thumbnail40");
		
		if(itemId instanceof Recipe) {
			embed.setSource(helper.getImageThumbnail(((Recipe)itemId).getImage(), 40));
		} else {
			embed.setSource(helper.getImageThumbnail(null , 40));
		}
		
		return embed;
	}

}
