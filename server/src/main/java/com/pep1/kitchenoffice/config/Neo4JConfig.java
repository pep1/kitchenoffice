package com.pep1.kitchenoffice.config;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.config.Neo4jConfiguration;

/**
 * Created by pepone on 20/09/15.
 */
@Configuration
@EnableNeo4jRepositories(basePackages = "com.pep1.kitchenoffice.repository")
public class Neo4JConfig extends Neo4jConfiguration {

    private String location;

    public Neo4JConfig() {
        setBasePackage("com.pep1.kitchenoffice.data");
    }

    @Bean
    GraphDatabaseService graphDatabaseService() {
        return new GraphDatabaseFactory().newEmbeddedDatabase("/tmp/kitchenoffice/db");
    }
}