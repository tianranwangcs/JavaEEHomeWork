#Java EE第四次作业
- 在第三次基础上增加了对.jsp文件的支持
- 手动添加.jsp文件到res目录下 并在web.xml中配置好（参考我的index.jsp范例）
- 运行src/Control/HttpServer.java
- 访问localhost:8888/index.jsp 程序会根据web.xml中的配置 寻找src/Servlet/index_jsp.java文件 
由于第一次并没有生成 所以在ServletProcessor中会调用JSPParser来解析、生成.java文件 
之后调用JavaCompiler的方法来动态编译
我使用的是IntelliJ IDEA 所以设置编译后的class文件放到out/production/工程名（我这里是HW4JSP2Servlet）/ 中 不同IDE可能会不一样 要手动设置
