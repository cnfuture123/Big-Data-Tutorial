## Spring概述

  - 概述：
    - Spring是一个开源框架
    - Spring是一个IOC(DI)和AOP容器框架
  - Spring特性：
    - 非侵入式：基于Spring开发的应用中的对象可以不依赖于Spring API
    - 依赖注入：DI-Dependency Injection，是IOC（反转控制）最经典的实现
    - 面向切面编程：AOP-Aspect Oriented Programming
    - 容器：Spring是一个容器，包含并管理应用对象的生命周期
    - 组件化：Spring使用XML和Java注解的方式将简单的组件对象组合成一个复杂的应用
    - 一站式：在IOC和AOP的基础上可以整合各种企业应用的开源框架和优秀的第三方类库
  - 创建Spring配置文件：
    - 使用bean元素定义一个由IOC容器创建的对象
    - class属性指定创建bean的全类名
    - id属性指定引用bean实例的唯一标识
    - 使用property标签为bean的属性赋值
    - 示例：
      - 定义bean:
        ```
        <bean id="student" class="cn.bean.Student">
          <property name="id" value="01"/>
          <property name="name" value="cn"/>
          <property name="age" value="28"/>
        </bean>
        ```
      - 调用bean:
        ```
        1.创建IOC容器对象
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        2.根据bean id获取bean实例对象
        Student stu = (Student)context.getBean("student");
        3.调用bean实例对象方法
        stu.printName();
        ```

## IOC容器

### IOC和DI

  - IOC(Inversion of Control)：反转控制
    - 在应用程序的组件需要获取资源时，传统的方式是组件主动从容器中获取所需的资源
    - 反转控制的思想是由容器主动的将资源推送给需要的组件，开发人员不需要知道容器是如何创建资源对象的
  - DI(Dependency Injection)：依赖注入
    - IOC的另一种表述方式：即组件以一些预先定义好的方式(例如：setter方法)接受来自于容器的资源注入。
    - IOC描述的是一种思想，而DI是对IOC思想的具体实现。
  - IOC容器在Spring中的实现：
    - 通过IOC容器读取Bean的实例之前，需要先将IOC容器本身实例化
    - Spring提供了IOC容器的两种实现方式：
      - BeanFactory：IOC容器的基本实现，是Spring内部的基础设施，是面向Spring本身的
      - ApplicationContext：BeanFactory的子接口，提供了更多高级特性，面向Spring的使用者
  - ApplicationContext的主要实现类：
    - ClassPathXmlApplicationContext：对应类路径下的XML格式的配置文件
    - FileSystemXmlApplicationContext：对应文件系统中的XML格式的配置文件
  - ConfigurableApplicationContext：
    - ApplicationContext的子接口，包含一些扩展方法：refresh(), close()等
  - WebApplicationContext：
    - 专门为WEB应用而准备的，允许从WEB根目录的路径中完成初始化工作
