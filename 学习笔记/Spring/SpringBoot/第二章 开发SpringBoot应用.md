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
  - @ControllerAdvice：
    - Spring 3.2支持带@ControllerAdvice注解的全局的@ExceptionHandler
      ```
      @Slf4j
      @ControllerAdvice
      public class GlobalExceptionHandler {
      
          @ResponseStatus(HttpStatus.BAD_REQUEST)
          @ExceptionHandler(CustomException.class)
          public BaseResponse<Void> handlerCustomException(CustomException e) {
              return BaseResponse.error(e.getCode(), e.getMessage());
          }
          
          @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
          @ExceptionHandler(Exception.class)
          public BaseResponse<Void> handlerException(Exception e) {
              log.error("Uncapped exception", e);
              return BaseResponse.error(9999, e.getMessage());
          }
      }
      ```
    - @ControllerAdvice注解使多个，分散的@ExceptionHandlers成为一个全局的异常处理组件，这种机制简单并且灵活：
      - 可以控制响应体和状态码
      - 可以将几种异常映射到同一个方法，一起被处理
      - 更好的利用RESTful ResposeEntity响应

### 常用注解：

  - @RequestBody：
    - 将HttpRequest body映射到java对象
      ```
      @PostMapping
      public BaseResponse<Boolean> create(@RequestBody StudentVO studentVO) {
          log.info("create student {}", studentVO);
          return BaseResponse.success(studentService.create(studentVO));
      }
      ```
    - Spring会自动将JSON反序列化为Java类型，但是Java类型的格式需要和客户端传的JSON格式相匹配
  - @ResponseBody：
    - 将Controller返回的对象自动序列化JSON，并传递给HttpResponse对象
    - @RestController注解默认包含了@ResponseBody
    - 设置Content Type：
      - 可以使用@RequestMapping的produce, consume属性指定Content Type
        ```
        // JSON
        @PostMapping(value = "student", produces = MediaType.APPLICATION_JSON_VALUE)
        @ResponseBody
        public BaseResponse<StudentVO> create(@RequestBody StudentVO studentVO) {
            log.info("create student {}", studentVO);
            return BaseResponse.success(studentService.create(studentVO));
        }
        
        // XML
        @PostMapping(value = "student", produces = MediaType.APPLICATION_JSON_VALUE)
        @ResponseBody
        public BaseResponse<StudentVO> create(@RequestBody StudentVO studentVO) {
            log.info("create student {}", studentVO);
            return BaseResponse.success(studentService.create(studentVO));
        }
        ```
      - 请求头设置不同的Accept参数会得到不同格式的响应
        ```
        // JSON
        curl -i \ 
        -H "Accept: application/json" \ 
        -H "Content-Type:application/json" \ 
        -X POST --data 
          '{"username": "johnny", "password": "password"}' "https://localhost:8080/.../content"
        response: {"text":"JSON Content!"}
        
        // XML
        curl -i \
        -H "Accept: application/xml" \
        -H "Content-Type:application/json" \
        -X POST --data
          '{"username": "johnny", "password": "password"}' "https://localhost:8080/.../content"
        response: <BaseResponse><text>XML Content!</text></BaseResponse>
        ```
        
### 重要概念

  - ResponseEntity：
    - 表示整个HTTP响应：status code, headers, and body
    - ResponseEntity是泛型，可以使用任意类型作为响应体
    - 示例：
      ```
      @GetMapping("/hello")
      ResponseEntity<String> hello() {
          return new ResponseEntity<>("Hello World!", HttpStatus.OK);
      }
      ```
    - 替代方式：
      - @ResponseBody：返回的结果作为HTTP响应体
      - @ResponseStatus：可以自定义HTTP状态
  - HTTP状态码：
    - HTTP状态码是一个3位的数字码，用于客户端的解析，并作出相应的行为。客户端可以是：发送请求的浏览器，另一个服务器，或正在运行的应用
    - HTTP状态码分为5类：
      ![image](https://user-images.githubusercontent.com/46510621/133407980-d1814a5f-0c66-40d4-ba62-065403b68707.png)
    - 常用的状态码：
      - 200: 请求成功，可以是GET, POST, PUT, PATCH, and DELETE任意操作
      - 201: 请求成功，并创建新的资源
      - 301: 请求的资源已经移动，301响应中Location头，客户端可以用这个Location重新请求资源
      - 400: 请求的语法不对
      - 401: 请求的用户没有认证
      - 403: 服务端由于不足的访问权限拒绝请求，即使被认证，也可能缺少访问某些资源的权限
      - 404: 请求的资源不存在
      - 405: 服务端不支持请求中使用的方法
      - 408: 请求超时
      - 413: 请求负载太大
      - 500: 服务端内部错误，通常是服务端bug或没有捕获的异常
  - 实体相关概念：
    - Entity: 基本和数据表一一对应，一个实体一张表
    - BO(Business Object): 业务对象，将业务逻辑封装为一个对象，通过调用Dao方法，结合Po或Vo进行业务操作
    - VO(Value Object): 值对象，通常用于业务层之间的数据传递。主要体现在视图的对象，对于一个WEB页面将整个页面的属性封装成一个VO对象，在控制层与视图层进行传输交换
    - PO(Persistant Object): 持久层对象，数据库表中的记录在java对象中的显示状态，最形象的理解就是一个PO就是数据库中的一条记录
    - DTO(Data Transfer Object): 数据传输对象，接口之间传递的数据封装，一是能提高数据传输的速度(减少了传输字段)，二能隐藏后端表结构
    - POJO(Plain Ordinary Java Object): 简单无规则java对象，最基本的Java Bean，属性加上属性的get和set方法
    - DAO(Data Access Object): 

## 参考

  - https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#using
  - https://rapidapi.com/blog/how-to-build-an-api-with-java/
  - https://codeburst.io/spring-boot-rest-microservices-best-practices-2a6e50797115
  - https://www.baeldung.com/rest-with-spring-series
