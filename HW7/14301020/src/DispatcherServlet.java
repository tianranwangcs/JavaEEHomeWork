package MVC;

import Annotation.RequestMapping;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by lovejoy on 2016/12/2.
 */
public class DispatcherServlet extends HttpServlet{
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().append("Served at: ").append(request.getContextPath());

        //set params from request to ModelAndView
        ModelAndView mav = new ModelAndView();
        Enumeration<String> pNames = request.getParameterNames();
        HashMap<String,Object> paraMap = new HashMap<>();
        while(pNames.hasMoreElements()){
            String pName = (String) pNames.nextElement();
            paraMap.put(pName,request.getParameter(pName));
        }
        mav.setParamMap(paraMap);

        //set values from Test.java to ModelAndView
        //then forward from index.jsp to test.jsp
        String uri = request.getRequestURI();
        ControllerMap cm = ControllerMap.getControllerMap();
        mav = cm.invokeMethod(uri, mav);
        if(mav == null){
            System.out.println("mav is null");
            request.getRequestDispatcher(uri).forward(request, response);
            return;
        }
        Iterator<String> it = mav.getObjectNames().iterator();
        while(it.hasNext()){
            String name = it.next();
            request.setAttribute(name, mav.getObject(name));
        }
        System.out.println("forward to" + mav.getViewName());
        request.getRequestDispatcher(mav.getViewName()).forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
