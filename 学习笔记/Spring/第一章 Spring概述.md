## Spring概述

  - 概述：
    - Spring是一个开源框架，提供IOC和AOP特性，它提供了非常多的组件，能够让你在开发Java应用的过程变得更加容易，弹性地支持其他软件框架
    - 优势和不足：
      - 优势：Spring面向模块进行开发，根据不同的功能进行划分，根据需求引入对应的模块即可，对于开发人员非常友好
        - Spring IoC容器，将我们的Java对象作为Spring Bean进行管理，管理着Bean的整个生命周期
        - Spring MVC提供“模型-视图-控制器”（Model-View-Controller）架构和随时可用的组件，用于开发灵活且松散耦合的Web应用程序
        - Spring AOP提供面向切面编程的接口
      - 不足：
        - 整个Spring体系比较复杂，对于开发人员需要一定的学习成本
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
    - 概述：
      - 是面向对象中的一种编程思想或原则
      - 在应用程序的组件需要获取资源时，传统的方式是：当我依赖一个对象，我需要主动去创建它并进行属性赋值，然后我才能去使用这个对象
      - 反转控制的思想是由容器主动的将资源推送给需要的组件，开发人员不需要过多地关注细节，如创建对象、属性赋值，这些工作交都由IoC容器来完成，达到解耦的目的
    - IoC的作用：
      - 屏蔽构造细节，在复杂的系统中，我们的应用更应该关注的是对象的运用，而非它的构造和初始化等细节
    - IoC容器的职责：
      - 依赖处理，通过依赖查找或者依赖注入
      - 管理托管的资源（Java Bean或其他资源）的生命周期
      - 管理配置（容器配置、外部化配置、托管的资源的配置）
  - DI(Dependency Injection)：依赖注入
    - DI依赖注入是IoC的一种实现方式或策略
    - IoC的实现策略：
      - 依赖查找：在应用程序里面主动调用IoC容器提供的接口去获取对应的Bean对象
      - 依赖注入：在IoC容器启动或者初始化的时候，通过构造器、字段、setter方法或者接口等方式注入依赖。依赖注入在IoC容器中的实现也是调用相关的接口获取Bean对象，只不过这些工作在IoC容器启动时由容器实现了
    - 依赖注入方式：
      - 构造器注入：通过构造器的参数注入相关依赖对象
      - Setter注入：通过Setter方法注入依赖对象，也可以理解为字段注入
      - 两者的区别：
        - 构造器注入在初始化该对象时才会注入依赖对象，一定程度上保证了Bean初始化后就是不变的对象，这样对于我们的程序和维护性都会带来便利
        - 构造器注入不允许出现循环依赖，因为它要求被注入的对象都是成熟态，保证能够实例化，而Setter注入或字段注入没有这样的要求
        - 构造器注入可以保证依赖的对象能够有序的被注入，而Setter注入或字段注入底层是通过反射机制进行注入，无法完全保证注入的顺序
      - 接口回调注入：通过实现Aware接口（例如 BeanNameAware、ApplicationContextAware）可以注入相关对象，Spring在初始化这类Bean时会调用其setXxx方法注入对象
  - IOC容器在Spring中的实现：
    - 通过IOC容器读取Bean的实例之前，需要先将IOC容器本身实例化
    - Spring提供了IOC容器的两种实现方式：
      - BeanFactory：IOC容器的基本实现，是Spring内部的基础设施，是面向Spring本身的
      - ApplicationContext：BeanFactory的子接口，提供了更多高级特性：面向切面、配置元信息、资源管理、事件机制、国际化、注解、Environment抽象等，面向Spring的使用者
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

  - 概述：
    - Spring中有两种类型的bean，一种是普通bean，另一种是工厂bean，即FactoryBean
    - FactoryBean跟普通bean不同，其返回的对象不是指定类的一个实例，其返回的是该工厂bean的getObject方法所返回的对象
  - ObjectFactory、FactoryBean和BeanFactory的区别：
    - ObjectFactory：提供的是延迟依赖查找，想要获取某一类型的Bean，需要调用其getObject()才能依赖查找到目标Bean对象。ObjectFactory就是一个对象工厂，想要获取该类型的对象，需要调用其getObject()方法生产一个对象
    - FactoryBean：不提供延迟性，在被依赖注入或依赖查找时，得到的就是通过getObject()拿到的实际对象
    - BeanFactory：Spring底层IoC容器，里面保存了所有的单例Bean
  - BeanFactory是如何处理循环依赖：
    - 这里的循环依赖指的是单例模式下的Bean字段注入时出现的循环依赖。构造器注入对于Spring无法自动解决（应该考虑代码设计是否有问题），可通过延迟初始化来处理。Spring只解决单例模式下的循环依赖
    - BeanFactory中处理循环依赖的方法主要借助于以下3个Map集合，当通过getBean依赖查找时会首先依次从上面三个Map获取，存在则返回，不存在则进行初始化
      - singletonObjects（一级 Map）：保存了所有已经初始化好的单例Bean，也就是会保存Spring IoC容器中所有单例的Spring Bean
      - earlySingletonObjects（二级 Map）：保存从三级Map获取到的正在初始化的Bean
      - singletonFactories（三级 Map）：保存了正在初始化的Bean对应的ObjectFactory实现类，调用其getObject()返回正在初始化的Bean对象（仅实例化还没完全初始化好），如果存在则将获取到的Bean对象并保存至二级Map，同时从当前三级Map移除该ObjectFactory实现类
    - 示例：两个Bean出现循环依赖，A依赖B，B依赖A；当我们去依赖查找A，在实例化后初始化前会先生成一个ObjectFactory对象（可获取当前正在初始化A）保存在上面的singletonFactories中，初始化的过程需注入B；接下来去查找B，初始B的时候又要去注入A，又去查找A，这时可以通过singletonFactories直接拿到正在初始化的A，那么就可以完成B的初始化，然后继续A的初始化，这样就避免出现循环依赖
    - 为什么需要上面的二级Map：
      - 因为通过三级Map获取Bean会有相关SmartInstantiationAwareBeanPostProcessor#getEarlyBeanReference(..) 的处理，避免重复处理，处理后返回的可能是一个代理对象
      - 例如在循环依赖中一个Bean可能被多个Bean依赖， A -> B（也依赖 A） -> C -> A，当你获取A时，后续B和C都要注入A，没有上面的二级Map的话，三级Map保存的ObjectFactory实现类会被调用两次，会重复处理，可能出现问题，这样做在性能上也有所提升
    - 参考：https://zhuanlan.zhihu.com/p/62382615

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
    - singleton：单个Bean实例，一个BeanFactory有且仅有一个实例
    - prototype：每次依赖查找和依赖注入生成新Bean对象，每次调用getBean()都会返回一个新的实例
    - request：将Spring Bean存储在ServletRequest上下文中，每次HTTP请求都会创建一个新的Bean，该作用域仅适用于WebApplicationContext环境
    - session：将Spring Bean存储在HttpSession中，同一个HTTP Session共享一个Bean，不同的HTTP Session使用不同的Bean。该作用域仅适用于WebApplicationContext环境
    - application：将Spring Bean存储在ServletContext中

