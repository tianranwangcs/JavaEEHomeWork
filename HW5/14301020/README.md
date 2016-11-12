#Java EE第五次作业
- 实现了类似Spring框架的控制反转(Inversion of Control)功能
- 通过在bean.xml里配置类和类之间的相互依赖\r\n
注意：被依赖的类在xml文件里声明必须在使用依赖的类之前
- 两种注入方式：设值注入和构造注入
- 运行src/test/BeanFactoryTest.java测试
注意：这里使用到了JUnit单元测试 通过@Test注解直接执行测试方法 没有main函数
