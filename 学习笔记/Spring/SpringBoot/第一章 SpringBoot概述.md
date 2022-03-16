# SpringBoot概述

## 微服务架构

  - 概述：微服务架构是将大型复杂系统按功能或者业务需求垂直切分成更小的子系统，这些子系统以独立部署的进程存在，它们之间通过轻量级的、跨语言的同步（比如REST，gRPC）或者异步（消息）网络调用进行通信
  - 微服务架构的优缺点：
    - 优点：
      - 更快的开发速度
      - 支持迭代开发或现代化增量开发
      - 支持水平缩放和细粒度缩放
    - 缺点：
      - 

## 特点

  - 创建独立的Spring应用
  - 内置Tomcat, Jetty or Undertow等服务
  - 提供推断式的starter，简化构建项目的配置
  - 自动配置Spring和第三方类库
  - 提供生产级别的特性，例如：指标，健康检查，外部配置等
  - 不生成代码，不需要XML配置
  
## 注解

  - @SpringBootApplication: 用于Spring Boot主类，标识这是一个Spring Boot应用，用来开启Spring Boot的各项能力
    - 这个注解是@SpringBootConfiguration、@EnableAutoConfiguration、@ComponentScan这三个注解的组合
  - @EnableAutoConfiguration: 使Spring Boot自动配置注解，Spring Boot根据当前类路径下的包或者类来配置Bean
  - @Configuration: 用于类级别，提供配置信息，代替applicationContext.xml配置文件
  - @SpringBootConfiguration: 这个注解就是@Configuration注解的变体，只是用来修饰Spring Boot配置而已
  - @ComponentScan: 开启组件扫描，即自动扫描包路径下的@Component注解注册Bean实例到context中，代替配置文件中的component-scan配置
  - @Conditional: 用来标识一个Spring Bean或者Configuration配置文件，当满足指定的条件才开启配置
  - @ConditionalOnBean: 组合@Conditional注解，当容器中有指定的Bean才开启配置
  - @ConditionalOnMissingBean: 组合@Conditional注解，当容器中没有指定的Bean才开启配置
  - @ConditionalOnClass: 组合@Conditional注解，当容器中有指定的Class才开启配置
  - @ConditionalOnMissingClass: 组合@Conditional注解，当容器中没有指定的Class才开启配置
  - @AutoConfigureAfter: 用在自动配置类上面，表示该自动配置类需要在另外指定的自动配置类配置完之后
    ```
    @AutoConfigureAfter(DataSourceAutoConfiguration.class)
    public class MybatisAutoConfiguration {}
    ```
  - @AutoConfigureBefore: 表示该自动配置类需要在另外指定的自动配置类配置之前
  - @Import: 用来导入一个或者多个@Configuration注解修饰的类
  - @ImportResource: 用来导入一个或者多个Spring配置文件，这对Spring Boot兼容老项目非常有用
  - @Bean: 用于方法级别，表明一个方法产生bean，由Spring容器管理
  - @Service: 用于类级别，表明被注解的类是一个service类，例如：业务逻辑，调用外部API
  - @Repository: 用于类级别，表明被注解的类是一个repository类，它是连接数据库的DAO(Data Access Object)
  - @Controller: 用于类级别，表明被注解的类是一个controller类


