# Flume进阶

## Flume事务

  - Flume事务：
  
  ![Flume事务](./图片/Flume事务.PNG)
  
## Flume Agent内部原理

  - Flume Agent内部原理：
  
  ![Flume Agent内部原理](./图片/FlumeAgent内部原理.PNG)
  
  - 组件细节：
    - ChannelSelector：
      - ChannelSelector的作用就是选出Event将要被发往哪个Channel。
      - 共有两种类型，分别是Replicating（复制）和Multiplexing（多路复用）。
      - ReplicatingSelector会将同一个Event发往所有的Channel，Multiplexing会根据相应的原则，将不同的Event发往不同的Channel。
    - SinkProcessor:
      - SinkProcessor共有三种类型，分别是DefaultSinkProcessor 、LoadBalancingSinkProcessor 和 FailoverSinkProcessor。
      - DefaultSinkProcessor对应的是单个的Sink，LoadBalancingSinkProcessor和FailoverSinkProcessor对应的是Sink Group。
      - LoadBalancingSinkProcessor可以实现负载均衡的功能，FailoverSinkProcessor可以实现故障转移的功能。
      
## Flume拓扑结构

### 简单串联

  - Flume简单串联结构：
  
  ![Flume简单串联结构](./图片/Flume简单串联结构.PNG)
  
  - 注意细节：
    - 这种模式是将多个flume顺序连接起来了，从最初的source开始到最终sink的存储系统。
    - 此模式不建议桥接过多的flume，flume数量过多不仅会影响传输速率，而且一旦传输过程中某个节点flume宕机，会影响整个传输系统。
    
### 复制和多路复用

  - Flume复制和多路复用结构：
  
  ![Flume复制和多路复用结构](./图片/Flume复制和多路复用结构.PNG)
  
  - 注意细节：
    - Flume 支持将事件流向一个或者多个目的地。
    - 这种模式可以将相同数据复制到多个channel中，或者将不同数据分发到不同的channel中，sink可以选择传送到不同的目的地。
    
### 负载均衡和故障转移

  - Flume负载均衡和故障转移结构：
  
  ![Flume负载均衡和故障转移结构](./图片/Flume负载均衡和故障转移结构.PNG)
  
  - 注意细节：
    - Flume支持使用将多个sink逻辑上分到一个sink组，sink组配合不同的SinkProcessor可以实现负载均衡和错误恢复的功能。
  
### 聚合

  - Flume聚合结构：
  
  ![Flume聚合结构](./图片/Flume聚合结构.PNG)
  
  - 注意细节：
    - 这种模式是我们最常见的，也非常实用，日常web应用通常分布在上百个服务器，大者甚至上千个、上万个服务器。产生的日志，处理起来也非常麻烦。
    - 用flume的这种组合方式能很好的解决这一问题，每台服务器部署一个flume采集日志，传送到一个集中收集日志的flume，再由此flume上传到hdfs、hive、hbase等，进行日志分析。
    

  
  
