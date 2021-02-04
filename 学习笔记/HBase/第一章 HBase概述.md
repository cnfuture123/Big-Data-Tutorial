## HBase概述

  - HBase定义: HBase是一种分布式、可扩展、支持海量数据存储的NoSQL数据库，面向列族的数据库。
  - HBase使用场景：
    - 有足够的数据
    - 关系型数据库的特性不是限制
    - 保证足够的服务器，分布式集群要求足够多的DataNode
  - HBase数据模型: 
    - 逻辑上，HBase的数据模型同关系型数据库很类似，数据存储在一张表中，有行有列。
    - 但从HBase的底层物理存储结构（K-V）来看，HBase更像是一个multi-dimensional map。
  - HBase逻辑结构: 
  
  ![HBase逻辑结构](./图片/HBase逻辑结构.PNG)
  
  - HBase物理存储结构: 
  
  ![HBase物理存储结构](./图片/HBase物理存储结构.PNG) 
  
  - 数据模型:
    - Name Space: 
      - 命名空间，类似于关系型数据库的DatabBase, 每个命名空间下有多个表。
      - HBase有两个自带的命名空间，分别是hbase和default。
      - hbase中存放的是HBase内置的表，default表是用户默认使用的命名空间。
    - Table:
      - HBase定义表时只需要声明列族即可，不需要声明具体的列。
      - 往HBase写入数据时，字段可以动态、按需指定。
      - 因此，和关系型数据库相比，HBase能够轻松应对字段变更的场景。
    - Row:
      - 数据以行的形式存储在表中。
      - 每行数据都由一个唯一标识RowKey和多个Column（列）组成。RowKey没有数据类型，在HBase中RowKey被看作一个字节数组。
      - 数据是按照RowKey的字典顺序存储的，并且查询数据时只能根据RowKey进行检索。
    - Column Family:
      - 行中的数据根据列族分组，列族会影响HBase中数据的物理分布，将具有相同列族的列及其值存储在一起。
    - Column:
      - HBase中的每个列都由Column Family(列族)和Column Qualifier（列限定符）进行限定。
      - 例如 info:name，info:age。
    - Cell:
      - 由{rowkey, column family:column qualifier, value, timeStamp} 唯一确定的单元。
      - cell中的数据是没有类型的，全部是字节码形式存贮。
    - Timestamp:
      - 用于标识数据的不同版本（Version），每条数据写入时，如果不指定时间戳，系统会自动为其加上该字段，其值为写入HBase的时间。
      
## HBase基本架构

  - HBase基本架构:
  
  ![HBase基本架构](./图片/HBase基本架构.PNG)
      
  - 架构角色：
    - Region Server：
      - 概述：
        - 一个Region Server就是一个机器节点，包含多个Region，为Region的管理者。HRegionServer是其实现类
        - 负责切分正在运行过程中变的过大的Region
      - 接口：
        - 对于数据的操作：get, put, delete。
        - 对于Region的操作：splitRegion、compactRegion。
      - 进程：RegionServer后台运行一些线程
        - CompactSplitThread: 检查何时做splits，并处理minor compactions
        - MajorCompactionChecker: 检查何时做major compactions
        - MemStoreFlusher: 周期性将MemStore内存中的数据刷入StoreFiles
        - LogRoller: 周期性检查RegionServer的WAL
    - Master:
      - 概述：
        - Master是所有RegionServer的管理者，负责监控RegionServer，并且是元数据修改的接口。
        - 在分布式集群中，Master运行在NameNode上，其实现类为HMaster。
        - 对于RegionServer的操作：分配regions到每个RegionServer，监控每个RegionServer的状态，负载均衡和故障转移。
      - 接口：
        - 对于表的操作：createTable, modifyTable, removeTable, enable, disable
        - 对于列族的操作：addColumn, modifyColumn, removeColumn
        - 对于Region的操作：move, assign, unassign
      - 进程：Master后台运行一些线程
        - LoadBalancer: 周期性地移动，重新分配regions，实现集群的负载均衡
        - CatalogJanitor: 周期性检查和清理hbase:meta表
    - HRegion:
      - table在行的方向上分隔为多个Region。Region是HBase中分布式存储和负载均衡的最小单元，即不同的region可以分别在不同的Region Server上，但同一个Region是不会拆分到多个server上。
      - Region按大小分隔，每个表一般是只有一个region。随着数据不断插入表，region不断增大，当region的某个列族达到一个阈值（默认256M）时就会分成两个新的region。
    - Store:
      - 每一个region由一个或多个store组成，HBase会把一起访问的数据放在一个store里面，即为每个ColumnFamily建一个store。
      - 一个Store由一个MemStore和0或者多个StoreFile组成。 HBase以store的大小来判断是否需要切分region。
    - MemStore:
      - MemStore是内存存储系统，一旦数据大小达到阈值，会溢写到磁盘作为额外的StoreFile存储，这个操作由MemStoreFlusher线程完成
    - StoreFile:
      - MemStore的数据写到磁盘的文件就是StoreFile，StoreFile底层是以HFile的格式保存
    - HFile:
      - HBase中KeyValue数据的存储格式，是hadoop的二进制格式文件
    - HLog:
      - HLog文件就是一个普通的Hadoop Sequence File，key是HLogKey对象，其中记录了写入数据的归属信息，除了table和region名字外，还同时包括sequence number和timestamp。value是HBase的KeyValue对象，即对应HFile中的KeyValue。
    - ZooKeeper:
      - HBase通过Zookeeper来做Master的高可用、RegionServer的监控、元数据的入口以及集群配置的维护等工作。
    - HDFS:
      - HDFS为HBase提供最终的底层数据存储服务，同时为HBase提供高可用的支持。
        
