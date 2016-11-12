package dev.bean;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import dev.util.ReflectionUtils;

public class BeanUtil {

    public static void invokeSetterMethod(Object obj, String propertyName, Object propertyValue) {
        char[] tmp = propertyName.toCharArray();
        if (tmp[0] >= 'a' && tmp[0] <= 'z') {
            tmp[0] -= 32;
        }

        String setMethodName = String.format("set%s", String.valueOf(tmp));
        Field field = null;
        Class<?> cls = obj.getClass();
        try {
            field = cls.getDeclaredField(propertyName);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        Class<?> type = field.getType();
        Method method = ReflectionUtils.findMethod(cls, setMethodName, type);
        if (method != null) {
            try {
                method.invoke(obj, propertyValue);
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}