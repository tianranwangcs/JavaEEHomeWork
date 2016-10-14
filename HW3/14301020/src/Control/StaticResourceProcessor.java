package Control;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by LOVEJOY on 2016/10/5.
 */
public class StaticResourceProcessor {
    private static String msg;

    public static void process(ServletRequest req, ServletResponse res){
        try{
            String fileName=req.getParameter("Uri").substring(1);//"/index.html"->"index.html"
            String content=new String(Files.readAllBytes(Paths.get("res/"+fileName)));
            String head="HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n";
            msg=head+content;
        }catch (Exception e){
            e.printStackTrace();
            msg="HTTP/1.1 404 Not Found\r\nContent-Type: text/plain\r\n\r\n404 Not Found";
        }
        try{
            PrintWriter writer=res.getWriter();
            writer.print(msg);
            writer.flush();
            writer.close();
            System.out.println("StaticResourceProcessor Finish");
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
