package com.gentics.kitchenoffice;

import java.util.HashSet;
import java.util.Set;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.RelatedTo;

class Auto {

     @Fetch
        @RelatedTo(type = "TAG", direction = Direction.INCOMING)
        private Set<Tag> tags = new HashSet<Tag>();

	/*
         * @param name
         * @return the created tag
         */
        public Tag addTag(String name) {
                Tag newTag = new Tag(name);
                this.tags.add(newTag);
                return newTag;
        }
}
