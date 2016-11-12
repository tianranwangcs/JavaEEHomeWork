package dev.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;

public class ReflectionUtils {
    public static Method findMethod(Class<?> cls, String name, Class<?>... parameterTypes) {
        Class<?> currentClass = cls;
        while (currentClass != null) {
            Method[] allMethods = currentClass.getDeclaredMethods();
            for (Method method : allMethods) {
                if (name.equals(method.getName()) &&
                        (parameterTypes == null ||
                                Arrays.equals(parameterTypes, method.getParameterTypes())))
                    return method;
            }
            currentClass = currentClass.getSuperclass();
        }
        return null;
    }

    public static Constructor findConstructor(Class<?> cls,  Class<?>... parameterTypes){
        Class<?> currentClass = cls;
        while (currentClass != null) {
            Constructor[] allConstructors = currentClass.getDeclaredConstructors();
            for (Constructor constructor : allConstructors) {
                if (parameterTypes == null ||
                                Arrays.equals(parameterTypes, constructor.getParameterTypes()))
                    return constructor;
            }
            currentClass = currentClass.getSuperclass();
        }
        return null;
    }
}
