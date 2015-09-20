package com.pep1.kitchenoffice.guava;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * EventBusPostProcessor registers Spring beans with EventBus. All beans
 * containing Guava's @Subscribe annotation are registered.
 *
 * @author pmeade
 */
public class EventBusPostProcessor implements BeanPostProcessor {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private EventBus eventBus;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        // for each method in the bean
        Method[] methods = bean.getClass().getMethods();
        for (Method method : methods) {
            // check the annotations on that method
            Annotation[] annotations = method.getAnnotations();
            for (Annotation annotation : annotations) {
                // if it contains the Subscribe annotation
                if (annotation.annotationType().equals(Subscribe.class)) {
                    // register it with the event bus
                    eventBus.register(bean);
                    log.debug("Bean {} containing method {} was subscribed to {}",
                            beanName, method.getName(), EventBus.class.getCanonicalName());
                    // we only need to register once
                    return bean;
                }
            }
        }

        return bean;
    }
}
