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
    - 使用management.endpoint.<id>.enabled属性可以控制是否启用该endpoint
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
    - 




