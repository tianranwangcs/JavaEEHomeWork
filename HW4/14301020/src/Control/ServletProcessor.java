package Control;

import Model.MyRequest;
import Model.MyResponse;
import Util.JSPParser;
import Util.WebXmlParser;

import javax.servlet.*;
import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by LOVEJOY on 2016/10/5.
 */
public class ServletProcessor {
    public static void process(MyRequest req, MyResponse res) {
        String urlPattern = req.getParameter("Uri");//"/Login"
        String servletClass = WebXmlParser.getServletClass(urlPattern);
        String servletName = WebXmlParser.getServletName(urlPattern);
        if (servletClass == null) {
            String msg = "HTTP/1.1 404 Not Found\r\nContent-Type: text/plain\r\n\r\n404 Not Found";
            try {
                res.getWriter().print(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                File dir = new File("src");
                URL url = dir.toURL();
                URL[] urls = new URL[]{url};
                ClassLoader loader = new URLClassLoader(urls);
                Class myClass = loader.loadClass(servletClass);
                Servlet servlet = (Servlet) myClass.newInstance();
                servlet.init(null);
                servlet.service(req, res);
                servlet.destroy();
            } catch (ClassNotFoundException e) {
                try {
                    //.jsp对应的.java文件不存在 那么用JSPParser动态生成
                    JSPParser parser = new JSPParser(servletName);
                    parser.generateservlet();

                    //利用JavaCompiler动态编译刚生成的.java文件
                    JavaCompiler jc = ToolProvider.getSystemJavaCompiler();
                    StandardJavaFileManager fileManager = jc.getStandardFileManager(null, null, null);
                    String currentDir=System.getProperty("user.dir");
                    String fileName=servletClass.replace(".","\\");

                    //默认情况下这个.class文件会和.java文件在同一路径下 改成生成在/out文件夹下的对应目录不同编译器对应的out目录可能会不同 要自己手动改
                    //参考 http://stackoverflow.com/questions/2028193/specify-output-path-for-dynamic-compilation
                    fileManager.setLocation(StandardLocation.CLASS_OUTPUT, Arrays.asList(new File(currentDir+"\\out\\production\\HW4JSP2Servlet\\")));

                    Iterable<? extends JavaFileObject> fileObjects = fileManager.getJavaFileObjects(currentDir+"\\src\\"+fileName+".java");
                    JavaCompiler.CompilationTask cTask = jc.getTask(null, fileManager, null, null, null, fileObjects);
                    cTask.call();
                    fileManager.close();

                    //利用ClassLoader加载编译后的.class文件到内存
                    File dir = new File("src");
                    URL url = dir.toURL();
                    URL[] urls = new URL[]{url};
                    ClassLoader loader = new URLClassLoader(urls);
                    Class myClass = loader.loadClass(servletClass);

                    Servlet servlet = (Servlet) myClass.newInstance();
                    servlet.init(null);
                    servlet.service(req, res);
                    servlet.destroy();
                } catch (MalformedURLException e1) {
                    e1.printStackTrace();
                } catch (ClassNotFoundException e1) {
                    e1.printStackTrace();
                } catch (InstantiationException e1) {
                    e1.printStackTrace();
                } catch (IllegalAccessException e1) {
                    e1.printStackTrace();
                } catch (ServletException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ServletException e) {
                e.printStackTrace();
            }
        }
    }
}
