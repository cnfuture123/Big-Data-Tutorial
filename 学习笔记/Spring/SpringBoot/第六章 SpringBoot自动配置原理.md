## 自动配置

  - 概述：
    - SpringBoot自动装配定义：SpringBoot在启动时会扫描外部引用jar包中的META-INF/spring.factories文件，将文件中配置的类型信息加载到Spring容器，并执行类中定义的各种操作。对于外部 jar来说，只需要按照SpringBoot定义的标准，就能将自己的功能装置进SpringBoot
    - 自动装配可以简单理解为：通过注解或者一些简单的配置就能在Spring Boot的帮助下实现某块功能。比如你想要在项目中使用Redis，直接在项目中引入对应的starter即可
      ```
      <dependency>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-starter-data-redis</artifactId>
      </dependency>
      ```
  - 自动装配原理：
    - 核心依赖：spring-boot-starter-parent
      ```
      <parent>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-starter-parent</artifactId>
          <version>2.4.4</version>
          <relativePath/>
      </parent>
      ```
      - 它的父依赖：规定所有依赖的版本信息
        ```
        <parent>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-dependencies</artifactId>
          <version>2.4.4</version>
        </parent>
        ```
    - 核心注解@SpringBootApplication
      - 包含注解：
        ```
        @SpringBootConfiguration
        @EnableAutoConfiguration
        @ComponentScan(excludeFilters = { @Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class), @Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class) })
        ```
      - @SpringBootConfiguration: @Configuration允许在上下文中注册额外的bean或导入其他配置类，代表当前类是一个配置类
        ```
        @Configuration
        public @interface SpringBootConfiguration
        ```
      - @EnableAutoConfiguration: 启动SpringBoot的自动配置机制
        ```
        @AutoConfigurationPackage
        @Import(AutoConfigurationImportSelector.class)
        public @interface EnableAutoConfiguration 
        ```
        - AutoConfigurationPackage: 指定默认的包规则，将添加该注解的类所在的package作为自动配置package进行管理。当SpringBoot应用启动时默认会将启动类所在的package作为自动配置的package，然后使用@Import注解将其注入到ioc容器中
          ```
          @Import({Registrar.class})
          public @interface AutoConfigurationPackage
          ```
          
    - @EnableAutoConfiguration是开启自动装配的核心注解，EnableAutoConfiguration是通过AutoConfigurationImportSelector类实现自动装配核心功能
    - AutoConfigurationImportSelector类实现ImportSelector接口，并实现了这个接口中的selectImports方法，该方法主要用于获取所有符合条件的类的全限定类名，这些类需要被加载到IoC 容器中
    - getAutoConfigurationEntry方法调用链：
    
      ![Uploading image.png…]()

    - 总结：Spring Boot通过@EnableAutoConfiguration开启自动装配，通过SpringFactoriesLoader最终加载META-INF/spring.factories中的自动配置类实现自动装配，自动配置类其实就是通过@Conditional按需加载的配置类，想要其生效必须引入spring-boot-starter-xxx包实现起步依赖
  - 禁用某个自动配置类：
    - 在@SpringBootApplication中使用exclude属性指定需要禁用的配置类
      ```
      @SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
      public class MyApplication {
      
      }
      ```
    - 如果类不在类路径上，可以使用excludeName属性，并指定该类的全限定名

## 参考

  - https://cloud.tencent.com/developer/article/1912976
  - https://juejin.cn/post/7035528380753641508
