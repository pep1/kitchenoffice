package com.gentics.kitchenoffice.webservice;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.gentics.kitchenoffice.data.Tag;
import com.gentics.kitchenoffice.service.TagService;


@Component
@Scope("singleton")
@Path("/tags")
public class TagWebService {

	private static Logger log = Logger.getLogger(TagWebService.class);
	
	@Autowired
	private TagService tagService;
	
	@GET
	@PreAuthorize("hasRole('ROLE_USER')")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Tag> getTags(@QueryParam("page") Integer page, @QueryParam("size") Integer size, @QueryParam("search") String search) {

		log.debug("calling getTags");

		if (page == null) {
			page = 0;
		}
		if (size == null) {
			size = 25;
		}

		return tagService.findByNameLike(search, new PageRequest(page, size));
	}
	
	@POST
	@PreAuthorize("hasRole('ROLE_USER')")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Tag createOrUpdateTag(Tag tag) {
		log.debug("calling createOrUpdateTag");
		
		Assert.notNull(tag);
		Assert.hasText(tag.getName());
		
		return tagService.save(tag);
	}
}
