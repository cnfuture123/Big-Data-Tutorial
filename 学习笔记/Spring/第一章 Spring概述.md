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

### IOC和DI（重要）

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

### 通过类型获取bean

  - 从IOC容器中获取bean时，除了通过id值获取，还可以通过bean的类型获取。但如果同一个类型的bean在XML文件中配置了多个，则获取时会抛出异常，所以同一个类型的bean在容器中必须是唯一的。
    ```
    HelloWorld world = context.getBean(HelloWorld.class);
    ```
  
### 为bean的属性赋值

  - 依赖注入的方式：
    - 通过bean的setXxx()方法赋值：Student类的setId(), setName(), setAge()
    - 通过bean的构造器赋值：
      - Spring自动匹配合适的构造器：
        ```
        <bean id="student" class="cn.bean.Student">
          <constructor-arg value="01"/>
          <constructor-arg value="cn"/>
          <constructor-arg value="28"/>
        </bean>
        ```
      - 通过索引值指定参数位置:
        ```
        <bean id="student" class="cn.bean.Student">
          <constructor-arg value="01" index="0"/>
          <constructor-arg value="cn" index="1"/>
          <constructor-arg value="28" index="2"/>
        </bean>
        ```
  - 赋值支持的类型：
    - 字面量：
      - 可以使用字符串表示的值，可以通过value属性或value子节点的方式指定
      - 基本数据类型及其封装类、String等类型都可以采取字面值注入的方式
      - 若字面值中包含特殊字符，可以使用<![CDATA[]]>把字面值包裹起来
    - null值
    - 外部已声明的bean：
      ```
      <bean id="shop" class="com.cn.Shop">
        <property name="book", ref="book"/>
      </bean>
      ```

### 集合属性

  - 在Spring中可以通过一组内置的XML标签来配置集合属性，例如：```<list>，<set>或<map>```
  - 数组和List：
    - 指定```<list>```标签，通过<value>指定简单的常量值，通过```<ref>```指定对其他Bean的引用，通过```<null/>```指定空元素。甚至可以内嵌其他集合。
      ```
      <bean id="shop" class="com.cn.Shop">
        <property name="categoryList">
          <list>
            <value>历史</value>
            <value>生活</value>
          </list>
        </property>
      </bean>
      ```
  - Map:
    - 通过```<map>```标签定义，标签里可以使用多个```<entry>```作为子标签。每个条目包含一个键和一个值。
    - 可以将Map的键和值作为```<entry>```的属性定义：简单常量使用key和value来定义；bean引用通过key-ref和value-ref属性定义。
      ```
      <bean id="shop" class="com.cn.Shop">
        <property name="shopMap">
          <map>
            <entry>
              <key>
                <value>key01</value>
              </key>
              <ref bean="shop01"/>
            </entry>
          </map>
        </property>
      </bean>
      ```
  
### FactoryBean

  - Spring中有两种类型的bean，一种是普通bean，另一种是工厂bean，即FactoryBean。
  - 工厂bean跟普通bean不同，其返回的对象不是指定类的一个实例，其返回的是该工厂bean的getObject方法所返回的对象。

### bean的高级配置

  - 配置信息的继承：
    - Spring允许继承bean的配置，被继承的bean称为父bean，继承这个父bean的bean称为子bean。
      - 子bean从父bean中继承配置，包括bean的属性配置
      - 子bean也可以覆盖从父bean继承过来的配置
    - 示例：
    ```
    <!-- 以emp作为父bean，继承后可以省略公共属性值的配置 -->
    <bean id="emp02" parent="emp">
      <property name="empId" value="1002"/>
      <property name="empName" value="cn2"/>
      <property name="age" value="25"/>
    </bean>
    ```
  - bean之间的依赖：
    - 创建一个bean的时候需要保证另外一个bean也被创建，称前面的bean对后面的bean有依赖。需要注意的是依赖关系不等于引用关系，Employee即使依赖Department也可以不引用它。
    - 示例：
      ```
      <bean id="emp03" class="com.cn.Employee" depends-on="dept">
        <property name="empId" value="1003"/>
        <property name="empName" value="Kate"/>
        <property name="age" value="21"/>
      </bean>
      ```

### bean的作用域（重要）

  - 在<bean>元素的scope属性里设置bean的作用域，以决定这个bean是单实例的还是多实例的。
  - 默认情况下，Spring只为每个在IOC容器里声明的bean创建唯一实例，整个IOC容器范围内都能共享该实例：所有后续的getBean()调用和bean引用都将返回这个唯一的bean实例。该作用域被称为singleton，它是所有bean的默认作用域。
  - 作用域配置：
    - singleton：单个Bean实例
    - prototype：每次调用getBean()都会返回一个新的实例
    - request：每次HTTP请求都会创建一个新的Bean，该作用域仅适用于WebApplicationContext环境
    - session：同一个HTTP Session共享一个Bean，不同的HTTP Session使用不同的Bean。该作用域仅适用于WebApplicationContext环境

