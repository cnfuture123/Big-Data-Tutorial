# SpringBoot特性

## SpringApplication

  - SpringApplication提供方便的方式去启动一个Spring应用：
    ```
    @SpringBootApplication
    public class MyApplication {
        public static void main(String[] args) {
            SpringApplication.run(MyApplication.class, args);
        }
    }
    ```
  - 懒加载：
    - SpringApplication允许懒加载，Beans在需要时创建，而不是在应用启动时创建
    - 懒加载的缺点是会延迟发现问题，不能在启动时发现问题
    - 启用懒加载配置：spring.main.lazy-initialization=true
  - 自定义SpringApplication: 例如关闭banner
    ```
    @SpringBootApplication
    public class MyApplication {
        public static void main(String[] args) {
            SpringApplication application = new SpringApplication(MyApplication.class);
            application.setBannerMode(Banner.Mode.OFF);
            application.run(args);
        }
    }
    ```
  
## 外部配置

  - 外部Application属性；
    - Spring Boot自动从如下路径加载application.properties和application.yaml：
      - 类路径，类路径/config包下
      - 当前目录，当前目录/config及其子目录
    - 属性占位符：${name}
      ```
      app.name=MyApp
      app.description=${app.name} is a Spring Boot application
      ```
  - YAML: JSON的超集，可以用来指定分层级的配置数据
    ```
    environments:
      dev:
        url: https://dev.example.com
        name: Developer Setup
      prod:
        url: https://another.example.com
        name: My Cool App
    ```
  
