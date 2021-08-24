# Spring Boot Actuator

## 启用生产环境的特性

  - spring-boot-actuator模块提供Spring Boot生产环境的特性，添加spring-boot-starter-actuator可以启用这些特性

## Endpoints

  - Spring Boot包含一系列内置的endpoints，并且支持自定义
  - 每个endpoint可以启用或禁用，并可以通过HTTP或JMX暴露出去
  - 大多数应用使用HTTP暴露接口，/actuator作为前缀，例如：health endpoint映射为/actuator/health
  - 常用的endpoints:
    - beans: 展示应用中所有的Spring beans列表
    - configprops: @ConfigurationProperties列表
    - env: Spring’s ConfigurableEnvironment中的属性
    - health: 应用健康信息
    - httptrace: HTTP trace信息，默认最近100个HTTP请求响应信息，要求HttpTraceRepository bean
    - info: 任意的应用信息
    - loggers: 展示或修改loggers配置
    - metrics: 'metrics'信息
    - mappings: @RequestMapping路径的列表
    - quartz: Quartz调度任务的信息
    - shutdown: 应用逐渐停止
    - threaddump: 进行线程转储
  - 启用Endpoints:
    - 使用```management.endpoint.<id>.enabled```属性可以控制是否启用该endpoint
      ```
      management.endpoint.shutdown.enabled=true
      ```
  - 暴露endpoints:
    - 使用include, exclude属性可以指定哪些endpoints暴露出去
      ![image](https://user-images.githubusercontent.com/46510621/129752516-effd0a66-a510-478f-93bb-078c2006db63.png)
    - 示例：
      ```
      management.endpoints.web.exposure.include=*
      management.endpoints.web.exposure.exclude=env,beans
      ```
  - 健康信息：
    - 健康信息用来检查运行应用的状态
    - health endpoint依赖于management.endpoint.health.show-details and management.endpoint.health.show-components属性配置决定
      ![image](https://user-images.githubusercontent.com/46510621/129756768-90be45ec-72ec-42a3-a9f1-c9fc559de2bd.png)
    - 健康信息由 HealthContributorRegistry收集，默认包含ApplicationContext中的所有HealthContributor
    - HealthContributor可以是HealthIndicator或CompositeHealthContributor
    - Spring Boot自动配置一些HealthIndicators，并可以通过management.health.'key'.enabled启用或禁用
  
## 通过HTTP监控和管理
  
  - 自定义Endpoint路径：
    ```
    management.endpoints.web.base-path=/manage
    ```
  - 自定义服务器端口
    ```
    management.server.port=8081
    ```
  - 禁用HTTP Endpoints:
    ```
    management.server.port=-1
    or 
    management.endpoints.web.exposure.exclude=*
    ```

## Loggers
  
  - Actuator可以在运行时查看或配置日志级别
  - 日志级别：
    - 包括：TRACE, DEBUG, INFO, WARN, ERROR, FATAL, OFF, null
    - null表明没有显式配置日志级别
  - 可以通过HTTP Post请求改变日志级别：
    ```
    {
        "configuredLevel": "DEBUG"
    }
    ```
  
## Metrics

  - 支持的度量指标:
    - JVM指标:
      - 内存和缓冲池数据
      - 垃圾收集数据
      - 线程利用率
      - 加载和卸载的类数量
    - 系统指标：system. process.
      - CPU指标
      - 文件描述指标
      - 应用运行时间
    - 日志指标：
      - Logback和Log4J2的指标
    - Spring MVC指标：
      ![image](https://user-images.githubusercontent.com/46510621/129928443-19a2aafc-4cd5-4b5a-8a31-2e962003208b.png)
  - Metrics Endpoint:
    - /actuator/metrics会展示可用的度量指标
    - 进一步查看具体的度量信息，例如：/actuator/metrics/jvm.memory.max
    
