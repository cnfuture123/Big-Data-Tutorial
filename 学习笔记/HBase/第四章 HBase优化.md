# HBase优化

  - 高可用:
    - 在HBase中HMaster负责监控HRegionServer的生命周期，RegionServer的负载均衡。
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
      - Salting：给RowKey加一个随机分配的前缀，使数据均衡分布地排序。前缀地数量可以等于Region的数量
      - Hashing：使用单向散列，将给定行始终以相同的前缀"盐化"，以实现负载均衡
      - 反转固定宽度或数字行键，使最常更改的部分（最末位）位于首位。这样可以增加RowKey的随机性，但是牺牲了按照RowKey排序的性质
  - 内存优化：
    - HBase操作过程中需要大量的内存开销，毕竟Table是可以缓存在内存中的，一般会分配整个可用内存的70%给HBase的Java堆。
    - 不建议分配非常大的堆内存，因为GC过程持续太久会导致RegionServer处于长期不可用状态，一般16~48G内存就可以了。
    
## HBase写优化

### 客户端优化

  - 批量写：
    - 采用批量写，可以减少客户端到RegionServer之间的RPC的次数，提高写入性能。批量写请求要么全部成功返回，要么抛出异常。
    - HTable.put(List<Put>);
  - 多线程并发写：
    - 客户端开启多个HTable写线程，每个写线程负责一个HTable对象的flush操作，这样结合定时flush和写buffer，可以保证在数据量小的时候，数据可以在较短时间内被flush，同时在数据量大的时候，写buffer一满就进行flush。
  - 使用BulkLoad写入：
    - 在HBase中数据都是以HFile形式保存在HDFS中的，当有大量数据需要写入到HBase的时候，可以使用MapReduce或者Spark直接生成HFile格式的数据文件，然后再通过RegionServer将HFile数据文件移动到相应的Region上去。
  
### 写数据表设计调优
  
  - 压缩：
    - 配置数据的压缩算法，这里的压缩是HFile中block级别的压缩。对于可以压缩的数据，配置压缩算法可以有效减少磁盘的IO，从而达到提高性能的目的。
  - IN_MEMORY：
    - 配置表的数据优先缓存在内存中，这样可以有效提升读取的性能。适合小表，而且需要频繁进行读取操作的。
  - 合理设置WAL存储级别：
    - 数据在写入HBase的时候，先写WAL，再写入缓存。默认WAL机制是开启的，并且使用的是同步机制写WAL。
    - 如果业务不特别关心异常情况下部分数据的丢失，而更关心数据写入吞吐量，可考虑关闭WAL写，这样可以提升2~3倍数据写入的吞吐量。如果业务不能接受不写WAL，但是可以接受WAL异步写入，这样可以带了1~2倍性能提升。
    - WAL的持久化等级分为如下四个等级：
      - SKIP_WAL：只写缓存，不写HLog日志。这种方式因为只写内存，因此可以极大的提升写入性能，但是数据有丢失的风险。
      - ASYNC_WAL：异步将数据写入HLog日志中。
      - SYNC_WAL：同步将数据写入HLog日志中，需要注意的是数据只是被写入文件系统中，并没有真正落盘，默认。
      - FSYNC_WAL：同步将数据写入HLog日志并强制落盘。最严格的日志写入等级，可以保证数据不会丢失，但是性能相对比较差。
      
## HBase读优化

