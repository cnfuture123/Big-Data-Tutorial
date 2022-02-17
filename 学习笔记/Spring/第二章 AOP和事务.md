## AOP概述

  - AOP(Aspect-Oriented Programming，面向切面编程)：是一种新的方法论，是对传统OOP(Object-Oriented Programming，面向对象编程)的补充。
  - AOP编程操作的主要对象是切面(aspect)，而切面模块化横切关注点。
  - 在应用AOP编程时，可以明确的定义这个功能应用在哪里，以什么方式应用，并且不必修改受影响的目标类。
  - AOP的好处：
    - 每个事物逻辑位于一个位置，代码不分散，便于维护和升级
    - 业务模块更简洁，只包含核心业务代码
  - AOP图解：
  
    ![图片](https://user-images.githubusercontent.com/46510621/110959575-ca6f1300-8388-11eb-9544-bb3c0d678b7e.png)

## AOP术语

  - 横切关注点：从每个方法中抽取出来的同一类非核心业务，每个关注点体现为一个通知方法。
  - 切面(Aspect)：封装横切关注点信息的类。
  - 通知(Advice)：切面必须要完成的各个具体工作
  - 目标(Target)：被通知的对象
  - 代理(Proxy)：向目标对象应用通知之后创建的代理对象
  - 连接点(Joinpoint)：横切关注点在程序代码中的具体体现，对应程序执行的某个特定位置。例如：类某个方法调用前、调用后、方法捕获到异常后等。
  - 切入点(pointcut)：如果把连接点看作数据库中的记录，那么切入点就是查询条件——AOP可以通过切入点定位到特定的连接点。切入点通过org.springframework.aop.Pointcut接口进行描述，它使用类和方法作为连接点的查询条件。
  - 图解：
  
    ![图片](https://user-images.githubusercontent.com/46510621/111643148-aeacb680-8839-11eb-8c60-0d759934da32.png)

## AspectJ

  - 简介：
    - AspectJ：Java社区里最完整最流行的AOP框架。
    - 在Spring2.0以上版本中，可以使用基于AspectJ注解或基于XML配置的AOP。
  - 配置：
    - <aop:aspectj-autoproxy>：当Spring IOC容器检测到bean配置文件中的<aop:aspectj-autoproxy>元素时，会自动为与AspectJ切面匹配的bean创建代理
  - 用AspectJ注解声明切面：
    - 要在Spring中声明AspectJ切面，只需要在IOC容器中将切面声明为bean实例。
    - 在AspectJ注解中，切面只是一个带有@Aspect注解的Java类，它往往要包含很多通知，通知是标注有某种注解的简单的Java方法。
    - AspectJ支持5种类型的通知注解：
      - @Before：前置通知，在方法执行之前执行
      - @After：后置通知，在方法执行之后执行
      - @AfterRunning：返回通知，在方法返回结果之后执行
      - @AfterThrowing：异常通知，在方法抛出异常之后执行
      - @Around：环绕通知，围绕着方法执行

## AOP细节

  - 切入点表达式：
    - 作用：通过表达式的方式定位一个或多个具体的连接点。
    - 语法：
      - 格式：
        ```
        execution([权限修饰符] [返回值类型] [简单类名/全类名] [方法名]([参数列表]))
        ```
      - 举例：
        ```
        1.ArithmeticCalculator接口中声明的所有方法
          execution(* com.cn.spring.ArithmeticCalculator.*(..))
          第一个"*"代表任意修饰符及任意返回值
          第二个"*"代表任意方法
          ".."匹配任意数量、任意类型的参数。
        2.ArithmeticCalculator接口的所有公有方法
          execution(public * ArithmeticCalculator.*(..))
        3.参数类型为double，double类型的方法
          execution(public double ArithmeticCalculator.*(double, double))
        ```
    - 切入点表达式应用到实际的切面类中:
    
      ![图片](https://user-images.githubusercontent.com/46510621/111645989-372c5680-883c-11eb-8cfc-03006498ef57.png)

  - 连接点细节：
    - 连接点的具体信息，例如：当前连接点所在方法的方法名、当前传入的参数值等，这些信息都封装在JoinPoint接口的实例对象中。
  - 通知：
    - 概述：
      - 在具体的连接点上要执行的操作
      - 一个切面可以包括一个或者多个通知
      - 通知所使用的注解的值往往是切入点表达式
    - 分类：
      - 前置通知（@Before）：在方法执行之前执行的通知
      - 后置通知（@After）：后置通知是在连接点完成之后执行的，使用注解
      - 返回通知（@AfterReturning）：无论连接点是正常返回还是抛出异常，后置通知都会执行。如果只想在连接点返回的时候记录日志，应使用返回通知代替后置通知
      - 异常通知（@AfterThrowing）：只在连接点抛出异常时才执行异常通知，将throwing属性添加到@AfterThrowing注解中，也可以访问连接点抛出的异常
      - 环绕通知（@Around）：
        - 所有通知类型中功能最为强大的，能够全面地控制连接点，甚至可以控制是否执行连接点。
        - 对于环绕通知来说，连接点的参数类型必须是ProceedingJoinPoint。它是 JoinPoint的子接口，允许控制何时执行，是否执行连接点。
        - 在环绕通知中需要明确调用ProceedingJoinPoint的proceed()方法来执行被代理的方法。
  - 指定切面优先级：
    - 切面的优先级可以通过实现Ordered接口或利用@Order注解指定。
      - 实现Ordered接口，getOrder()方法的返回值越小，优先级越高
      - 若使用@Order注解，序号出现在注解中
      
## 以XML方式配置切面

  - 配置细节：
    - 在bean配置文件中，所有的Spring AOP配置都必须定义在<aop:config>元素内部。对于每个切面而言，都要创建一个<aop:aspect>元素来为具体的切面实现引用后端bean实例。
  - 声明切入点：
    - 切入点使用<aop:pointcut>元素声明
    - 切入点必须定义在<aop:aspect>元素下，或者直接定义在<aop:config>元素下：
      - 定义在<aop:aspect>元素下：只对当前切面有效
      - 定义在<aop:config>元素下：对所有切面都有效
    - 基于XML的AOP配置不允许在切入点表达式中用名称引用其他切入点
  - 声明通知：
    - 在aop名称空间中，每种通知类型都对应一个特定的XML元素，method属性指定切面类中通知方法的名称

    ![图片](https://user-images.githubusercontent.com/46510621/111656182-406df100-8845-11eb-8f09-01adf2337091.png)

## JdbcTemplate

  - 概述：Spring的JdbcTemplate看作是一个小型的轻量级持久化层框架，为不同类型的JDBC操作提供模板方法，可以在尽可能保留灵活性的情况下，将数据库存取的工作量降到最低。 
  - JdbcTemplate所需要的JAR包：
    - spring-jdbc-4.0.0.RELEASE.jar
    - spring-orm-4.0.0.RELEASE.jar
    - spring-tx-4.0.0.RELEASE.jar
  - JdbcTemplate对象：
    
    ![图片](https://user-images.githubusercontent.com/46510621/111656773-cbe78200-8845-11eb-938f-c1c2d327b01c.png)

  - 持久化操作：
    - 增删改：JdbcTemplate.update(String, Object...)
    - 批量增删改：JdbcTemplate.batchUpdate(String, List<Object[]>)
      - Object[]封装了SQL语句每一次执行时所需要的参数
      - List集合封装了SQL语句多次执行时的所有参数
    - 查询单行：JdbcTemplate.queryForObject(String, RowMapper<Department>, Object...)
    - 查询多行：JdbcTemplate.query(String, RowMapper<Department>, Object...)
    
## 事务管理

  - 事务概述：
    - 定义：事务是逻辑上的一组操作，要么都执行，要么都不执行。所谓事务管理，其实就是“按照给定的事务规则来执行提交或者回滚操作”。
    - 特性：ACID
      - 原子性：事务是最小的执行单位，不允许分割。事务的原子性确保动作要么全部完成，要么完全不起作用
      - 一致性： 执行事务前后，数据保持一致，保证数据的准确性
      - 隔离性： 并发访问数据库时，一个用户的事务不被其他事务所干扰，各并发事务之间数据库是独立的
      - 持久性: 一个事务被提交之后。它对数据库中数据的改变是持久的，即使数据库发生故障也不应该对其有任何影响
  - Spring事务管理接口：
    - PlatformTransactionManager：（平台）事务管理器
      - Spring并不直接管理事务，而是提供了多种事务管理器 ，他们将事务管理的职责委托给Hibernate或者JTA等持久化机制所提供的相关平台框架的事务来实现。
      - Spring事务管理器的接口是PlatformTransactionManager，通过这个接口，Spring为各个平台如JDBC、Hibernate等都提供了对应的事务管理器，但是具体的实现就是各个平台自己的事情了。
      - PlatformTransactionManager根据不同持久层框架所对应的接口实现类：
        
        ![image](https://user-images.githubusercontent.com/46510621/111726501-10a20600-88a4-11eb-994e-ed55df5d97ee.png)

    - TransactionDefinition：事务定义信息(事务隔离级别、传播行为、是否超时、是否只读、回滚规则)
    - TransactionStatus：事务运行状态
  - 事务隔离级别：
    - 并发事务带来的问题：
      - 脏读（Dirty read）：当一个事务正在访问数据并且对数据进行了修改，而这种修改还没有提交到数据库中，这时另外一个事务也访问了这个数据，然后使用了这个数据。
      - 不可重复读（Unrepeatableread）: 指在一个事务内多次读同一数据。在第一个事务中的两次读数据之间，由于第二个事务的修改导致第一个事务两次读取的数据可能不太一样。
      - 幻读（Phantom read）: 幻读发生在一个事务（T1）读取了几行数据，接着另一个并发事务（T2）插入了一些数据时。在随后的查询中，第一个事务（T1）就会发现多了一些原本不存在的记录，就好像发生了幻觉一样，所以称为幻读。不可重复读的重点是修改，幻读的重点在于新增或者删除。
    - 隔离级别：
      - 读未提交（READ_UNCOMMITTED）：最低的隔离级别，允许读取尚未提交的数据变更，可能会导致脏读、幻读或不可重复读
      - 读已提交（READ_COMMITTED）：允许读取并发事务已经提交的数据，可以阻止脏读，但是幻读或不可重复读仍有可能发生
      - 可重复读（REPEATABLE_READ）：对同一字段的多次读取结果都是一致的，除非数据是被本身事务自己所修改，可以阻止脏读和不可重复读，但幻读仍有可能发生。
      - 串行化（SERIALIZABLE）：最高的隔离级别，完全服从ACID的隔离级别。所有的事务依次逐个执行，这样事务之间互相没有干扰。该级别可以防止脏读、不可重复读以及幻读。但是这将严重影响程序的性能。通常情况下也不会用到该级别。
  - 事务传播行为：
    
    ![图片](https://user-images.githubusercontent.com/46510621/111875965-63300f00-89d7-11eb-978d-0a5f457fc12b.png)
    
  - 事务超时属性：
    - 一个事务所允许执行的最长时间，如果超过该时间限制但事务还没有完成，则自动回滚事务。timeout参数中以int的值来表示超时时间，其单位是秒。
  - 事务只读属性：
    - 对事务性资源进行只读操作或者是读写操作，所谓事务性资源就是指那些被事务管理的资源，比如数据源、 JMS 资源，以及自定义的事务性资源等等
    - readOnly参数以boolean类型来表示该事务是否只读
  - 回滚规则：
    - 默认情况下，事务只有遇到运行期异常时才会回滚，而在遇到检查型异常时不会回滚
    - 但是你可以声明事务在遇到特定的检查型异常时像遇到运行期异常那样回滚。同样，你还可以声明事务遇到特定的异常不回滚，即使这些异常是运行期异常。
      


  
