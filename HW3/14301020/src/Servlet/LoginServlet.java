package Servlet;

import javax.servlet.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;


/**
 * Created by LOVEJOY on 2016/10/4.
 */
public class LoginServlet implements Servlet {
    private HashMap<String, String> contextParamMap;

    public void init(ServletConfig config) throws ServletException {
        contextParamMap = new HashMap<>();
        System.out.println("LoginServlet Init");
    }

    public void service(ServletRequest req, ServletResponse res)
            throws ServletException, IOException {

        String context = req.getParameter("Context");
        if (req.getParameter("Method").equals("POST") && !context.equals("")) {//save params into HashMap
            if (context.contains("&")) {
                String[] params = context.split("&");
                for (int i = 0; i < params.length; i++) {
                    String[] pair = params[i].split("=");
                    contextParamMap.put(pair[0], pair[1]);
                }
            } else {
                String[] pair = context.split("=");
                contextParamMap.put(pair[0], pair[1]);
            }
        }

        StringBuilder builder = new StringBuilder();
        for(HashMap.Entry<String,String> entry:contextParamMap.entrySet()){
            builder.append(entry.getKey()+":"+entry.getValue()+"\n");
        }

        PrintWriter writer = res.getWriter();
        writer.println("HTTP/1.1 200 OK\n" +
                "Content-Type: text/plain\n" +
                "\n" +
                "This is from a LoginServet.\n" +
                builder.toString());
        writer.flush();
        writer.close();
        System.out.println("LoginServlet Service");
    }

    public void destroy() {
        System.out.println("LoginServlet Destroy");
    }

    public String getServletInfo() {
        return null;
    }

    public ServletConfig getServletConfig() {
        return null;
    }
}