### bean的生命周期（重要）

  - 生命周期：
    - Spring Bean元信息配置阶段：通过面向资源（XML 或 Properties）、面向注解、面向API进行配置
    - Spring Bean元信息解析阶段：对上一步的配置元信息进行解析，解析成BeanDefinition对象，该对象包含定义Bean的所有信息，用于实例化一个Spring Bean
    - Spring Bean元信息注册阶段：将BeanDefinition配置元信息 保存至BeanDefinitionRegistry的ConcurrentHashMap集合中
    - Spring BeanDefinition合并阶段：定义的Bean可能存在层次关系，则需要将它们进行合并，存在相同配置则覆盖父属性，最终生成一个RootBeanDefinition对象
    - Spring Bean的实例化阶段：首先通过类加载器加载出一个Class对象，通过它的构造器创建一个实例对象，构造器注入在此处会完成。在实例化阶段Spring提供了实例化前后两个扩展点（InstantiationAwareBeanPostProcessor的postProcessBeforeInstantiation、postProcessAfterInstantiation方法）
    - Spring Bean属性赋值阶段：在Spring实例化后，需要对其相关属性进行赋值，注入依赖的对象。首先获取该对象所有属性与属性值的映射，可能已定义，也可能需要注入，在这里都会进行赋值（反射机制）
    - Aware接口回调阶段：如果Spring Bean是Spring提供的Aware接口类型（例如BeanNameAware、ApplicationContextAware），这里会进行接口的回调，注入相关对象（例如beanName、ApplicationContext）
    - Spring Bean初始化阶段：调用Spring Bean配置的初始化方法，执行顺序：@PostConstruct标注方法、实现InitializingBean接口的afterPropertiesSet()、自定义初始化方法。在初始化阶段Spring提供了初始化前后两个扩展点（BeanPostProcessor的postProcessBeforeInitialization、postProcessAfterInitialization方法）
    - Spring Bean初始化完成阶段：在所有的Bean初始化后，Spring会再次遍历所有初始化好的单例Bean对象
    - Spring Bean销毁阶段：当Spring应用上下文关闭或者你主动销毁某个Bean时则进入Spring Bean的销毁阶段，执行顺序：@PreDestroy注解的销毁动作、实现了DisposableBean接口的Bean的回调、destroy-method自定义的销毁方法
    - Spring垃圾收集（GC）
  - 在配置bean时，通过init-method和destroy-method属性为bean指定初始化和销毁方法
  - BeanDefinition定义：
    - BeanDefinition是Spring Bean的“前身”，其内部包含了初始化一个Bean的所有元信息，在Spring初始化一个Bean的过程中需要根据该对象生成一个Bean对象并进行一系列的初始化工作
  