### bean的生命周期（重要）

  - Spring IOC容器对bean的生命周期进行管理的过程：
    - 通过构造器或工厂方法创建bean实例
    - 为bean的属性设置值和对其他bean的引用
    - 调用bean的初始化方法
    - 使用bean
    - 当容器关闭时，调用bean的销毁方法
  - 在配置bean时，通过init-method和destroy-method属性为bean指定初始化和销毁方法
  - bean的后置处理器：
    - bean后置处理器允许在调用初始化方法前后对bean进行额外的处理
    - bean后置处理器对IOC容器里的所有bean实例逐一处理，而非单一实例。
    - bean后置处理器需要实现接口org.springframework.beans.factory.config.BeanPostProcessor，并调用以下两个方法：
      - postProcessBeforeInitialization(Object, String)
      - postProcessAfterInitialization(Object, String)

### 引用外部属性文件

  - 当bean的配置信息逐渐增多时，查找和修改一些bean的配置信息就变得愈加困难。这时可以将一部分信息提取到bean配置文件的外部，以properties格式的属性文件保存起来，同时在bean的配置文件中引用properties属性文件中的内容，从而实现一部分属性值在发生变化时仅修改properties属性文件即可。这种技术多用于连接数据库的基本信息的配置。
  - 示例：
    - 创建properties配置文件：
      ```
      prop.userName=root
      prop.password=root
      prop.url=jdbc:mysql:///test
      prop.driverClass=com.mysql.jdbc.Driver
      ```
    - 指定properties文件的位置：
      ```
      <!-- classpath:xxx 表示属性文件位于类路径下 -->
      <context:property-placeholder location="classpath:jdbc.properties"/>
      ```
    - 从properties属性文件中引入属性值：
      ```
      <bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
        <property name="user" value="${prop.userName}"/>
        <property name="password" value="${prop.password}"/>
        <property name="jdbcUrl" value="${prop.url}"/>
        <property name="driverClass" value="${prop.driverClass}"/>
      </bean>
      ```

### 自动装配

  - 手动装配：以value或ref的方式明确指定属性值都是手动装配。
  - 自动装配：根据指定的装配规则，不需要明确指定，Spring自动将匹配的属性值注入bean中。
    - 根据类型自动装配：将类型匹配的bean作为属性注入到另一个bean中
    - 根据名称自动装配：必须将目标bean的名称和属性名设置的完全相同

### 通过注解配置bean

  - 使用注解标识组件
    - @Component：普通组件
    - @Controller：表述层控制器组件
    - @Service：业务逻辑层组件
    - @Repository：持久化层组件
    - 注意：事实上Spring并没有能力识别一个组件到底是不是它所标记的类型，即使将@Respository注解用在一个表述层控制器组件上面也不会产生任何错误，所以@Respository、@Service、@Controller这几个注解仅仅是为了让开发人员自己明确当前的组件扮演的角色。
  - 扫描组件（重要）：
    - 指定被扫描的包：
      ```
      <context:component-scan base-package="com.cn.component"/>
      ```
    - 说明：
      - base-package属性指定一个需要扫描的基类包，Spring容器将会扫描这个基类包及其子包中的所有类。
      - 当需要扫描多个包时可以使用逗号分隔。
      - 如果仅希望扫描特定的类而非基包下的所有类，可使用resource-pattern属性过滤特定的类：
        ```
        <context:component-scan 
          base-package="com.cn.component" 
          resource-pattern="autowire/*.class"/>
        ```
  - 组件装配：
    - Controller组件中往往需要用到Service组件的实例，Service组件中往往需要用到Repository组件的实例。Spring可以通过注解的方式帮我们实现属性的装配。
    - 在指定要扫描的包时，<context:component-scan> 元素会自动注册一个bean的后置处理器：AutowiredAnnotationBeanPostProcessor的实例。该后置处理器可以自动装配标记了@Autowired、@Resource或@Inject注解的属性。
      - @Autowired注解：
        - 根据类型实现自动装配
        - 构造器、普通字段(即使是非public)、一切具有参数的方法都可以应用@Autowired注解
        - 默认情况下，所有使用@Autowired注解的属性都需要被设置。当Spring找不到匹配的bean装配属性时，会抛出异常
        - 若某一属性允许不被设置，可以设置@Autowired注解的required属性为false
        - 默认情况下，当IOC容器里存在多个类型兼容的bean时，Spring会尝试匹配bean的id值是否与变量名相同，如果相同则进行装配。如果bean的id值不相同，通过类型的自动装配将无法工作。此时可以在@Qualifier注解里提供bean的名称。
      - @Resource注解：
        - 要求提供一个bean名称的属性，若该属性为空，则自动采用标注处的变量或方法名作为bean的名称。
      - @Inject注解：
        - @Inject和@Autowired注解一样也是按类型注入匹配的bean，但没有reqired属性。
  - Spring循环依赖问题？
    - 参考：https://zhuanlan.zhihu.com/p/62382615



