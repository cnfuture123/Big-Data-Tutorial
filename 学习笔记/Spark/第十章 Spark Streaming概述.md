# Spark Streaming概述

  - Spark Streaming用于流式数据的处理。
  - Spark Streaming支持的数据输入源很多，例如：Kafka、Flume、Twitter、ZeroMQ和简单的TCP套接字等。结果也能保存在很多地方，如HDFS，数据库等。
  - 数据输入后可以用Spark的高度抽象原语如：map、reduce、join、window等进行运算。
  - Spark Streaming使用离散化流(Discretized Stream)作为抽象表示，叫作DStream。DStream是连续的流数据，由连续的RDDs表示。其中每个RDD包含一个特定时间间隔的数据。对DStream进行的操作实际转换为对底层一系列RDD的操作。
  
## Spark Streaming特点
  
  - Ease of Use
  - Fault Tolerance
  - Spark Integration
  
## Spark Streaming架构

  - Spark Streaming架构:
  
  ![SparkStreaming架构](./图片/SparkStreaming架构.PNG)
  
  - 核心组件：
    - Cluster Manager：集群管理器，负责资源管理和分配。
    - Driver：运行应用程序的主函数，初始化SparkContext(StreamingContext)，SparkContext连接集群管理器，并负责任务的调度。
    - Executor：执行具体的任务，包括数据的存储和计算。
    
## Spark Streaming基本使用

  - 初始化StreamingContext：
    ```
    val sparkConf = new SparkConf().setAppName("app")
    val ssc = new StreamingContext(sparkConf, Seconds(3))
    ```
    - SparkConf：用于Spark应用的配置（key-value pairs）
    - StreamingContext：使用Spark Streaming功能的入口
    - Seconds(3)：流数据划分批次的时间间隔
  - 程序启动和结束：
    - ssc.start()：启动程序，开始接收数据，并进行数据处理
    - ssc.awaitTermination()：等待程序结束，手动停止或出错停止
    - ssc.stop()：结束程序
  - 程序的部署：
    - 有管理器的集群：Standalone, Mesos, Yarn等
    - 将应用程序打包为Jar
    - 为Driver&Executor配置足够的内存：
      ```
      sparkConf.set("spark.driver.memory", "2G")
      sparkConf.set("spark.driver.cores", "2")
      sparkConf.set("spark.executor.memory", "5G")
      sparkConf.set("spark.executor.cores", "5")
      ```
    - 如果需要可以配置checkpoint，用来提高容错性。Spark Streaming支持两种方式：元数据检查点和数据检查点。元数据检查点用于Driver失败的恢复，数据检查点用于有状态数据的恢复。
      - 示例：
        ```
        ssc.checkpoint("checkpointDirectory") // set checkpoint directory
        ```
    - 配置应用程序Dirver的自动重启，为了自动从Driver故障中恢复，运行流应用程序的部署设施必须能监控Driver进程，如果失败了能够重启它。
  - 程序的提交：
    ```
    bin/spark-submit 
      --class <main-class> 
      --deploy-mode client/master
      --master <master-url>
      --files <./jaas.conf,./jaas-zk.conf,krb5.conf,user.keytab> //上传配置文件到集群
      --jars <./a.jar,./b.jar,c.jar> //上传依赖到集群
      --num-executors 5 
      --queue <job-queue> //指定程序运行的队列
    ```
    - 支持的master类型：
      - local: 本地一个线程运行程序
      - local[K]: 本地K个线程运行程序
      - local[*]: 本地用机器核数的线程运行程序
      - spark://HOST:PORT: 连接Spark Standalone集群
      - mesos://HOST:PORT: 连接Mesos集群
      - yarn: 连接Yarn集群
  - 反压机制：
    - 背景：
      - 当一个批次数据的处理时间大于批次时间间隔时，意味着数据处理速度跟不上数据接收速度，会造成数据在接收端的积压。当积累的数据过多时，如果数据存储采用MEMORY_ONLY模式就会导致OOM，采用MEMORY_AND_DISK多余的数据溢写到磁盘上反而会增加数据读取时间。
      - Spark Streaming从1.5开始引入反压机制（BackPressure），动态控制数据接收速率来适配集群数据处理能力。
    - 配置参数：
      - spark.streaming.backpressure.enabled=true : 开启反压
      - spark.streaming.kafka.maxRatePerPartition=10000 : 限定每个Kafka partition每秒最多消费条数
    
