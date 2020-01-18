# 键值对RDD数据分区器

  - Spark目前支持Hash分区和Range分区，用户也可以自定义分区，Hash分区为当前的默认分区。
  - Spark中分区器直接决定了RDD中分区的个数、RDD中每条数据经过Shuffle过程属于哪个分区和Reduce的个数。
  - 注意细节：
    - 只有Key-Value类型的RDD才有分区器的，非Key-Value类型的RDD分区器的值是None。
    - 每个RDD的分区ID范围：0~numPartitions-1，决定这个值是属于那个分区的。
  - Hash分区：
    - HashPartitioner分区的原理：对于给定的key，计算其hashCode，并除以分区的个数取余，如果余数小于0，则用余数+分区的个数（否则加0），最后返回的值就是这个key所属的分区ID。
  - Ranger分区：
    - HashPartitioner分区弊端：可能导致每个分区中数据量的不均匀，极端情况下会导致某些分区拥有RDD的全部数据。
    - RangePartitioner作用：
      - 将一定范围内的数映射到某一个分区内，尽量保证每个分区中数据量的均匀。
      - 分区与分区之间是有序的，一个分区中的元素肯定都是比另一个分区内的元素小或者大，但是分区内的元素是不能保证顺序的。
  - 自定义分区：
    - 要实现自定义的分区器，你需要继承 org.apache.spark.Partitioner 类并实现下面三个方法：
      - numPartitions: Int:返回创建出来的分区数。
      - getPartition(key: Any): Int:返回给定键的分区编号(0到numPartitions-1)。
      - equals():Java 判断相等性的标准方法。Spark需要用这个方法来检查你的分区器对象是否和其他分区器实例相同，这样Spark才可以判断两个RDD的分区方式是否相同。
      
      
  
