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
    
     
  
  