### Spring应用上下文（ApplicationContext）的生命周期
  
  - Spring应用上下文启动准备阶段：设置相关属性，例如启动时间、状态标识、Environment对象
  - BeanFactory初始化阶段：初始化一个BeanFactory对象，加载出BeanDefinition；设置相关组件：ClassLoader类加载器、表达式语言处理器、属性编辑器，并添加BeanPostProcessor处理器等
  - BeanFactory后置处理阶段：主要是执行BeanFactoryPostProcessor和BeanDefinitionRegistryPostProcessor的处理，对BeanFactory和BeanDefinitionRegistry进行后置处理
  - BeanFactory注册BeanPostProcessor阶段：主要初始化BeanPostProcessor类型的Bean（依赖查找），在Spring Bean生命周期的许多节点都能见到该类型的处理器
  - 初始化内建Bean：初始化当前Spring应用上下文的MessageSource对象（国际化文案相关）、ApplicationEventMulticaster事件广播器对象、ThemeSource对象
  - Spring事件监听器注册阶段：获取到所有的ApplicationListener事件监听器进行注册，并广播早期事件
  - BeanFactory初始化完成阶段：初始化所有还未初始化的Bean（不是抽象、单例模式、不是懒加载方式）
  - Spring应用上下文刷新完成阶段：清除当前Spring应用上下文中的缓存，并发布上下文刷新事件
  - Spring应用上下文启动阶段：需要主动调用AbstractApplicationContext#start()方法，会调用所有Lifecycle的start()方法，最后会发布上下文启动事件
  - Spring应用上下文停止阶段：需要主动调用AbstractApplicationContext#stop() 方法，会调用所有Lifecycle的stop()方法，最后会发布上下文停止事件
  - Spring应用上下文关闭阶段：发布当前Spring应用上下文关闭事件，销毁所有的单例Bean，关闭底层BeanFactory容器
 
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

