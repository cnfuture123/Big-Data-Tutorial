# 开发SpringBoot应用

## 依赖

  - 添加web依赖：
    ```
    <dependencies>
      <dependency>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-starter-web</artifactId>
      </dependency>
    </dependencies>
    ```
  
## 编写代码

  - 开发Demo:
    ```
    @RestController
    @EnableAutoConfiguration
    public class MyApplication {
        @RequestMapping("/")
        String home() {
            return "Hello World!";
        }
        public static void main(String[] args) {
            SpringApplication.run(MyApplication.class, args);
        }
    }
    ```
    - 注解说明：
      - @RestController: 提示这是一个Web @Controller, 因此Spring使用它处理访问的web请求
      - @RequestMapping: 提供路由信息

## 依赖管理

  - Spring Boot会自动管理依赖的版本，并且这些依赖版本是匹配的，用户也可以指定某个版本去覆盖Sring Boot提供的版本
  - Sring Boot支持使用Maven, Gradle, Ant构建项目
  - Starters：
    - 一组方便使用的依赖描述，它包括很多项目中需要使用的依赖。例如：如果使用Spring和JPA访问数据库，可以使用spring-boot-starter-data-jpa依赖描述
    - 官方Starters命名规则是spring-boot-starter-*，* 表示特定类型的应用
    - 常用的Starter:
      - spring-boot-starter: Core starter, including auto-configuration support, logging and YAML
      - spring-boot-starter-test: Starter for testing Spring Boot applications with libraries including JUnit Jupiter, Hamcrest and Mockito
      - spring-boot-starter-validation: Starter for using Java Bean Validation with Hibernate Validator
      - spring-boot-starter-web: Starter for building web, including RESTful, applications using Spring MVC. Uses Tomcat as the default embedded container
      - spring-boot-starter-actuator: Starter for using Spring Boot’s Actuator which provides production ready features to help you monitor and manage your application

## 配置类

  - @Configuration注解用于基于Java的配置类

## 参考

  - https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#upgrading
