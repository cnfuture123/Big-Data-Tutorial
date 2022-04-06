
## 概述

  - Reactor项目的主要artifact是reactor-core
  - Reactor引入可组合的实现了Publisher反应型，并提供了丰富的操作符：Flux and Mono
    - Flux对象表示一个包含N个元素的序列
    - Mono对象表示单值或空元素（0/1）

## Flux

  - Flux转换流程：
    
    <img width="892" alt="image" src="https://user-images.githubusercontent.com/46510621/161702862-e649406f-ed18-47e2-afb9-5a7f0e06f194.png">

  - ```Flux<T>```是一个标准的```Publisher<T> ```，表示一个0-N个元素的异步序列，可以由完成信号或错误终止，相应的调用下游Subscriber的onNext, onComplete, and onError方法

## Mono

  - Mono转换流程：
    
    <img width="892" alt="image" src="https://user-images.githubusercontent.com/46510621/161704035-b4ac27fd-8beb-4c3f-a5e8-01781dfd1d7c.png">
    
  - ```Mono<T>```是一种专用的```Publisher<T>```，通过onNext信号发送最多一个元素，onComplete信号表示成功完成，onError信号表示错误发生

## Flux和Mono使用示例

  - 创建一个String序列：
    ```
    Flux<String> seq1 = Flux.just("foo", "bar", "foobar");

    List<String> iterable = Arrays.asList("foo", "bar", "foobar");
    Flux<String> seq2 = Flux.fromIterable(iterable);
    
    Mono<String> noData = Mono.empty(); 
    Mono<String> data = Mono.just("foo");
    ```
  - subscribe方法：
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

## 创建一个序列

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
     
## 线程和调度器
    
  - 概述：
    - Reactor是并发无关的，它不强制实施并发模型
    - 获取一个Flux或Mono不意味它在一个专用的线程运行，相反大多数操作符继续在前一个操作符执行的线程中工作
    - 在Reactor中，执行模型由Scheduler决定的，Scheduler类似于ExecutorService，负责调度任务
  - Schedulers类提供静态方法，访问如下执行上下文：
    - 没有执行上下文(Schedulers.immediate())：提交的Runnable任务将会在当前线程上直接执行
    - 单个，可重用的线程（Schedulers.single())：为所有调用方重用相同的线程，直到Scheduler被弃用；如果需要为每次调用分配一个专用线程，每次调用时使用Schedulers.newSingle()
    - 无界、弹性的线程池(Schedulers.elastic())：调度器会动态创建工作线程，线程数无上界，会隐藏背压问题并导致线程过多的问题，被Schedulers.boundedElastic()取代
    - 有界、弹性的线程池(Schedulers.boundedElastic())：它根据需要创建新的工作线程池并重用空闲的线程，它可以创建的线程数有上限（默认值为CPU内核数x10），达到上限后，最多能提交100000个任务将被排队，并在线程可用时重新调度
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
        
