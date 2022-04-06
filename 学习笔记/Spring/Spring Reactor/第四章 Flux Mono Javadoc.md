## Flux Javadoc

  - fromIterable:
    - 语法：
      ```
      public static <T> Flux<T> fromIterable(Iterable<? extends T> it)
      ```
    - 说明：
      
      <img width="1189" alt="image" src="https://user-images.githubusercontent.com/46510621/161938056-3273bf43-cbe9-443e-b60e-853975852d5f.png">
      
  - buffer:
    - 语法：
      ```
      public final Flux<List<T>> buffer(int maxSize)
      ```
    - 说明：
    
      <img width="1090" alt="image" src="https://user-images.githubusercontent.com/46510621/161940516-92283688-33be-45ac-9c03-f8757250fff1.png">
    
## Mono Javadoc
     
  - fromCallable:
    - 语法：
      ```
      public static <T> Mono<T> fromCallable(Callable<? extends T> supplier)
      ```
    - 说明：
    
      <img width="942" alt="image" src="https://user-images.githubusercontent.com/46510621/161943043-42a08265-72bd-489c-a80f-0722bd03a5c0.png">

  - timeout:
    - 语法：
      ```
      public final Mono<T> timeout(Duration timeout)
      ```
    - 说明：
      
      <img width="663" alt="image" src="https://user-images.githubusercontent.com/46510621/161945038-de347f67-b233-4f0e-bca8-1d09149db4ff.png">

  - onErrorResume:
    - 语法：
      ```
      public final Mono<T> onErrorResume(Function<? super Throwable,? extends Mono<? extends T>> fallback)
      ```
    - 说明：
      
      <img width="813" alt="image" src="https://user-images.githubusercontent.com/46510621/161946567-d7e81b37-3156-49af-8c63-b295cb68ad47.png">

  - just:
    - 语法：
      ```
      public static <T> Mono<T> just(T data)
      ```
    - 说明：
      
      <img width="652" alt="image" src="https://user-images.githubusercontent.com/46510621/161947120-9f4be24e-0964-4e29-829c-d8e5b80a0a12.png">

  - subscribeOn:
    - 语法：
      ```
      public final Mono<T> subscribeOn(Scheduler scheduler)
      ```
    - 说明：
      
      <img width="1244" alt="image" src="https://user-images.githubusercontent.com/46510621/161948732-eeeacbbd-6b9f-46f8-93b8-83ff0253f516.png">

  - block:
    - 语法：
      ```
      public T block()
      ```
    - 说明：
      
      <img width="1272" alt="image" src="https://user-images.githubusercontent.com/46510621/161949668-139ec29e-d32b-4aeb-877a-d445053b540e.png">

## 参考

  - https://projectreactor.io/docs/core/release/api/reactor/core/publisher/Flux.html
