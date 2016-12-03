#Java EE第7次作业
- Simulate a simple DispatcherServler in SpringMVC.
- Tools:IntelliJ2016 Tomcat9.0
- You need to create a JavaEE Project and add servlet-api.jar to the dependent library.
- Type in localhost:8080/index.jsp to access the login form. Click the "Submit" button to forward to test.jsp.
- The action "/hello" will be intercepted by DispatcherServlet.
- Then DispatcherServlet will find all annotated methods in package Test.
- Then it will get ModelAndView with params.
- Later params in ModelAndView will be sent to request.
- Finally we forward request and response to test.jsp.