### 客户端优化

  - 批量get请求：
    - 使用批量请求，可以减少RPC的次数，显著提高吞吐量。需要注意的是，批量get请求要么成功返回所有请求数据，要么抛出异常。
    - table.get(List<Get> gets)
  - 合理设置scan缓存大小：
    - 实际客户端发起一次scan请求，并不会将所有数据一次性加载到本地，而是分成多次RPC请求进行加载，这样设计一方面是因为大量数据请求可能会导致网络带宽严重消耗进而影响其他业务，另一方面是有可能因为数据量太大导致客户端发生OOM。
    - 数据加载到本地就存放在scan缓存中，增大scan的缓存，可以让客户端减少一次scan的RPC次数，从而从整体上提升数据读取的效率。
    - scan.setCaching(int cache) 
  - 指定请求列族或者列名:
    - HBase是列族数据库，同一列族的数据存储在一块，不同列族是分开存储的。只是根据RowKey而不指定列族进行检索的话，性能必然会比指定列族的查询差的多。
    - 此外指定请求的列的话，不需要将整个列族的所有列的数据返回，这样就减少了网路IO。
    - scan.addColumn();
  - 关闭ResultScanner:
    - 在使用table.getScanner之后，记得关闭，否则它会和服务器端一直保持连接，资源无法释放，从而导致服务端的某些资源不可用。
    - scanner.close();
  - 离线计算访问HBase建议禁用缓存：
    - 当离线访问HBase时，往往会对HBase表进行扫描，此时读取的数据没有必要存放在BlockCache中，否则会降低扫描的效率。
    - 对于频繁查询HBase的应用场景不需要禁用缓存，并且可以考虑在应用程序和HBase之间加一层缓存系统（如Redis），先查询缓存，缓存没有命中再去查询HBase。
  
### 读数据表设计调优

  - BloomFilter：
    - BloomFilter主要用来过滤不存在待检索RowKey或者Row-Col的HFile文件，避免无用的IO操作。通过设置BloomFilter可以提升读写的性能。
    - BloomFilter取值有两个，ROW和ROWCOL，需要根据业务来确定具体使用哪种：
      - 如果业务大多数随机查询仅仅使用row作为查询条件，BloomFilter一定要设置为ROW
      - 如果大多数随机查询使用row+col作为查询条件，BloomFilter需要设置为ROWCOL
      
## HBase服务端调优

  - GC_OPTS：
    - HBase是利用内存完成读写操作。提高HBase内存可以有效提高HBase性能。
    - GC_OPTS主要需要调整HeapSize和NewSize的大小。
    - 当HBase集群规模越大，Region数量越多时，可以适当调大HMaster的GC_OPTS参数。RegionServer需要比HMaster更大的内存，在内存充足的情况下，HeapSize可以相对设置大一些。
  - RegionServer并发请求处理数量：
    - hbase.regionserver.handler.count表示RegionServer在同一时刻能够并发处理多少请求。
    - 设置过高会导致激烈的线程竞争，如果设置过小，请求将会在RegionServer长时间等待，降低处理能力。应该根据资源情况，适当增加处理线程数。
    - 建议根据CPU的使用情况，可以设置为100至300之间的值。
  - 控制MemStore的大小:
    - hbase.hregion.memstore.flush.size默认值128M，单位字节，一旦有MemStore超过该值将被flush，如果regionserver的jvm内存比较充足(16G以上)，可以调整为256M。
  - BlockCache优化:
    - BlockCache作为读缓存，合理设置对于提高读性能非常重要。
    - 默认情况下，BlockCache和MemStore的配置各占40%，可以根据集群业务进行修正，比如读多写少业务可以将BlockCache占比调大。
  - Split优化:
    - hbase.hregion.max.filesize表示HBase中Region的文件总大小的最大值。当Region中的文件大小大于该参数时，将会导致Region分裂。
    - 如果该参数设置过小时，可能会导致Split操作频繁；如果该参数设置过大时，会导致Compaction操作需要处理的文件个数增多，影响Compaction执行效率
  - Compaction优化：
    - hbase.hstore.compaction.min当一个Store中文件超过该值时，会进行Compaction；hbase.hstore.compaction.max控制一次Compaction操作时的文件数据量的最大值
    - hbase.hstore.compaction.max.size如果一个HFile文件的大小大于该值，那么在Minor Compaction操作中不会选择这个文件进行Compaction操作，除非进行Major Compaction操作。这个值可以防止较大的HFile参与Compaction操作。
    - 原则是：尽量要减小Compaction的次数和Compaction的执行时间


  
  
    
  
