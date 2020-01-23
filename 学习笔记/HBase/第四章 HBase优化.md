# HBase优化

  - 高可用:
    - 在HBase中HMaster负责监控HRegionServer的生命周期，均衡RegionServer的负载。
    - 如果HMaster挂掉了，那么整个HBase集群将陷入不健康的状态，并且此时的工作状态并不会维持太久。
    - 配置高可用步骤：
      - 在conf目录下创建backup-masters文件。
      - 在backup-masters文件中配置高可用HMaster节点。
  - 预分区：
    - 每一个region维护着StartRow与EndRow，如果加入的数据符合某个Region维护的RowKey范围，则该数据交给这个Region维护。
    - 依照这个原则，我们可以将数据所要投放的分区提前大致的规划好，以提高HBase性能。
    - 预分区的常用方法：
      - 手动设定预分区：create 'staff1','info','partition1',SPLITS => ['1000','2000','3000','4000']
      - 生成16进制序列预分区：create 'staff2','info','partition2',{NUMREGIONS => 15, SPLITALGO => 'HexStringSplit'}
      
  - RowKey设计：
    - 一条数据的唯一标识就是RowKey，那么这条数据存储于哪个分区，取决于RowKey处于哪个一个预分区的区间内。
    - 设计RowKey的主要目的 ，就是让数据均匀的分布于所有的region中，在一定程度上防止数据倾斜。
    - RowKey常用的设计方案：
      - 生成随机数、hash、散列值。在做此操作之前，一般我们会选择从数据集中抽取样本，来决定什么样的RowKey Hash后作为每个分区的临界值。
      - 字符串反转:
        - 例子：20170524000001 转成 10000042507102
      - 字符串拼接：20170524000001_a12e
  - 内存优化：
    - HBase操作过程中需要大量的内存开销，毕竟Table是可以缓存在内存中的，一般会分配整个可用内存的70%给HBase的Java堆。
    - 不建议分配非常大的堆内存，因为GC过程持续太久会导致RegionServer处于长期不可用状态，一般16~48G内存就可以了。
    
  
