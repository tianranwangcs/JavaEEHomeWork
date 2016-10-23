package Util;

import java.io.*;

/**
 * Created by LOVEJOY on 2016/10/22.
 */
public class JSPParser {
    byte[] tempbytes = new byte[4096];
    String m_string;
    String class_name;
    private String m_url;
    int[][] position;
    String[] list;
    int count;

    public JSPParser(String url) {
        //去掉".jsp"
        this.m_url = url.substring(0, url.length() - 4);
        list = new String[20];
        count = 0;
        position = new int[20][];
        for (int i = 0; i < 20; i++) {
            position[i] = new int[2];
        }

        if (m_url.lastIndexOf("/") > -1) {
            class_name = m_url.substring(m_url.lastIndexOf("/") + 1, m_url.length());
            class_name += "_jsp";
        } else {
            class_name = m_url + "_jsp";
        }
        this.m_url += "_jsp.java";

        InputStream in = null;
        try {
            int byteread = 0;
            in = new FileInputStream(url);
            while ((byteread = in.read(tempbytes)) != -1) {
                System.out.write(tempbytes, 0, byteread);
            }
            System.out.println();
            m_string = new String(tempbytes, "utf8");

        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e1) {
                }
            }
        }
    }

    private void separate() {//截取"<%"和"%>"之间的 存到list中
        String current = m_string;
        int a = 0;
        int b = 0;
        while (true) {
            a = m_string.indexOf("<%", b);//从第b位开始找"<%" 找到返回索引值 否则返回-1
            if (a < 0) {
                break;
            }
            b = m_string.indexOf("%>", a);//找刚才这个"<%"之后的"%>"
            if (b != -1) {
                list[count] = m_string.substring(a + 2, b);//截取"<%"和"%>"中间的
                System.out.println(list[count]);
                position[count][0] = a;
                position[count][1] = b + 2;
                count++;
            }
        }
        return;
    }

    private void writecommonatveryfirst(OutputStreamWriter pw) throws IOException {
        pw.write("package Servlet;\n");
    }

    private void writecommonbeforeoutput(OutputStreamWriter pw) throws IOException {
        pw.write("import javax.servlet.*;import java.io.IOException;import java.io.PrintWriter;");
        pw.write("public class " + class_name + " implements Servlet{\n");
        pw.write("public void init(ServletConfig config) throws ServletException {\n" +
                "        System.out.println(\"init\");\n" +
                "    }\n" +
                "\n" +
                "    public void service(ServletRequest request, ServletResponse response)\n" +
                "            throws ServletException, IOException {\n" +
                "        System.out.println(\"from service\");" +
                "PrintWriter out = response.getWriter();" +
                "out.write(\"HTTP/1.1 200 OK\");" +
                "out.write(\"\\n\");" +
                "out.write(\"Content-Type: text/html\");" +
                "out.write(\"\\n\");" +
                "out.write(\"\\n\");");
    }

    private void writecommonafteroutput(OutputStreamWriter pw) throws IOException {
        pw.write("out.flush();" +
                "out.close();" +
                "}\n" +
                "\n" +
                "    public void destroy() {\n" +
                "        System.out.println(\"destroy\");\n" +
                "    }\n" +
                "\n" +
                "    public String getServletInfo() {\n" +
                "        return null;\n" +
                "    }\n" +
                "\n" +
                "    public ServletConfig getServletConfig() {\n" +
                "        return null;\n" +
                "    }\n" +
                "}");
    }

    private void writelable(String s, OutputStreamWriter pw) throws IOException {//输出html语句
        int a = 0;
        int b = 0;
        s = s.replaceAll("\n", "");//所有"\r\n"替换成空格
        s = s.replaceAll("\r", "");

        s = s.replaceAll("\"", "\\\\\"");//在"前面加上转义符\ 糟糕的代码
        s.trim();
        pw.write("out.write(\"" + s + "\");");
    }

    private void headparsing(OutputStreamWriter pw) throws IOException {//import 包名
        for (int i = 0; i < count; i++) {
            if (list[i].substring(0, 1).equals("@")) {//刚才截取的count个字符串中如果开头是@的话
                int a = list[i].indexOf("import");//找到里面的import
                if (a > 0) {
                    String im = list[i].substring(a + 8, list[i].indexOf("\"", a + 8));//找到具体import的包名
                    int b = im.indexOf(",");//多个包名用","隔开的话
                    while (b > 0) {
                        pw.write("import ");
                        pw.write(im.substring(b));
                        pw.write(";\r\n");
                        im = im.substring(b + 1, im.length());
                        b = im.indexOf(",");
                    }
                    pw.write("import ");
                    pw.write(im);
                    pw.write(";\r\n");
                }
            }
        }
    }

    public void generateservlet(){
        OutputStream out = null;
        try {
            OutputStreamWriter pw = null;
            //这里的路径根据个人文件夹路径不同而更改
            pw = new OutputStreamWriter(new FileOutputStream("src/Servlet/" + class_name + ".java"));
            writecommonatveryfirst(pw);
            this.separate();
            headparsing(pw);
            writecommonbeforeoutput(pw);
            // 内容
            for (int i = 0; i < count; i++) {
                if (i > 0) {//i>0时
                    if (!list[i - 1].substring(0, 1).equals("@")) {
                        if (!list[i - 1].substring(0, 1).equals("="))//没有@ 没有=
                            pw.write(list[i - 1]);
                        else//没有@ 有=
                            pw.write("out.write("
                                    + (list[i - 1].substring(1,
                                    list[i - 1].length())) + ");\r\n");
                    }
                    pw.write("\r\n");
                    String s = (m_string.substring(position[i - 1][1],
                            position[i][0]));//中间正常的html语句
                    s.trim();
                    writelable(s, pw);

                } else {//i=0时
                    String s = (m_string.substring(0, position[i][0]));
                    s.trim();
                    writelable(s, pw);
                }
            }
            if (!list[count - 1].substring(0, 1).equals("@")) {//最后一个在"<%"和"%>"之间的str
                if (!list[count - 1].substring(0, 1).equals("=")) {
                    pw.write(list[count - 1]);
                } else {
                    pw.write("out.write("
                            + (list[count - 1].substring(1,
                            list[count - 1].length())) + ");\r\n");
                }
            }
            pw.write("\r\n");
            String s = (m_string.substring(position[count - 1][1],
                    m_string.length()));//剩下的html字符串
            s.trim();
            writelable(s, pw);
            writecommonafteroutput(pw);
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
