# Kafka架构深入

## Kafka工作流程

  - Kafka工作流程：
  
  ![Kafka工作流程](./图片/Kafka工作流程.PNG)
  
  - Kafka中消息是以topic进行分类的，生产者生产消息，消费者消费消息，都是面向topic的。
  - topic是逻辑上的概念，而partition是物理上的概念。
  - 每个partition对应于一个log文件，该log文件中存储的就是producer生产的数据。
  - Producer生产的数据会被不断追加到该log文件末端，且每条数据都有自己的offset。
  - 消费者组中的每个消费者，都会实时记录自己消费到了哪个offset，以便出错恢复时，从上次的位置继续消费。
    
## Kafka文件存储机制

  - Kafka文件存储机制：
  
  ![Kafka文件存储机制](./图片/Kafka文件存储机制.PNG)
  
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
  - ISR(in-sync replica set): 
    - Leader维护了一个动态的in-sync replica set (ISR)，意为和leader保持同步的follower集合。
    - 当ISR中的follower完成数据的同步之后，leader就会给follower发送ack。
    - 如果follower长时间未向leader同步数据 ， 则该follower将被踢出ISR， 该时间阈值由replica.lag.time.max.ms 参数设定。
    - Leader发生故障之后，就会从ISR中选举新的leader。
  - ack应答机制：
    - Kafka为用户提供了三种可靠性级别，用户根据对可靠性和延迟的要求进行权衡。
    - acks参数配置：
      - 0： producer不等待broker的ack，这一操作提供了一个最低的延迟，broker一接收到还没有写入磁盘就已经返回，当broker故障时有可能丢失数据。
      - 1： producer等待broker的ack，partition的leader落盘成功后返回ack。如果在follower同步成功之前leader故障，那么将会丢失数据。
      - -1(all): producer等待broker的ack，partition的leader和follower全部落盘成功后才返回ack。但是如果在follower同步完成后，broker发送ack之前，leader发生故障，那么会造成数据重复。
    - 故障处理细节:
      - Log文件中的HW和LEO:
      
      ![Log文件中的HW和LEO](./图片/Log文件中的HW和LEO.PNG)
      
      - follower故障: follower发生故障后会被临时踢出ISR，待该follower恢复后，follower会读取本地磁盘记录的上次的HW，并将log文件高于HW的部分截取掉，从HW开始向leader进行同步。等该follower的LEO大于等于该Partition的HW，即follower追上leader之后，就可以重新加入ISR了。
      - leader故障: leader发生故障之后，会从ISR中选出一个新的leader，之后，为保证多个副本之间的数据一致性，其余的follower会先将各自的log文件高于 HW的部分截掉，然后从新的leader同步数据。注意：这只能保证副本之间的数据一致性，并不能保证数据不丢失或者不重复。
      
### Exactly Once 语义

  - 将服务器的ACK级别设置为-1，可以保证Producer到Server之间不会丢失数据，即At Least Once语义。相对的，将服务器ACK级别设置为0，可以保证生产者每条消息只会被发送一次，即At Most Once语义。
  - At Least Once可以保证数据不丢失，但是不能保证数据不重复；相对的， At Most Once可以保证数据不重复，但是不能保证数据不丢失。
  - 对于一些非常重要的信息，比如说交易数据，下游数据消费者要求数据既不重复也不丢失，即 Exactly Once 语义。
  - 0.11版本的Kafka，引入了一项重大特性：幂等性。所谓的幂等性就是指Producer不论向Server发送多少次重复数据，Server端都只会持久化一条。
  - 幂等性结合 At Least Once 语义，就构成了Kafka的Exactly Once语义。
  - 要启用幂等性，只需要将 Producer 的参数中 enable.idompotence 设置为 true 即可。
  - Kafka的幂等性实现其实就是将原来下游需要做的去重放在了数据上游。
  - 开启幂等性的Producer在初始化的时候会被分配一个PID，发往同一Partition的消息会附带Sequence Number。而Broker端会对<PID, Partition, SeqNumber>做缓存，当具有相同主键的消息提交时，Broker只会持久化一条。
  - 但是PID重启就会变化，同时不同的Partition也具有不同主键，所以幂等性无法保证跨分区跨会话的Exactly Once。
  
## Kafka消费者

      
  
  
