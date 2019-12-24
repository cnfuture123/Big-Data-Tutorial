# Kafka架构深入

## Kafka工作流程

  - Kafka工作流程：
  
  ![Kafka工作流程](./图片/Kafka工作流程.md)
  
  - Kafka中消息是以topic进行分类的，生产者生产消息，消费者消费消息，都是面向topic的。
  - topic是逻辑上的概念，而partition是物理上的概念。
  - 每个partition对应于一个log文件，该log文件中存储的就是producer生产的数据。
  - Producer生产的数据会被不断追加到该log文件末端，且每条数据都有自己的offset。
  - 消费者组中的每个消费者，都会实时记录自己消费到了哪个offset，以便出错恢复时，从上次的位置继续消费。
    
## Kafka文件存储机制

  - Kafka文件存储机制：
  
  ![Kafka文件存储机制](./图片/Kafka文件存储机制.md)
  
  - 由于生产者生产的消息会不断追加到log文件末尾，为防止log文件过大导致数据定位效率低下，Kafka采取了分片和索引机制将每个partition分为多个segment。
  - 每个segment对应两个文件".index"文件和".log"文件。
  - 这些文件位于一个文件夹下，该文件夹的命名规则为：topic名称+分区序号。
  - index和log文件以当前segment的第一条消息的offset命名。
  - ".index"文件存储大量的索引信息，".log"文件存储大量的数据。
  - 索引文件中的元数据指向对应数据文件中message的物理偏移地址。
    
## Kafka生产者

### Kafka生产者分区策略

  - 分区的原因：
    - 方便在集群中扩展，每个Partition可以通过调整以适应它所在的机器，而一个topic又可以有多个Partition组成，因此整个集群就可以适应任意大小的数据了。
    - 可以提高并发，因为可以以Partition为单位读写了。
  - 分区的原则：
    - 指明partition的情况下，直接将指明的值直接作为partiton值。
    - 没有指明partition值但有key的情况下，将key的hash值与topic的partition数进行取余得到partition值。
    - 既没有partition值又没有key值的情况下，第一次调用时随机生成一个整数（之后每次调用在这个整数上自增），将这个值与topic可用的partition总数取余得到partition值，也就是常说的round-robin算法。
    
### 数据可靠性保证

  - 为保证producer发送的数据，能可靠的发送到指定的topic，topic的每个partition收到producer发送的数据后，都需要向producer发送ack（acknowledgement 确认收到） 。如果
producer收到ack，就会进行下一轮的发送，否则重新发送数据。
  - 生产者数据传送：
  
  ![生产者数据传送](./图片/生产者数据传送.PNG)
  
  - 副本数据同步策略
  
  ![副本数据同步策略](./图片/副本数据同步策略.PNG)
  
  - Kafka选择了第二种方案, 因为Kafka的每个分区都有大量的数据，第一种方案会造成大量数据的冗余。虽然第二种方案的网络延迟会比较高，但网络延迟对Kafka的影响较小。
    
  
  
