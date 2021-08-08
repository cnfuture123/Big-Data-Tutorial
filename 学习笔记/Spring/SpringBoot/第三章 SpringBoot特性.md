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
    

