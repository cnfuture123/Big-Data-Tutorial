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
    - 前置通知：
      - 
      
        
