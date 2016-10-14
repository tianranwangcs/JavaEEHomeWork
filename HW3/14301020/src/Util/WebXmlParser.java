package Util;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by LOVEJOY on 2016/10/4.
 *
 * JDOM解析XML文档
 */
public class WebXmlParser {
    private static String WEBROOT="web.xml";

    private static HashMap<String,String> servletMap=new HashMap<String,String>();

    private static void Parse()
    {
        SAXBuilder builder =new SAXBuilder();
        try
        {
            Document document=builder.build(WEBROOT);
            Element webApp=document.getRootElement();
            Namespace ns=Namespace.getNamespace("http://java.sun.com/xml/ns/javaee");//need namespace
            List servletList=webApp.getChildren("servlet",ns);
            List servletMappingList=webApp.getChildren("servlet-mapping",ns);
            if(!servletList.isEmpty()&&!servletMappingList.isEmpty())
            {
                for(int i=0;i<servletList.size();i++)
                {
                    Element servlet=(Element)servletList.get(i);
                    Element servletMapping=(Element)servletMappingList.get(i);

                    String servletName=servlet.getChildText("servlet-name",ns);
                    String servletClass=servlet.getChildText("servlet-class",ns);
                    String urlPattern=servletMapping.getChildText("url-pattern",ns);

                    servletMap.put(urlPattern,servletName+":"+servletClass);
                }
            }
        }
        catch(JDOMException e)
        {
            e.printStackTrace();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public static String getServletName(String urlPattern)
    {
        if(servletMap.isEmpty())
            Parse();
        String value=servletMap.get(urlPattern);
        if(value==null)
            return null;
        else
            return value.split(":")[0];
    }

    public static String getServletClass(String urlPattern)
    {
        if(servletMap.isEmpty())
            Parse();
        String value=servletMap.get(urlPattern);
        if(value==null)
            return null;
        else
            return value.split(":")[1];
    }
}
