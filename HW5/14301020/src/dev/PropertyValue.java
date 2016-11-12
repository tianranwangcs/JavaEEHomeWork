package dev.bean;

public class PropertyValue {
    public PropertyValue(String name, Object value,boolean isConstructorParam) {
        this.name = name;
        this.value = value;
        this.isConstructorParam=isConstructorParam;
    }

    private String name;

    private Object value;

    private boolean isConstructorParam;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public boolean getIsConstructorParam() {
        return isConstructorParam;
    }

    public void setConstructorParam(boolean constructorParam) {
        isConstructorParam = constructorParam;
    }
}