## Spark Streaming和Kafka的集成（Direct API）

  - 背景：Spark 1.3引入Kafka Direct API。通过周期性地获取Kafka每个分区的最新offset，Spark Driver只需简单计算下一个批次需要处理Kafka分区偏移量的范围，然后Spark Executor直接从相应分区消费数据。这种方法把Kafka当作成一个文件系统，然后像读文件一样来消费主题中的数据。
  - 消费数据：
    - 设置消费者参数：
      ```
      val consumerParams = Map[String, String](
        "bootstrap.servers" -> brokers,
        "group.id" -> consumerGroup,
        "key.deserializer" -> "org.apache.kafka.common.serialization.StringDeserializer",
        "value.deserializer" -> "org.apache.kafka.common.serialization.StringDeserializer",
        "security.protocol" -> "SASL_PLAINTEXT",
        "sasl.kerberos.service.name" -> "kafka",
        "kerberos.domain.name" -> "hadoop.hadoop.com"
      )
      ```
    - 配置策略：
      ```
      val locationStrategy = LocationStrategies.PreferConsistent
      val consumerStrategy = ConsumerStrategies.Subscribe[String, String](Set(topic), consumerParams)
      ```
    - 接收并处理数据：
      ```
      val stream: InputDStream[ConsumerRecord[String, String]] = KafkaUtils.createDirectStream[String, String](ssc, locationStrategy, consumerStrategy)
      val topicStream: DStream[String] = stream.transform(record => record.map(r => r.value))
      topicStream.foreachRDD(
        rdd => process(rdd)  
      )
      ```
  - 消费者API解析：
    - LocationStrategies：分区数据调度策略。Spark集成Kafka时在Executor节点缓存Consumers（而不是每次重新创建），并且将Kafka分区数据调度到有合适Consumer的机器节点。
      - PreferConsistent：平均地将分区数据分配到可用的Executors
      - PreferBrokers：调度分区数据到这个分区的Kafka leader上，executors和Kafka brokers在相同的机器上时使用
      - PreferFixed：显示地指定分区数据到主机的映射，分区负载不均衡时使用
    - ConsumerStrategies：消费策略。
      - Subscribe：订阅一个固定集合的主题
      - SubscribePattern：可以使用正则表达式指定主题
      - Assign：指定一个固定集合的分区
      - ConsumerStrategy是一个public类，可以通过继承进行定制化
    - ConsumerRecord：Direct API返回的对象，包含：主题名称、分区号、偏移量、<key, value>形式的消费数据
      - 获取数据：val data = stream.transform(record => record.map(r => r.value))
      - 获取key：val key = stream.transform(record => record.map(r => r.key))
  - 生产数据：
    - 设置生产者参数：
      ```
      val props = new Propertes()
      props.put("bootstrap.servers", brokers)
      props.put("acks", "1")
      props.put("batch.size", "16384")
      props.put("linger.ms", "10")
      props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
      props.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer")
      props.put("security.protocol", "SASL_PLAINTEXT")
      props.put("sasl.kerberos.service.name", "kafka")
      props.put("kerberos.domain.name", "hadoop.hadoop.com")
      ```
    - 创建生产者对象：
      ```
      val producer = new KafkaProducer(props)
      ```
    - 同步发送数据：
      ```
      val record = new ProducerRecord[String, Array[Byte]](topic, message.getBytes("UTF-8"))
      producer.send(record).get()
      ```
    - 异步发送数据：
      ```
      producer.send(record, new Callback{
        override def onCompletion(recordMetadata: RecordMetadata, e: Exception): Unit = {
          if (e != null) {
            process exception
          } else {
            do something
          }
        }
      })
      ```

