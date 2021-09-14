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
    - 官方Starters命名规则是```spring-boot-starter-*，* ```表示特定类型的应用
    - 常用的Starter:
      - spring-boot-starter: Core starter, including auto-configuration support, logging and YAML
      - spring-boot-starter-test: Starter for testing Spring Boot applications with libraries including JUnit Jupiter, Hamcrest and Mockito
      - spring-boot-starter-validation: Starter for using Java Bean Validation with Hibernate Validator
      - spring-boot-starter-web: Starter for building web, including RESTful, applications using Spring MVC. Uses Tomcat as the default embedded container
      - spring-boot-starter-actuator: Starter for using Spring Boot’s Actuator which provides production ready features to help you monitor and manage your application

## 配置类

  - @Configuration注解用于基于Java的配置类
  - @Import注解可以引入其他的配置类，或者通过@ComponentScan注解自动引入所有Spring组件
  - @ImportResource注解可以加载XML配置文件

## 自动配置

  - Spring Boot会基于添加的jar依赖自动配置Spring应用
  - 禁用某个自动配置类：
    - 在@SpringBootApplication中使用exclude属性指定需要禁用的配置类
      ```
      @SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
      public class MyApplication {
      
      }
      ```
    - 如果类不在类路径上，可以使用excludeName属性，并指定该类的全限定名

## Rest API设计

  - 简介：
    - RESTful架构是一种架构风格，定义软件如何通过网络通信。客户端请求资源，服务端响应资源
    - 资源映射到URIs，对资源的操作映射到HTTP POST, GET, PUT, PATCH, DELETE等方法
  - API设计任务：
    - 设计一个健壮的REST API需要的活动：
      - 确定资源：
        - 根据实际应用中涉及的实体确定对应的资源，例如：CatalogClient, CatalogImage, and CatalogMetaDatum
      - 创建一个资源模型
        - REST的基础概念是资源，所有的设计围绕资源
        - 资源模型是描述各资源之间的关系，并且资源是分层的，因此REST接口应该反应这种层次
          ![image](https://user-images.githubusercontent.com/46510621/132990550-88924be5-5716-427f-bc1e-defe6d4ced21.png)
        - 对应的分层URIs:
          ![image](https://user-images.githubusercontent.com/46510621/132990591-c551798d-159a-4864-b4ca-26af59a29b3a.png)
      - 将资源模型形式化为对象模型
        - 在确定资源及资源模型之后，使用对象模型建模
        - 每种资源创建一个对应的类
      - 创建资源的JSON模式（如果使用JSON）
        - JSON模式JSON文档校验，提供API中JSON数据的显示的描述
      - 编写对资源进行的操作 
      - 将对象模型转换为URLs
        - 资源URI模版：
          ![image](https://user-images.githubusercontent.com/46510621/132991507-9fdcb1ea-6391-407d-9c89-6f1212283a3d.png)
      - 将操作映射为HTTP方法和查询参数
        - 常用的请求方法：
          ![image](https://user-images.githubusercontent.com/46510621/132990011-a917bdc2-3b40-49ce-9bfb-794162918e1f.png)  
      - 决定如何表示传输的数据（JSON, XML或其他格式）
      - 定义描述资源的模式
    - 设计规则：
      - 使用名词表示资源，复数表示集合，单数表示单个的资源
        - 资源是对象，名词表示对象。
        - URI是资源的标识符，URL是资源的标识符和位置
        - 使用动词格式的RPC API和名词格式的RESTful API区别：
          ```
          // using a verb - RPC Style
          http://www.nowhere.com/imageclient/getClientsById
          // using a noun - RESTful
          http://www.nowhere.com/imageClient/{client-id}
          ```
      - HTTP请求方法定义对资源的操作
      - 所有资源和通信是无状态的，不需要使用本地缓存数据，URL参数，会话变量维护状态
      - 为API指定一个版本号
        - 通过使用版本号保证在更新API时，旧的API可以继续工作，并逐渐切换到新的API
                - /表示层次关系
      - URIs中使用-代替_
      - URIs中使用小写字母
      - URIs中不要包含文件扩展名来表明文件类型
      - URIs中使用查询变量过滤

### 异常处理

  - @ExceptionHandler：
    - 用于@Controller级别，定义方法处理异常，并使用@ExceptionHandler注解
      ```
        @ExceptionHandler(CustomException.class)
        public BaseResponse<Void> handlerCustomException(CustomException e) {
            return BaseResponse.error(e.getCode(), e.getMessage());
        }
      ```
    - 这种方式的主要缺点：@ExceptionHandler注解只对特定的Controller有效，不是全局有效。
  - HandlerExceptionResolver：
    - 处理应用抛出的任意异常，可以实现统一的异常处理机制
    - ResponseStatusExceptionResolver：
      - 在自定义异常上使用@ResponseStatus注解，并将这些异常映射到HTTP状态码
        ```
          @ExceptionHandler(CustomException.class)
          @ResponseStatus(HttpStatus.BAD_REQUEST)
          public BaseResponse<Void> handlerCustomException(CustomException e) {
              return BaseResponse.error(e.getCode(), e.getMessage());
          }
        ```

## 参考

  - https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#using
  - https://rapidapi.com/blog/how-to-build-an-api-with-java/
  - https://codeburst.io/spring-boot-rest-microservices-best-practices-2a6e50797115
  - https://www.baeldung.com/rest-with-spring-series
