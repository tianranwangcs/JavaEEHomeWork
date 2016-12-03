package MVC;

import Annotation.Controller;
import Annotation.RequestMapping;
import Util.ScanPackage;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by lovejoy on 2016/12/3.
 */
public class ControllerMap {
    private static ControllerMap cm = null;
    private static HashMap<String,Method> uriMethodMap = new HashMap<String,Method>();
    private static HashMap<String,Object> uriObjectMap = new HashMap<String,Object>();

    private ControllerMap() {
        ScanPackage scan = new ScanPackage("Test");
        try {
            List<String> list = scan.getFullyQualifiedClassNameList();
            Iterator<String> it = list.iterator();
            Class<?> c;
            while (it.hasNext()) {
                String className = it.next();
                c = Class.forName(className);
                //get the class with annotation Controller
                if (c.isAnnotationPresent(Controller.class)) {
                    Method[] methods = c.getDeclaredMethods();
                    for (int i = 0; i < methods.length; i++) {
                        //get methods with annotation RequestMapping
                        if (methods[i].isAnnotationPresent(RequestMapping.class)) {
                            RequestMapping rm = methods[i].getAnnotation(RequestMapping.class);
                            String uri = rm.value();
                            Object obj = uriObjectMap.get(uri);
                            if (obj == null) {
                                uriObjectMap.put(uri, c.newInstance());
                                uriMethodMap.put(uri, methods[i]);
                            }
                        }
                    }

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e){
            e.printStackTrace();
        }
    }

    //single instance mode
    public static ControllerMap getControllerMap(){
        if(cm == null){
            cm = new ControllerMap();
        }
        return cm;
    }

    public ModelAndView invokeMethod(String uri,ModelAndView inMav){
        Object obj = uriObjectMap.get(uri);
        Method method = uriMethodMap.get(uri);
        if(method == null){
            return null;
        }
        ModelAndView mav = null;
        try {
            mav = (ModelAndView) method.invoke(obj,inMav);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return mav;
    }
}
