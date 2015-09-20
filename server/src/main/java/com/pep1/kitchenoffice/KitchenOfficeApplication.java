package com.pep1.kitchenoffice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;

/**
 * Created by pepone on 20/09/15.
 */
@SpringBootApplication
@Slf4j
public class KitchenOfficeApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(KitchenOfficeApplication.class);
    }

    public static void main(String[] args) throws Exception {
        ApplicationContext context = SpringApplication.run(KitchenOfficeApplication.class, args);

        log.debug("Let's inspect the beans provided by Spring Boot:");

        String[] beanNames = context.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            log.debug(beanName);
        }
    }
}
