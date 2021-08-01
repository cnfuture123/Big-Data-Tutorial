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
  - Spring MVC框架：
    - Model-View-Controller框架
    - 使用@Controller or @RestController注解的Beans处理HTTP请求
    - @RequestMapping注解将请求映射到对应的方法
    - HttpMessageConverters：Spring MVC使用这个接口转换HTTP请求和响应，支持自定义
    
  
