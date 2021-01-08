# Flume概述

## Flume定义

  - Flume是Cloudera提供的一个高可用的，高可靠的，分布式的海量日志采集、聚合和传输的系统。
  - Flume基于流式架构，灵活简单。
  - Flume最主要的作用就是，实时读取服务器本地磁盘的数据，将数据写入到HDFS。
  - Flume图示：
  
  ![Flume图示](./图片/Flume图示.PNG)
  
## Flume基础架构

### Flume基础架构：
  
  ![Flume基础架构](./图片/Flume基础架构.PNG)
  
### 组件介绍：

  - Agent:
    - Agent是一个JVM进程，它以事件的形式将数据从源头送至目的地。
    - Agent主要由3个部分组成，Source、Channel、Sink。
  - Event:
    - Flume数据传输的基本单元，以Event形式将数据从源头送至目的地。
    - Event由Header和Body两部分组成，Header用来存放该event的一些属性，为K-V结构。Body用来存放该条数据，形式为字节数组。
  - Source:
    - Source是负责接收数据到Flume Agent的组件，可以处理各种类型、各种格式的日志数据。
    - Source类型：
      - Spooling Directory Source：监控特定目录的新文件，并从新文件中获取Events数据。
      - Exec Source：启动时运行一个Unix命令，并预期这个进程不断生产数据到标准输出（stderr可以通过将logStdErr设置为true生效）。如果进程退出，则Exec Source也会退出不再生产数据。
      - Taildir Source：监控某些特定的文件，对这些文件执行tail命令，获取文件中新增的数据。
      - Avro Source：监听Avro端口，接收外部Avro客户端流数据。
      - JMS Source：从JMS端，例如：queue或者topic读取信息。
      - Thrift Source：监听Thrift端口，接收外部Thrift客户端流数据。
      - Kafka Source：作为一个Kafka消费者从Kafka主题获取数据。
      - NetCat TCP Source：监听指定端口，将输入的每一行转换为一个Event。
      - HTTP Source：通过HTTP Get或Post接收到的Event。
  - Channel:
    - Channel是位于Source和Sink之间的缓冲区。
    - Channel允许Source和Sink运作在不同的速率上。
    - Channel是线程安全的，可以同时处理几个Source的写入操作和几个Sink的读取操作。
    - Flume支持4种Channel：Memory Channel，File Channel，JDBC Channel，Kafka Channel。
      - Memory Channel：Events存储在内存中的队列，最大容量可配置。它在不需要关心数据丢失的情景下适用。因为程序死亡、机器宕机或者重启都会导致数据丢失。
      - File Channel：所有事件写到磁盘。因此在程序关闭或机器宕机的情况下不会丢失数据。
      - JDBC Channel：Events存储在数据库，支持嵌入的Derby。
      - Kafka Channel：Events存储在Kafka集群。
  - Sink:
    - Sink不断地轮询Channel中的事件且批量地移除它们，并将这些事件批量写入到存储或索引系统、或者被发送到另一个Flume Agent。
    - Sink类型：
      - HDFS Sink：将Events写入HDFS。支持Text和Sequence文件格式，两种格式都支持压缩，并且可以周期性地滚动产生新的文件。
      - Hive Sink：将包含文本或Json数据的Event直接通过Hive事务写入Hive表或分区。
      - HBase Sink：包含三种类型：HBaseSink，HBase2Sink和AsyncHBaseSink。HBase2Sink和HBaseSink基本相同，用于HBase 2.x版本；AsyncHBaseSink通过异步的方式写数据到HBase。
      - ElasticSearch Sink：数据写入ElasticSearch集群。
      - Kafka Sink：发布数据到Kafka主题。
      - Logger Sink：输出INFO级别的日志，通常用于测试和调试的目的。
      - Avro Sink：数据转换为Avro Event，并发送到指定的主机和端口。
      - Thrift Sink：数据转换为Thrift Event，并发送到指定的主机和端口。
      - Null Sink：丢弃所有接收到的Events。
      - HTTP Sink：使用HTTP Post将Events发送到远程服务。
    
