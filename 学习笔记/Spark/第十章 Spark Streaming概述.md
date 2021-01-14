# Spark Streaming概述

  - Spark Streaming用于流式数据的处理。
  - Spark Streaming支持的数据输入源很多，例如：Kafka、Flume、Twitter、ZeroMQ和简单的TCP套接字等。结果也能保存在很多地方，如HDFS，数据库等。
  - 数据输入后可以用Spark的高度抽象原语如：map、reduce、join、window等进行运算。
  - Spark Streaming使用离散化流(Discretized Stream)作为抽象表示，叫作DStream。DStream是连续的流数据，由连续的RDDs表示。其中每个RDD包含一个特定时间间隔的数据。对DStream进行的操作实际转换为对底层一系列RDD的操作。
  
## Spark Streaming特点
  
  - Ease of Use
  - Fault Tolerance
  - Spark Integration
  
## SparkStreaming架构

  - SparkStreaming架构:
  
  ![SparkStreaming架构](./图片/SparkStreaming架构.PNG)
  
  
