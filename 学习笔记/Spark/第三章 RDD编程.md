# RDD编程

## 编程模型

  - 在Spark中，RDD被表示为对象，通过对象上的方法调用来对RDD进行转换。
  - 只有遇到action，才会执行RDD的计算(即延迟计算)，这样在运行时可以通过管道的方式传输多个转换。
  - 编程模型：
  
  ![编程模型](./图片/编程模型.PNG)
  
## RDD的创建

  - 在Spark中创建RDD的创建方式可以分为三种：从集合中创建RDD；从外部存储创建RDD；从其他RDD创建。
  - 从集合中创建RDD，Spark主要提供了两种函数：parallelize和makeRDD：
    - val rdd1 = sc.makeRDD(Array(1,2,3,4,5,6,7,8))
  - 由外部存储系统的数据集创建，包括本地的文件系统，还有所有Hadoop支持的数据集，比如HDFS、Cassandra、HBase等：
    - val rdd2= sc.textFile("hdfs://hadoop102:9000/RELEASE")
  
## RDD的转换

  - RDD整体上分为Value类型和Key-Value类型。
  
### Value类型

  - map(func):
    - 作用：返回一个新的RDD，该RDD由每一个输入元素经过func函数转换后组成。 
    - 示例：
      - var source  = sc.parallelize(1 to 10)
      - val mapRDD = source.map(_ * 2)
  - mapPartitions(func):
    - 作用：类似于map，但独立地在RDD的每一个分片上运行，因此在类型为T的RDD上运行时，func的函数类型必须是Iterator[T] => Iterator[U]。
    - 假设有N个元素，有M个分区，那么map的函数的将被调用N次,而mapPartitions被调用M次,一个函数一次处理所有分区。
    - 示例：
      - val rdd = sc.makeRDD(Array(1, 2, 3, 4))
      - rdd.mapPartitions(x => x.map(_ * 2))
  - mapPartitionsWithIndex(func):
    - 作用：类似于mapPartitions，但func带有一个整数参数表示分片的索引值，因此在类型为T的RDD上运行时，func的函数类型必须是(Int, Interator[T]) => Iterator[U]。
    - 示例：
      - val rdd = sc.parallelize(Array(1,2,3,4))
      - val indexRdd = rdd.mapPartitionsWithIndex((index,items)=>(items.map((index,_))))
  - flatMap(func):
    - 作用：类似于map，但是每一个输入元素可以被映射为0或多个输出元素（所以func应该返回一个序列，而不是单一元素）。
  - glom():
    - 作用：将每一个分区形成一个数组，形成新的RDD类型时RDD[Array[T]]。
    - 示例：
      - val rdd = sc.parallelize(1 to 16,4)
      - rdd.glom()
  - groupBy(func):
    - 作用：分组，按照传入函数的返回值进行分组。将相同的key对应的值放入一个迭代器。
    - 示例：
      - val rdd = sc.parallelize(1 to 4)
      - val group = rdd.groupBy(_%2)
  - filter(func):
    - 作用：过滤。返回一个新的RDD，该RDD由经过func函数计算后返回值为true的输入元素组成。
  - sample(withReplacement, fraction, seed):
    - 作用：以指定的随机种子随机抽样出数量为fraction的数据，：withReplacement表示是抽出的数据是否放回，true为有放回的抽样，false为无放回的抽样，seed用于指定随机数生成器种子。
    - 示例：
      - val rdd = sc.parallelize(1 to 10)
      - var sample1 = rdd.sample(true,0.4,2)
      - var sample2 = rdd.sample(false,0.2,3)
  - distinct([numTasks]))：
    - 作用：对源RDD进行去重后返回一个新的RDD。默认情况下，只有8个并行任务来操作，但是可以传入一个可选的numTasks参数改变它。
    - 示例：
      - val rdd = sc.parallelize(List(1,2,1,5,2,9,6,1))
      - val distinctRDD = rdd.distinct()
  - coalesce(numPartitions):
    - 作用：缩减分区数，用于大数据集过滤后，提高小数据集的执行效率。
    - 示例：
      - val rdd = sc.parallelize(1 to 16,4)
      - val coalesceRDD = rdd.coalesce(3)
  - repartition(numPartitions)：
    - 作用：根据分区数，重新通过网络随机洗牌所有数据。
    - 示例：
      - val rdd = sc.parallelize(1 to 16,4)
      - val rerdd = rdd.repartition(2)
  - coalesce和repartition的区别：
    - coalesce重新分区，可以选择是否进行shuffle过程。由参数shuffle: Boolean = false/true决定。
    - repartition实际上是调用的coalesce，默认是进行shuffle的。
  - sortBy(func,[ascending], [numTasks])：
    - 作用；使用func先对数据进行处理，按照处理后的数据比较结果排序，默认为正序。
  - pipe(command, [envVars]):
    - 作用：管道，针对每个分区，都执行一个shell脚本，返回输出的RDD。
    - 注意：脚本需要放在Worker节点可以访问到的位置。
    
