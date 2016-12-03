package MVC;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by lovejoy on 2016/12/2.
 */
public class ModelAndView {
    private String viewName;
    private HashMap<String,Object> objectMap=new HashMap<>();//values from test
    private HashMap<String, Object> paramMap=new HashMap<>();//params from request

    public void setViewName(String viewName) {
        this.viewName = "/"+viewName+".jsp";
    }

    public String getViewName()
    {
        return viewName;
    }

    public void addObject(String objectKey, Object objectValue) {
        objectMap.put(objectKey,objectValue);
    }

    public Set<String> getObjectNames()
    {
        return objectMap.keySet();
    }

    public Object getObject(String objectKey)
    {
        return objectMap.get(objectKey);
    }

    public void setParamMap(HashMap<String,Object> paramMap)
    {
        this.paramMap=paramMap;
    }

    public Object getParam(String paramKey)
    {
        return paramMap.get(paramKey);
    }
}
