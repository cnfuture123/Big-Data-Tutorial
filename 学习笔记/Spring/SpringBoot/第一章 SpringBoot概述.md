# SpringBoot概述

## 微服务架构

  - 概述：微服务架构是将大型复杂系统按功能或者业务需求垂直切分成更小的子系统，这些子系统以独立部署的进程存在，它们之间通过轻量级的、跨语言的同步（比如REST，gRPC）或者异步（消息）网络调用进行通信
  - 微服务架构的优缺点：
    - 优点：
      - 每个服务足够内聚，聚焦一个指定的业务功能或业务需求
      - 开发简单、开发效率提高，微服务能够被小团队单独开发
      - 支持迭代开发或现代化增量开发
      - 支持水平缩放和细粒度缩放
    - 缺点：
      - 更高数量级的活动组件（服务、数据库、进程、容器、框架）
      - 复杂性从代码转移到基础设施
      - RPC调用和网络通信的大量增加
      - 整个系统的设计变得更加困难
      - 引入了分布式系统的复杂性
  - 何时使用微服务架构：
    - 大规模Web应用开发
    - 跨团队企业级应用协作开发
  - 微服务技术架构体系:
    - 示例图：

      ![image](https://user-images.githubusercontent.com/46510621/158558802-d5da0999-3ca5-471d-bd81-7e95ce3f73dc.png)

    - 服务发现：
      - 域名映射：开发人员开发了程序以后，会找运维配一个域名，服务的话通过DNS就能找到我们对应的服务。
        - 缺点：对负载均衡服务，可能会有相当大的性能问题
      - 服务注册：每一个服务都通过服务端内置的功能注册到注册中心，服务消费者不断轮询注册中心发现对应的服务，使用内置负载均衡调用服务
        - 缺点：对多语言环境不是很好，你需要单独给消费者的客户端开发服务发现和负载均衡功能
    - 网关：
      - 示意图：

        <img width="559" alt="image" src="https://user-images.githubusercontent.com/46510621/158559951-ca1265e9-e9e8-4a9c-b3c5-8f6db566ad4d.png">

      - 网关的作用：
        - 反向路由：将外部请求转换成内部具体服务调用
        - 安全认证：网络中会有很多恶意访问，譬如爬虫，譬如黑客攻击，网关维护安全功能
        - 限流熔断：当请求很多服务不堪重负，会让我们的服务不可用。限流熔断可以有效的避免这类问题
        - 日志监控：所有的外面的请求都会经过网关，就可以使用网关来记录日志信息
        - 灰度发布：
          - 定义：能够平滑过渡的一种发布方式，可以进行A/B测试，即让一部分用户继续用产品特性A，一部分用户开始用产品特性B，如果用户对B没有什么反对意见，逐步扩大范围，把所有用户都迁移到B上
          - 主要流程：
            - 定义目标：
              - 新功能验证：看这个新功能的指标是否能达到预期，或者是否会对产品造成损失
              - 新功能尝试：看这个功能是否符合用户需求，不符合则下线
            - 选定灰度策略：
              - 用户选择：地理位置、使用终端
              - 功能覆盖度：逐步功能开放还是全部功能开放
              - 提供用户数据反馈入口，让运营人员能够及时了解用户反馈
            - 灰度发布上线：
              - 设定分流规则
              - 灰度发布新版本
              - 运营数据采集分析
              - 分流规则微调
              - 灰度发布>产品完善>新一轮灰度发布>完整发布
          - 灰度发布问题：
            - 以偏概全：选择的样本不具有代表性，样本用户使用习惯并没有涉及所有升级的核心功能
            - 用户参与度不够
          - 具体实现方法：
            - 利用Nginx配置负载控制
            - 使用http头信息判断+权重（灰度值）：需要分析ip地址段，用户代理，Cookie中的信息。根据Cookie查询version值，如果该version值为v1转发到host1，为v2转发到host2，都不匹配的情况下转发到默认配置
            - 使用灰度发布工具：阿里acm、NepxionDiscovery、Istio
    - 配置中心：
      - 示例图：

        <img width="780" alt="image" src="https://user-images.githubusercontent.com/46510621/158564963-e0f2ebad-4141-4009-9bca-ad4c38253471.png">
        
    - 通讯方式：
      - RPC和REST对比：

        <img width="685" alt="image" src="https://user-images.githubusercontent.com/46510621/158583524-513d1445-1f8b-4352-a746-919cb68acc37.png">

    - 监控预警：
      - 一般监控分为如下层次：
        
        <img width="528" alt="image" src="https://user-images.githubusercontent.com/46510621/158583797-92d2329e-7620-4dfc-9eb7-53b2d38b0dd4.png">

      - 微服务可分为5个监控点：
        - 日志监控
        - Metrics监控
        - 健康检查
        - 调用链检查
        - 告警系统

## 特点

  - 创建独立的Spring应用
  - 内置Tomcat, Jetty or Undertow等服务
  - 提供推断式的starter，简化构建项目的配置
  - 自动配置Spring和第三方类库
  - 提供生产级别的特性，例如：指标，健康检查，外部配置等
  - 不生成代码，不需要XML配置
  
## 注解

  - @SpringBootApplication: 用于Spring Boot主类，标识这是一个Spring Boot应用，用来开启Spring Boot的各项能力
    - 这个注解是@SpringBootConfiguration、@EnableAutoConfiguration、@ComponentScan这三个注解的组合
  - @EnableAutoConfiguration: 使Spring Boot自动配置注解，Spring Boot根据当前类路径下的包或者类来配置Bean
  - @Configuration: 用于类级别，提供配置信息，代替applicationContext.xml配置文件
  - @SpringBootConfiguration: 这个注解就是@Configuration注解的变体，只是用来修饰Spring Boot配置而已
  - @ComponentScan: 开启组件扫描，即自动扫描包路径下的@Component注解注册Bean实例到context中，代替配置文件中的component-scan配置
  - @Conditional: 用来标识一个Spring Bean或者Configuration配置文件，当满足指定的条件才开启配置
  - @ConditionalOnBean: 组合@Conditional注解，当容器中有指定的Bean才开启配置
  - @ConditionalOnMissingBean: 组合@Conditional注解，当容器中没有指定的Bean才开启配置
  - @ConditionalOnClass: 组合@Conditional注解，当容器中有指定的Class才开启配置
  - @ConditionalOnMissingClass: 组合@Conditional注解，当容器中没有指定的Class才开启配置
  - @AutoConfigureAfter: 用在自动配置类上面，表示该自动配置类需要在另外指定的自动配置类配置完之后
    ```
    @AutoConfigureAfter(DataSourceAutoConfiguration.class)
    public class MybatisAutoConfiguration {}
    ```
  - @AutoConfigureBefore: 表示该自动配置类需要在另外指定的自动配置类配置之前
  - @Import: 用来导入一个或者多个@Configuration注解修饰的类
  - @ImportResource: 用来导入一个或者多个Spring配置文件，这对Spring Boot兼容老项目非常有用
  - @Bean: 用于方法级别，表明一个方法产生bean，由Spring容器管理
  - @Service: 用于类级别，表明被注解的类是一个service类，例如：业务逻辑，调用外部API
  - @Repository: 用于类级别，表明被注解的类是一个repository类，它是连接数据库的DAO(Data Access Object)
  - @Controller: 用于类级别，表明被注解的类是一个controller类