### 双Value类型交互

  - union(otherDataset)：
    - 作用：对源RDD和参数RDD求并集后返回一个新的RDD。
  - subtract (otherDataset)：
    - 作用：计算差的一种函数，去除两个RDD中相同的元素，不同的RDD将保留下来。
  - intersection(otherDataset)：
    - 作用：对源RDD和参数RDD求交集后返回一个新的RDD。
  - cartesian(otherDataset)：
    - 作用：笛卡尔积（数据量大时尽量避免使用）
    
### Key-Value类型

  - partitionBy：
    - 作用：对pairRDD进行分区操作，如果原有的partionRDD和现有的partionRDD是一致的话就不进行分区， 否则会生成ShuffleRDD，即会产生shuffle过程。
    - 示例：
      - val rdd = sc.parallelize(Array((1,"aaa"),(2,"bbb"),(3,"ccc"),(4,"ddd")),4)
      - var rdd2 = rdd.partitionBy(new org.apache.spark.HashPartitioner(2))
  - groupByKey：
    - 作用：groupByKey也是对每个key进行操作，但只生成一个sequence。
  - reduceByKey(func, [numTasks])：
    - 作用：在一个(K,V)的RDD上调用，返回一个(K,V)的RDD，使用指定的reduce函数，将相同key的值聚合到一起，reduce任务的个数可以通过第二个可选的参数来设置。
    - 示例：
      - val rdd = sc.parallelize(List(("female",1),("male",5),("female",5),("male",2)))
      - val reduce = rdd.reduceByKey((x,y) => x+y)
  - reduceByKey和groupByKey的区别：
    - reduceByKey：按照key进行聚合，在shuffle之前有combine（预聚合）操作，返回结果是RDD[k,v]。
    - groupByKey：按照key进行分组，直接进行shuffle。
  - aggregateByKey：
    - 参数：(zeroValue:U,[partitioner: Partitioner]) (seqOp: (U, V) => U,combOp: (U, U) => U)
    - 参数描述：
      - zeroValue：给每一个分区中的每一个key一个初始值。
      - seqOp：函数用于在每一个分区中用初始值逐步迭代value（分区内）。
      - combOp：函数用于合并每个分区中的结果（分区间）。
    - 作用：在kv对的RDD中，，按key将value进行分组合并，合并时，将每个value和初始值作为seq函数的参数，进行计算，返回的结果作为一个新的kv对，然后再将结果按照key进行合并，最后将每个分组的value传递给combine函数进行计算（先将前两个value进行计算，将返回结果和下一个value传给combine函数，以此类推），将key与计算结果作为一个新的kv对输出。
  - foldByKey：
    - 参数：(zeroValue: V)(func: (V, V) => V): RDD[(K, V)]
    - 作用：aggregateByKey的简化操作，seqop和combop相同。
  - combineByKey[C]：
    - 参数：(createCombiner: V => C,  mergeValue: (C, V) => C,  mergeCombiners: (C, C) => C) 
    - 参数描述：
      - createCombiner: combineByKey() 会遍历分区中的所有元素，因此每个元素的键要么还没有遇到过，要么就和之前的某个元素的键相同。如果这是一个新的元素,combineByKey()会使用一个叫作createCombiner()的函数来创建那个键对应的累加器的初始值。
      - mergeValue: 如果这是一个在处理当前分区之前已经遇到的键，它会使用mergeValue()方法将该键的累加器对应的当前值与这个新的值进行合并。
      - mergeCombiners: 由于每个分区都是独立处理的， 因此对于同一个键可以有多个累加器。如果有两个或者更多的分区都有对应同一个键的累加器， 就需要使用用户提供的 mergeCombiners() 方法将各个分区的结果进行合并。
    - 作用：对相同K，把V合并成一个集合。
  - sortByKey([ascending], [numTasks])：
    - 作用：在一个(K,V)的RDD上调用，K必须实现Ordered接口，返回一个按照key进行排序的(K,V)的RDD。
  - mapValues：
    - 针对于(K,V)形式的类型只对V进行操作。
  - join(otherDataset, [numTasks])：
    - 作用：在类型为(K,V)和(K,W)的RDD上调用，返回一个相同key对应的所有元素对在一起的(K,(V,W))的RDD。
  - cogroup(otherDataset, [numTasks])：
    - 作用：在类型为(K,V)和(K,W)的RDD上调用，返回一个(K,(Iterable<V>,Iterable<W>))类型的RDD。
  
  
    
      
  
      
      
      
      
      
      
