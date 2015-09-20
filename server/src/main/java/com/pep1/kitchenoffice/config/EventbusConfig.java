package com.pep1.kitchenoffice.config;

import com.google.common.eventbus.EventBus;
import com.pep1.kitchenoffice.guava.EventBusPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by pepone on 20/09/15.
 */
@Configuration
public class EventbusConfig {

    @Bean(name = "eventBus")
    public EventBus eventBus() {
        return new EventBus();
    }

    @Bean(name = "eventBusPostProcessor")
    public EventBusPostProcessor eventBusPostProcessor() {
        return new EventBusPostProcessor();
    }

}
