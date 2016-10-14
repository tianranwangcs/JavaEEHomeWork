package Control;

import Model.MyRequest;
import Model.MyResponse;
import Util.WebXmlParser;

import javax.servlet.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created by LOVEJOY on 2016/10/5.
 */
public class ServletProcessor {
    public static void process(MyRequest req, MyResponse res) {
        String urlPattern = req.getParameter("Uri");//"/Login"
        String servletClass = WebXmlParser.getServletClass(urlPattern);
        if (servletClass == null) {
            String msg = "HTTP/1.1 404 Not Found\r\nContent-Type: text/plain\r\n\r\n404 Not Found";
            try {
                res.getWriter().print(msg);
            }catch (IOException e){
                e.printStackTrace();
            }
        } else {
            try {
                File dir=new File("src");
                URL url=dir.toURL();
                URL[] urls=new URL[]{url};
                ClassLoader loader=new URLClassLoader(urls);
                Class myClass = loader.loadClass(servletClass);
                Servlet servlet = (Servlet) myClass.newInstance();
                servlet.init(null);
                servlet.service(req, res);
                servlet.destroy();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }catch (ServletException e){
                e.printStackTrace();
            }
        }
    }
}
