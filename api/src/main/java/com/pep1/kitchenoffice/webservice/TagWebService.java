/*
 * 
 */
package com.pep1.kitchenoffice.webservice;

import com.pep1.kitchenoffice.data.Tag;
import com.pep1.kitchenoffice.service.TagService;
import com.pep1.kitchenoffice.webservice.filter.CacheAnnotations.NoCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * The Class TagWebService.
 * <p>
 * Provides tag CRU functionality. Deletion of tags (cleanup) works
 * automatically when not used anymore.
 */
@RestController
@Scope("singleton")
@RequestMapping("/api/v1/tags")
@NoCache
public class TagWebService {

    /**
     * The log.
     */
    private static Logger log = LoggerFactory.getLogger(TagWebService.class);

    /**
     * The tag service.
     */
    @Autowired
    private TagService tagService;

    /**
     * Gets the tags.
     *
     * @param search the search
     * @return the tags
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(method = RequestMethod.GET)
    public List<Tag> getTags(Pageable pageable, @RequestParam("search") String search) {
        log.debug("calling getTags");
        return tagService.findByNameLike(search, pageable);
    }

    /**
     * Creates the or update tag.
     *
     * @param tag the tag
     * @return the tag
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(method = RequestMethod.POST)
    public Tag createOrUpdateTag(Tag tag) {
        log.debug("calling createOrUpdateTag");

        Assert.notNull(tag);
        Assert.hasText(tag.getName());

        return tagService.save(tag);
    }
}
