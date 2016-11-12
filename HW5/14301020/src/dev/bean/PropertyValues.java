package dev.bean;

import java.util.ArrayList;
import java.util.List;

public class PropertyValues {
    private List<PropertyValue> propertyValues = new ArrayList<PropertyValue>();

    public void AddPropertyValue(PropertyValue propertyValue) {
        propertyValues.add(propertyValue);
    }

    public List<PropertyValue> GetPropertyValues() {
        return propertyValues;
    }
}
