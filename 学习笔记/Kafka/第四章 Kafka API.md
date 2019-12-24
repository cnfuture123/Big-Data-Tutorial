# Kafka API

## Producer API

  - 消息发送流程:
    - Kafka的Producer发送消息采用的是异步发送的方式。
    - 在消息发送的过程中，涉及到了两个线程main线程和Sender线程，以及一个线程共享变量RecordAccumulator。
    - main线程将消息发送给RecordAccumulator，Sender线程不断从RecordAccumulator中拉取消息发送到Kafka broker。
    - 消息发送流程:
    
    ![消息发送流程](./图片/消息发送流程.PNG)
    - 相关参数：
      - batch.size：只有数据积累到batch.size之后，sender才会发送数据。
      - linger.ms：如果数据迟迟未达到batch.size，sender等待linger.time之后就会发送数据。
      
### 异步发送API

  - 需要用到的类：
    - KafkaProducer：需要创建一个生产者对象，用来发送数据。
    - ProducerConfig：获取所需的一系列配置参数。
    - ProducerRecord：每条数据都要封装成一个ProducerRecord对象。
  - 不带回调函数的API:
  
  ![不带回调函数的API](./代码/CustomProducer.java)

  - 带回调函数的API:
  
  ![带回调函数的API](./代码/ProducerCallBack.java)
  - 回调函数会在producer收到ack时调用，为异步调用，该方法有两个参数，分别是RecordMetadata 和 Exception。
  - 注意：消息发送失败会自动重试，不需要我们在回调函数中手动重试。
  
    