## 日志

  - Spring Boot使用Commons Logging打印内部日志。默认的配置提供了 Java Util Logging, Log4J2, and Logback。
  - 日志格式：
    ```
    2019-03-05 10:57:51.253  INFO 45469 --- [ost-startStop-1] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
    ```
    - Date and Time -> Log Level -> Process ID -> --- separator -> Thread name -> Logger name -> Log message
  - 文件输出：
    - Spring Boot默认只将日志输出到控制台，不会写入日志文件
    - 写入日志文件需要配置以下2个属性：
      - logging.file.path
      - logging.file.name
  - 文件滚动： 
    ![image](https://user-images.githubusercontent.com/46510621/127762124-8663f686-f8f9-47d7-b2f2-0f904768695c.png)
  - 日志级别：
    - TRACE, DEBUG, INFO, WARN, ERROR, FATAL, or OFF
      ```
      logging.level.root=warn
      logging.level.org.springframework.web=debug
      logging.level.org.hibernate=error
      ```

## JSON
  
  - Spring Boot集成3种JSON库：Gson, Jackson, JSON-B。Jackson是默认库
  
## 开发Web应用

  - 大多数时候使用spring-boot-starter-web去开发Web应用，spring-boot-starter-webflux用于开发响应式Web应用
  
### Spring MVC框架
  - Model-View-Controller框架
  - 使用@Controller or @RestController注解的Beans处理HTTP请求
  - @RequestMapping注解将请求映射到对应的方法
  - HttpMessageConverters：Spring MVC使用这个接口转换HTTP请求和响应，支持自定义
  - Json序列化和反序列化：
    - @JsonComponent注解可以直接使用在JsonSerializer, JsonDeserializer or KeyDeserializer实现上
    - 所有@JsonComponent beans会自动被注册为Jackson
  - 静态内容：
    - Spring Boot默认将静态内容配置在类路径/static (or /public or /resources or /META-INF/resources)等目录
    - 可以指定资源路径：
      ```
      spring.mvc.static-path-pattern=/resources/**
      ```
  - 欢迎页：首先在配置的静态资源路径下找index.html，如果没有，继续找index模板，然后使用它作为欢迎页
  - 路径匹配和内容协商：
    - Spring MVC将请求路径匹配到应用定义的mappings方法(@GetMapping, @PostMapping注解的方法)
    - Spring Boot禁用了后缀模式匹配，例如："GET /projects/spring-boot.json"不会匹配到@GetMapping("/projects/spring-boot")
    
### Spring WebFlux

  - Spring WebFlux使用HttpMessageReade和HttpMessageWriter接口去转换HTTP请求和响应
  - 静态内容：
    - 默认静态内容放在/static (or /public or /resources or /META-INF/resources)等目录
    - 可以通过实现自定义的WebFluxConfigurer，并重写addResourceHandlers方法
    - 指定资源路径：
      ```
      spring.webflux.static-path-pattern=/resources/**
      ```
  - Web过滤器：
    - Spring WebFlux提供WebFilter接口去提供过滤HTTP请求和响应数据
  - JAX-RS and Jersey：
    - JAX-RS编程模型开发REST接口，可以使用Jersey代替Spring MVC
    - 使用方式：
      - 引入spring-boot-starter-jersey依赖
      - 创建一个ResourceConfig类型的@Bean，注册所有的接口
        ```
        @Component
        public class MyJerseyConfig extends ResourceConfig {
            public MyJerseyConfig() {
                register(MyEndpoint.class);
            }
        }

        ```
  - 嵌入Servlet容器
    - Spring Boot支持嵌入的Tomcat, Jetty, Undertow服务器
    - 默认内置服务器监听HTTP请求的端口是8080
    - 注册Servlets, Filters, and Listeners:
      - 使用ServletRegistrationBean, FilterRegistrationBean, and ServletListenerRegistrationBean
      - Filters应该是有序的，通过@Order注解或者实现Ordered接口
      - 使用@ServletComponentScan可以启用被@WebServlet, @WebFilter, and @WebListener注解类的自动注册
  - 嵌入Reactive Server容器
    - 支持内置Reactor Netty, Tomcat, Jetty, and Undertow. 
    - 当自动配置Reactor Netty or Jetty时，Spring Boot会创建特殊的beans来提供HTTP资源给ReactorResourceFactory or JettyResourceFactory。
    
## 集成关系型数据库
    
### 配置数据源

  - 配置数据源：
    - javax.sql.DataSource接口提供了数据库连接的标准方法，传统的DataSource使用URL及证书建立数据库连接
    - Spring Boot支持自动配置嵌入的 H2, HSQL, and Derby数据库
  - 连接外部数据库：
    - 数据源配置：
      ```
      spring.datasource.url=jdbc:mysql://localhost/test
      spring.datasource.username=dbuser
      spring.datasource.password=dbpass
      ```
  - 连接池：
    - Spring Boot按如下顺序选择连接池：HikariCP -> Tomcat pooling DataSource -> Commons DBCP2 -> Oracle UCP
    - 使用spring-boot-starter-jdbc or spring-boot-starter-data-jpa starters时默认配置HikariCP依赖
    
### JPA and Spring Data JPA

  - Java Persistence API是将对象映射到关系型数据库的标准方式
  - spring-boot-starter-data-jpa提供Hibernate, Spring Data JPA, Spring ORM等依赖的支持
  
## 集成非关系型数据库

### Redis

  - spring-boot-starter-data-redis提供相关的依赖
  - 连接Redis:
    - 可以使用自动配置的RedisConnectionFactory, StringRedisTemplate或RedisTemplate实例连接Redis服务器
    
### MongoDB

  - 可以使用spring-boot-starter-data-mongodb and spring-boot-starter-data-mongodb-reactive配置依赖
  - 可以注入MongoDatabaseFactory连接MongoDB服务器，如果自定义了MongoClient，将会使用它配置一个合适的MongoDatabaseFactory
  - MongoClient可以用MongoClientSettings bean进行配置，如果MongoClientSettings没有配置，则使用spring.data.mongodb的属性在application.properties中进行配置
  
### Elasticsearch

  - spring-boot-starter-data-elasticsearch提供依赖
  - 使用REST客户端连接ES:
    - Spring Boot支持"High Level" client，如果类路径有这个依赖，Spring Boot会自动配置并注册一个RestHighLevelClient bean
    - 可以调整的参数配置：
      ```
      spring.elasticsearch.rest.uris=https://search.example.com:9200
      spring.elasticsearch.rest.read-timeout=10s
      spring.elasticsearch.rest.username=user
      spring.elasticsearch.rest.password=secret
      ```
  - 使用响应式REST客户端连接ES:
    

