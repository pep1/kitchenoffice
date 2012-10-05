package com.gentics.kitchenoffice.rest;

import java.util.Set;

import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig.Feature;
import org.codehaus.jackson.map.introspect.JacksonAnnotationIntrospector;
import org.codehaus.jackson.xc.JaxbAnnotationIntrospector;

import com.sun.jersey.api.core.PackagesResourceConfig;

public class JerseyConfiguration extends PackagesResourceConfig {
    /**
     * Create an instance of the REST Appplication
     */
    public JerseyConfiguration() {
        // tell Jersey in which package the resources can be found
        super("com.gentics.kitchenoffice.rest.resource");
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sun.jersey.api.core.DefaultResourceConfig#getSingletons()
     */
    public Set<Object> getSingletons() {
        Set<Object> s = super.getSingletons();

        // create and configure the object mapper for mapping POJO <-> JSON
        // (which is done by Jackson)
        ObjectMapper mapper = new ObjectMapper();

        // we want support for JAXB Annotations and Jackson Annotations
        AnnotationIntrospector primary = new JaxbAnnotationIntrospector();
        AnnotationIntrospector secondary = new JacksonAnnotationIntrospector();
        AnnotationIntrospector pair = new AnnotationIntrospector.Pair(primary, secondary);
        mapper.getDeserializationConfig().setAnnotationIntrospector(pair);
        mapper.getSerializationConfig().setAnnotationIntrospector(pair);
        mapper.getSerializationConfig().enable(Feature.INDENT_OUTPUT);

        // Set up the provider
        JacksonJaxbJsonProvider jaxbProvider = new JacksonJaxbJsonProvider();
        jaxbProvider.setMapper(mapper);

        // Return the provider, so Jersey will use it for serialization and
        // deserialization of JSON
        s.add(jaxbProvider);
        return s;
    }
}
