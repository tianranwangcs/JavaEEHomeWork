package Model;

import javax.servlet.*;
import java.io.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by LOVEJOY on 2016/10/2.
 */
public class MyRequest implements ServletRequest {
    private InputStream input;
    private HashMap<String, String> paramMap;

    public MyRequest(InputStream input) {
        this.input = input;
        paramMap = new HashMap<>();
    }

    private void parse() {//parse the request
        try {
            byte[] buffer=new byte[1024];
            int i=input.read(buffer);
            String msg=new String(buffer);
            String msgTrim=msg.trim();
            String[] lines=msgTrim.split("\\r\\n");
            String[] firstLine=lines[0].split(" ");//parse the first line
            paramMap.put("Method", firstLine[0]);
            paramMap.put("Uri", firstLine[1]);
            paramMap.put("Protocol", firstLine[2]);

            for(int j=1;j<lines.length-1;j++){
                if(!lines[j].equals("")){
                    String[] paramLine = lines[j].split(": ");
                    paramMap.put(paramLine[0], paramLine[1]);
                }
            }

            paramMap.put("Context", lines[lines.length-1]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getParameter(String s) {
        if (paramMap.isEmpty()) {
            parse();
        }
        return paramMap.get(s);
    }

    @Override
    public Object getAttribute(String s) {
        return null;
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return null;
    }

    @Override
    public String getCharacterEncoding() {
        return null;
    }

    @Override
    public void setCharacterEncoding(String s) throws UnsupportedEncodingException {

    }

    @Override
    public int getContentLength() {
        return 0;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return null;
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return null;
    }

    @Override
    public String[] getParameterValues(String s) {
        return new String[0];
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return null;
    }

    @Override
    public String getProtocol() {
        return null;
    }

    @Override
    public String getScheme() {
        return null;
    }

    @Override
    public String getServerName() {
        return null;
    }

    @Override
    public int getServerPort() {
        return 0;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return null;
    }

    @Override
    public String getRemoteAddr() {
        return null;
    }

    @Override
    public String getRemoteHost() {
        return null;
    }

    @Override
    public void setAttribute(String s, Object o) {

    }

    @Override
    public void removeAttribute(String s) {

    }

    @Override
    public Locale getLocale() {
        return null;
    }

    @Override
    public Enumeration<Locale> getLocales() {
        return null;
    }

    @Override
    public boolean isSecure() {
        return false;
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String s) {
        return null;
    }

    @Override
    public String getRealPath(String s) {
        return null;
    }

    @Override
    public int getRemotePort() {
        return 0;
    }

    @Override
    public String getLocalName() {
        return null;
    }

    @Override
    public String getLocalAddr() {
        return null;
    }

    @Override
    public int getLocalPort() {
        return 0;
    }

    @Override
    public ServletContext getServletContext() {
        return null;
    }

    @Override
    public AsyncContext startAsync() throws IllegalStateException {
        return null;
    }

    @Override
    public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException {
        return null;
    }

    @Override
    public boolean isAsyncStarted() {
        return false;
    }

    @Override
    public boolean isAsyncSupported() {
        return false;
    }

    @Override
    public AsyncContext getAsyncContext() {
        return null;
    }

    @Override
    public DispatcherType getDispatcherType() {
        return null;
    }
}