### Spring注解

  - Spring模式注解：
    - @Component：通用组件
    - @Controller：Web控制器组件
    - @Service：业务逻辑层组件
    - @Repository：持久化层组件
    - @Configuration：配置类模式注解
    - Spring模式注解都是@Component的派生注解，@Component注解是一个通用组件注解，标注这个注解后表明你需要将其作为一个Spring Bean进行使用，而其他注解都有各自的作用，例如@Controller及其派生注解用于Web场景下处理HTTP请求，@Configuration注解通常会将这个Spring Bean作为一个配置类，也会被CGLIB提供，帮助实现AOP特性
  - 装配注解：
    - @ImportResource：替换XML元素<import>
    - @Import：导入Configuration类
    - @ComponentScan：扫描指定package下标注Spring模式注解的类
  - 依赖注入注解：
    - @Autowired：Bean依赖注入
      - 根据类型实现自动装配
      - 构造器、普通字段(即使是非public)、一切具有参数的方法都可以应用@Autowired注解
      - 默认情况下，所有使用@Autowired注解的属性都需要被设置。当Spring找不到匹配的bean装配属性时，会抛出异常
      - 若某一属性允许不被设置，可以设置@Autowired注解的required属性为false
      - 默认情况下，当IOC容器里存在多个类型兼容的bean时，Spring会尝试匹配bean的id值是否与变量名相同，如果相同则进行装配。如果bean的id值不相同，通过类型的自动装配将无法工作。此时可以在@Qualifier注解里提供bean的名称。
    - @Qualifier：细粒度的@Autowired依赖查找
    - @Resource注解：要求提供一个bean名称的属性，若该属性为空，则自动采用标注处的变量或方法名作为bean的名称
    - @Inject注解：@Inject和@Autowired注解一样也是按类型注入匹配的bean，但没有reqired属性
  - @Enable模块驱动：
    - @EnableWebMvc：启动整个Web MVC模块
    - @EnableTransactionManagement：启动整个事务管理模块
    - @EnableCaching：启动整个缓存模块
    - @EnableAsync：启动整个异步处理模块
    - 这类注解底层原理就是通过@Import注解导入相关类（Configuration Class、 ImportSelector接口实现、ImportBeanDefinitionRegistrar接口实现），来实现引入某个模块或功能
  - 条件注解：
    - @Conditional：引入某个Bean的条件限定
    - @Profile：基于@Conditional实现，限定Bean的Spring应用环境
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
  - @Bean的处理流程：
    - Spring应用上下文生命周期，在BeanDefinition（@Component注解、XML配置）的加载完后，会执行所有BeanDefinitionRegistryPostProcessor类型的处理器，Spring内部有一个 ConfigurationClassPostProcessor处理器，它会对所有的配置类进行处理，解析其内部的注解（@PropertySource、@ComponentScan、@Import、@ImportResource、@Bean），其中@Bean注解标注的方法会生成对应的BeanDefinition对象并注册
  
### Spring事件机制
  
  - 主要角色：
    - Spring事件：org.springframework.context.ApplicationEvent，实现了java.util.EventListener接口
    - Spring事件监听器：org.springframework.context.ApplicationListener，实现了java.util.EventObject类
    - Spring事件发布器：org.springframework.context.ApplicationEventPublisher
    - Spring事件广播器：org.springframework.context.event.ApplicationEventMulticaster
  - Spring内建的事件：
    - ContextRefreshedEvent：Spring应用上下文就绪事件
    - ContextStartedEvent：Spring应用上下文启动事件
    - ContextStoppedEvent：Spring应用上下文停止事件
    - ContextClosedEvent：Spring应用上下文关闭事件
  - Spring应用上下文就是一个ApplicationEventPublisher事件发布器，其内部有一个ApplicationEventMulticaster事件广播器（被观察者），里面保存了所有的ApplicationListener事件监听器（观察者）。Spring应用上下文发布一个事件后会通过ApplicationEventMulticaster事件广播器进行广播，能够处理该事件类型的ApplicationListener事件监听器则进行处理
  - @EventListener的工作原理：
    - @EventListener用于标注在方法上面，该方法则可以用来处理Spring的相关事件
    - Spring内部有一个处理器EventListenerMethodProcessor，它实现了SmartInitializingSingleton接口，在所有的Bean（不是抽象、单例模式、不是懒加载方式）初始化后，Spring会再次遍历所有初始化好的单例Bean对象时会执行该处理器对该Bean进行处理。在EventListenerMethodProcessor中会对标注了@EventListener注解的方法进行解析，如果符合条件则生成一个 ApplicationListener事件监听器并注册
  
### 参考
  
  - https://www.cnblogs.com/lifullmoon/p/14422101.html#1-什么是-spring-framework-



