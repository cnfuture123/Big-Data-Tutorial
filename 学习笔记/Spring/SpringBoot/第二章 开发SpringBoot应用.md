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

## 参考

  - https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#upgrading
