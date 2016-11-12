package dev.factory;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import dev.bean.BeanDefinition;
import dev.bean.BeanUtil;
import dev.resource.Resource;
import dev.util.ReflectionUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import dev.bean.PropertyValue;
import dev.bean.PropertyValues;

public class XMLBeanFactory extends AbstractBeanFactory {

    private String xmlPath;

    public XMLBeanFactory(Resource resource) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();
            Document document = dbBuilder.parse(resource.getInputStream());
            NodeList beanList = document.getElementsByTagName("bean");
            for (int i = 0; i < beanList.getLength(); i++) {
                Node bean = beanList.item(i);
                BeanDefinition beandef = new BeanDefinition();
                String beanClassName = bean.getAttributes().getNamedItem("class").getNodeValue();
                String beanName = bean.getAttributes().getNamedItem("id").getNodeValue();

                beandef.setBeanClassName(beanClassName);

                try {
                    //the class has been set, so here we can get beanClass
                    Class<?> beanClass = Class.forName(beanClassName);
                    beandef.setBeanClass(beanClass);
                } catch (ClassNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                PropertyValues propertyValues = new PropertyValues();

                NodeList propertyList = bean.getChildNodes();
                for (int j = 0; j < propertyList.getLength(); j++) {
                    Node property = propertyList.item(j);
                    if (property instanceof Element) {
                        Element ele = (Element) property;

                        if (ele.getTagName().equals("property")) {
                            String name = ele.getAttribute("name");
                            Object value = ele.getAttribute("value");
                            //if this dependence injection is set by value
                            if (value.equals("")) {
                                value = ele.getAttribute("ref");
                                Object beRefedBean;
                                beRefedBean = this.getBean((String) value);
                                propertyValues.AddPropertyValue(new PropertyValue(name, beRefedBean, false));
                            } else {
                                Class<?> type = null;
                                try {
                                    type = beandef.getBeanClass().getDeclaredField(name).getType();
                                } catch (NoSuchFieldException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }

                                if (type == Integer.class) {
                                    value = Integer.parseInt((String) value);
                                }

                                propertyValues.AddPropertyValue(new PropertyValue(name, value, false));
                            }
                        }
                        //or if this dependence injection is set by constructor
                        else if (ele.getTagName().equals("constructor-arg")) {
                            String ref = ele.getAttribute("ref");
                            Object beRefedBean;
                            beRefedBean = this.getBean((String) ref);
                            propertyValues.AddPropertyValue(new PropertyValue(ref.toLowerCase(), beRefedBean, true));
                        }
                    }
                }
                beandef.setPropertyValues(propertyValues);

                //use parent's function to instant the bean class in BeanDef and put it into a Map
                this.registerBeanDefinition(beanName, beandef);
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected BeanDefinition GetCreatedBean(BeanDefinition beanDefinition) {

        //Instant a Bean class and add properties
        try {
            // set BeanClass for BeanDefinition
            Class<?> beanClass = beanDefinition.getBeanClass();
            Object bean = null;
            List<PropertyValue> fieldDefinitionList = beanDefinition.getPropertyValues().GetPropertyValues();
            List<Class<?>> constructorParamTypeList = new ArrayList<Class<?>>();
            for (PropertyValue propertyValue : fieldDefinitionList) {
                if (propertyValue.getIsConstructorParam()) {
                    try {
                        Field field = beanClass.getDeclaredField(propertyValue.getName());
                        Class<?> type = field.getType();
                        constructorParamTypeList.add(type);
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (constructorParamTypeList.isEmpty()) {
                //if all properties are injected by values
                // set Bean Instance for BeanDefinition
                bean = beanClass.newInstance();
            } else {
                //or some property is injected by constructor
                Constructor constructor;
                Class<?>[] classArray = new Class<?>[constructorParamTypeList.size()];
                constructor = ReflectionUtils.findConstructor(beanClass, constructorParamTypeList.toArray(classArray));

                Object[] objectArray = new Object[classArray.length];
                for (int i = 0; i < classArray.length; i++) {
                    Object object = classArray[i].newInstance();
                    objectArray[i] = object;
                }

                try {
                    // set Bean Instance for BeanDefinition
                    bean = constructor.newInstance(objectArray);
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }


            for (PropertyValue propertyValue : fieldDefinitionList) {
                if (!propertyValue.getIsConstructorParam()) {
                    //Add properties through this invoke method
                    BeanUtil.invokeSetterMethod(bean, propertyValue.getName(), propertyValue.getValue());
                }
            }

            //Set the Bean object to beanDefinition
            beanDefinition.setBean(bean);

            return beanDefinition;

        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

}
