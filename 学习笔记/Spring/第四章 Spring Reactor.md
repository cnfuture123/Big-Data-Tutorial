# Spring Reactor

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

## Reactor核心特性

  - 概述：
    - Reactor项目的主要artifact是reactor-core
    - Reactor引入可组合的实现了Publisher反应型，并提供了丰富的操作符：Flux and Mono
      - Flux对象表示一个包含N个元素的序列
      - Mono对象表示单值或空元素（0/1）

### Flux

  - Flux转换流程：
    
    <img width="892" alt="image" src="https://user-images.githubusercontent.com/46510621/161702862-e649406f-ed18-47e2-afb9-5a7f0e06f194.png">

  - ```Flux<T>```是一个标准的```Publisher<T> ```，表示一个0-N个元素的异步序列，可以由完成信号或错误终止，相应的调用下游Subscriber的onNext, onComplete, and onError方法

### Mono

  - Mono转换流程：
    
    <img width="892" alt="image" src="https://user-images.githubusercontent.com/46510621/161704035-b4ac27fd-8beb-4c3f-a5e8-01781dfd1d7c.png">
    
  - ```Mono<T>```是一种专用的```Publisher<T>```，通过onNext信号发送最多一个元素，onComplete信号表示成功完成，onError信号表示错误发生

### Flux和Mono使用示例

  - 创建一个String序列：
    ```
    Flux<String> seq1 = Flux.just("foo", "bar", "foobar");

    List<String> iterable = Arrays.asList("foo", "bar", "foobar");
    Flux<String> seq2 = Flux.fromIterable(iterable);
    
    Mono<String> noData = Mono.empty(); 
    Mono<String> data = Mono.just("foo");
    ```
  - subscribe方法：
    - 
    ```
    Flux<Integer> ints = Flux.range(1, 3); 
    ints.subscribe();
    
    Flux<Integer> ints = Flux.range(1, 4); 
    ints.subscribe(
      i -> System.out.println(i),
      error -> System.err.println("Error " + error),
      () -> System.out.println("Done")); 
    ```
  - BaseSubscriber:
    - BaseSubscriber的实例是一次性使用的，当它订阅到第二个Publisher时，就取消对第一个Publisher的订阅
    - 示例：
      ```
      public class SampleSubscriber<T> extends BaseSubscriber<T> {

        public void hookOnSubscribe(Subscription subscription) {
          System.out.println("Subscribed");
          request(1);
        }

        public void hookOnNext(T value) {
          System.out.println(value);
          request(1);
        }
      }
      ```
  - 反压和更新请求的方式：
    - 当实现反压时，消费端压力会通过发送一个请求到上游的方式传播到源。当前的请求和即是当前的需求或待处理的请求，需求上限为MAX_VALUE
    - 更新请求的操作符：
      - buffer(N)
      - request(2)
      - prefetch
      - limitRate
      - limitRequest

### 创建一个序列

  - 同步的generate：
    - 通过generate方法创建Flux是最简单的方式
    - 示例：
      ```
      Flux<String> flux = Flux.generate(
        AtomicLong::new,
          (state, sink) -> { 
          long i = state.getAndIncrement(); 
          sink.next("3 x " + i + " = " + 3*i);
          if (i == 10) sink.complete();
          return state; 
        }, (state) -> System.out.println("state: " + state)); 
      ```
  - 异步且多线程的create
    - 它暴露FluxSink，提供next, error, and complete方法。不同于generate，它没有基于状态的变量
    - 示例：
      ```
      Flux<String> bridge = Flux.create(sink -> {
          myEventProcessor.register( 
            new MyEventListener<String>() { 

              public void onDataChunk(List<String> chunk) {
                for(String s : chunk) {
                  sink.next(s); 
                }
              }

              public void processComplete() {
                  sink.complete(); 
              }
          });
      });
      ```
  - 异步且单线程的push
    - push处于generate和create之间，适用于处理来自单个生产者的事件
    - 示例：
      ```
      Flux<String> bridge = Flux.push(sink -> {
          myEventProcessor.register(
            new SingleThreadEventListener<String>() { 

              public void onDataChunk(List<String> chunk) {
                for(String s : chunk) {
                  sink.next(s); 
                }
              }

              public void processComplete() {
                  sink.complete(); 
              }

              public void processError(Throwable e) {
                  sink.error(e); 
              }
          });
      });
      ```
     
### 线程和调度器
    
  - 概述：
    - Reactor是并发无关的，它不强制实施并发模型
    - 获取一个Flux或Mono不意味它在一个专用的线程运行，相反大多数操作符继续在前一个操作符执行的线程中工作
    - 在Reactor中，执行模型由Scheduler决定的，Scheduler类似于ExecutorService，负责调度任务
  - Schedulers类提供静态方法，访问如下执行上下文：
    - 没有执行上下文(Schedulers.immediate())：提交的Runnable任务将会在当前线程上直接执行
    - 单个，可重用的线程（Schedulers.single())：为所有调用方重用相同的线程，直到Scheduler被弃用；如果需要为每次调用分配一个专用线程，每次调用时使用Schedulers.newSingle()
    - 无界、弹性的线程池(Schedulers.elastic())：会隐藏背压问题并导致线程过多的问题，被Schedulers.boundedElastic()取代
    - 有界、弹性的线程池(Schedulers.boundedElastic())：它根据需要创建新的工作线程池并重用空闲的线程，它对可以创建的后备线程数有上限（默认值为CPU内核数 x 10），达到上限后，最多 能提交100000个任务将被排队，并在线程可用时重新调度
    - 针对并行工作进行调整的固定工作线程池(Schedulers.parallel())：它创建和CPU核数相同多的工作线程
  - publishOn和subscribeOn：
    - Reactor提供了2种方式切换执行上下文：publishOn和subscribeOn
    - publishOn：
      - 它从上游获取信号，并在下游重播它们，同时从关联的调度器对工作线程执行回调
      - 它影响后续运算符的执行：
        - 切换执行上下文到Scheduler选择的某个线程
        - onNext调用按顺序进行，因此这会占用单个线程
        - 除非它们在特定的调度器上工作，否则publishOn之后的操作符继续在同一个线程中执行
      - 示例：
        ```
        Scheduler s = Schedulers.newParallel("parallel-scheduler", 4); 

        final Flux<String> flux = Flux
            .range(1, 2)
            .map(i -> 10 + i)  
            .publishOn(s)  
            .map(i -> "value " + i);  

        new Thread(() -> flux.subscribe(System.out::println)); 
        ```
    - subscribeOn:
      - subscribeOn应用于订阅阶段，无论将subscribeOn放置在链条的什么位置，它总是影响源的上下文
      - 示例：
        ```
        Scheduler s = Schedulers.newParallel("parallel-scheduler", 4); 

        final Flux<String> flux = Flux
            .range(1, 2)
            .map(i -> 10 + i)  
            .subscribeOn(s)  
            .map(i -> "value " + i);  

        new Thread(() -> flux.subscribe(System.out::println)); 
        ```


## 参考

  - https://spring.io/reactive
  - https://projectreactor.io/
