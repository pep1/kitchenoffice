package com.pep1.kitchenoffice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * Created by pepone on 19/10/14.
 */
@Configuration
public class QuartzConfig {

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setConfigLocation(new ClassPathResource("config/quartz.properties"));
        factory.setWaitForJobsToCompleteOnShutdown(true);
        factory.setApplicationContextSchedulerContextKey("applicationContext");
        return factory;
    }
}
