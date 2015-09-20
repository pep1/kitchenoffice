package com.pep1.kitchenoffice.config;

import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ui.velocity.VelocityEngineFactoryBean;

/**
 * Created by pepone on 20/09/15.
 */
@Configuration
@Slf4j
public class VelocityConfig {

    @Bean
    public VelocityEngineFactoryBean velocityEngineFactoryBean() {
        VelocityEngineFactoryBean bean = new VelocityEngineFactoryBean();
        bean.setVelocityPropertiesMap(
                ImmutableMap.<String, Object>builder()
                        .put("resource.loader", "class")
                        .put("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader").build());
        return bean;
    }
}
