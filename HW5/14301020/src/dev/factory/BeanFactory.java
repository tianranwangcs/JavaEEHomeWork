package dev.factory;

import dev.bean.BeanDefinition;

public interface BeanFactory {
    Object getBean(String beanName);

    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);
}
