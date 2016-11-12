package dev.factory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import dev.bean.BeanDefinition;

public abstract class AbstractBeanFactory implements BeanFactory {
    private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String, BeanDefinition>();

    public Object getBean(String beanName) {
        return this.beanDefinitionMap.get(beanName).getBean();
    }

    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        beanDefinition = GetCreatedBean(beanDefinition);
        this.beanDefinitionMap.put(beanName, beanDefinition);
    }

    protected abstract BeanDefinition GetCreatedBean(BeanDefinition beanDefinition);
}
