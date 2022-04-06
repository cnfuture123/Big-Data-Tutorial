## 简介

  - 概述：
    - Project Reactor和Spring技术栈组合可以共同构建企业级，低延时，高吞吐量的响应式系统
    - 响应式处理是一种范式，使开发者可以构建非阻塞、异步的应用，并且支持反压机制
  - Project Reactor：
    - Project Reactor是一个Spring生态中响应式技术栈的基础，用于Spring WebFlux, Spring Data, and Spring Cloud Gateway等
  - 基于Spring Boot的响应式微服务：
    
    <img width="752" alt="image" src="https://user-images.githubusercontent.com/46510621/161686597-ce446e4c-a69b-41d5-9165-e47d2f2f5758.png">
    
## Project Reactor

  - Reactor概述：
    - 响应器是JVM非阻塞、响应式编程的基础，支持高效的需求管理（即反压机制）
    - 直接和Java 8的功能API集成，包括：CompletableFuture、Stream、Duration
    - 提供了异步的API:
      - Flux: for N elements
      - Mono: for 0|1 elements
    - 支持非阻塞的进程间通信：reactor-netty。Reactor Netty为HTTP、TCP、UDP提供了网络引擎，也支持编解码
  - Maven依赖：
    ```
    <dependencies>
        <dependency>
            <groupId>io.projectreactor</groupId>
            <artifactId>reactor-core</artifactId> 
        </dependency>
        
        <dependency>
            <groupId>io.projectreactor</groupId>
            <artifactId>reactor-test</artifactId> 
            <scope>test</scope>
        </dependency>
    </dependencies>
    ```
    
## 响应式编程简介

  - 概述：
    - 响应式编程是一种异步编程范式，关注于数据流和变化的传播
  - 阻塞会浪费资源：
    - 通常有2种方式可以提高程序性能：
      - 并行度：使用更多的线程和硬件资源
      - 更高效的使用当前的资源
    - 阻塞会浪费资源，因为线程会闲置，等待数据
  - 异步编程：
    - Java提供2种异步编程的模型：
      - Callbacks：
        - 异步方法不需要返回值，而是在结果可用时调用额外的回调参数（a lambda or anonymous class）
          ```
          public void onSuccess();
          public void onError(Throwable error);
          ```
        - 缺点：回调很难组合在一起，导致代码难以阅读和维护
      - Futures：
        - 异步方法立即返回```Future<T>```，异步进程计算一个T值，并使用Future对象来包装它。这个值并不是立即可用的，可以轮询对象，直到该值可用
        - 缺点：
          - Future对象调用get()又进入阻塞状态
          - 不支持延迟计算
          - 缺少高级错误处理的支持
  - 响应式编程：
    - 响应式编程的目标是解决传统异步方法的缺点，其关注点在如下方面：
      - 可组合性和可读性
        - 可组合性指的是协调多个异步任务，可以使用前一个任务的结果作为后续任务的输入，也可以以fork-join模式同时运行多个任务。此外，可以复用异步任务作为更高级系统的独立组件
      - 将数据作为流处理，可以使用丰富的操作符处理
        - 响应式应用处理数据的流程可以类比于一个装配线，原材料从源（Publisher）进入，最终作为一个完成的产品准备发送给消费者（Subscriber）
        - 在反应器中，每个操作符向一个Publisher中增加一个行为，并把上一步的Publisher包装为一个新的实例。整个链条因此产生：从第一个Publisher产生数据，然后沿着链条移动，被每一个连接转换，最后一个Subscriber完成这个过程
      - 在订阅之前不会进行计算
        - 通过订阅操作，可以将Publisher绑定到Subscriber，从而触发整个链中的数据流
        - 其内部实现是由Subscriber的一个请求信号向上游传播，一直到源Publisher
      - 支持反压
        - 向上游传播信号是用于实现反压：
          - 一个subscriber可以在无界模式工作，让源以最快的速度推送所有数据
          - 或者使用请求机制向源发送信号通知最多可以接收n个元素

## 参考

  - https://spring.io/reactive
  - https://projectreactor.io/docs/core/release/reference/#producing

