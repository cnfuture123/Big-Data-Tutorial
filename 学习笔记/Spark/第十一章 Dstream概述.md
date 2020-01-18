# DStream概述

  - Discretized Stream是Spark Streaming的基础抽象，代表持续性的数据流和经过各种Spark原语操作后的结果数据流。在内部实现上，DStream是一系列连续的RDD来表示。每个RDD含有一段时间间隔内的数据。
  
## DStream创建

### 文件数据源

  - 文件数据流：能够读取所有HDFS API兼容的文件系统文件，通过fileStream方法进行读取，Spark Streaming将会监控dataDirectory目录并不断处理移动进来的文件，目前不支持嵌套目录。
  - 用法：streamingContext.textFileStream(dataDirectory)
  - 注意细节：
    - 文件需要有相同的数据格式。
    - 文件进入 dataDirectory的方式需要通过移动或者重命名来实现。
    - 一旦文件移动进目录，则不能再修改，即便修改了也不会读取新数据。
    
## Kafka数据源

  - KafkaUtils对象可以在StreamingContext和JavaStreamingContext中以你的Kafka消息创建出DStream。
  - 由于KafkaUtils可以订阅多个主题，因此它创建出的DStream由成对的主题和消息组成。
  
## DStream转换

  - DStream上的原语与RDD的类似，分为Transformations（转换）和Output Operations（输出）两种。
  - 转换操作中还有一些比较特殊的原语，如：updateStateByKey()、transform()以及各种Window相关的原语。
  
### 无状态转化操作

  - 无状态转化操作就是把简单的RDD转化操作应用到每个批次上，也就是转化DStream中的每一个RDD。
  - 相关操作: map(), flatMap(), filter(), repartition(), reduceByKey(), groupByKey()。
  
### 有状态转化操作

  - UpdateStateByKey：
    - UpdateStateByKey原语用于记录历史记录。
    - 它为我们提供了对一个状态变量的访问，用于键值对形式的DStream。
    - updateStateByKey()的结果会是一个新的DStream，其内部的RDD序列是由每个时间区间对应的(键，状态)对组成的。
    - updateStateByKey操作使得我们可以在用新信息进行更新时保持任意的状态。
    - 用法：
      - 定义状态，状态可以是一个任意的数据类型。
      - 定义状态更新函数，用此函数阐明如何使用之前的状态和来自输入流的新值对状态进行更新。
    - 使用updateStateByKey需要对检查点目录进行配置，会使用检查点来保存状态。
  - Window Operations：
    - Window Operations可以设置窗口的大小和滑动窗口的间隔来动态的获取当前Steaming的允许状态。
    - 基于窗口的操作会在一个比StreamingContext的批次间隔更长的时间范围内，通过整合多个批次的结果，计算出整个窗口的结果。
    - 所有基于窗口的操作都需要两个参数，分别为窗口时长以及滑动步长，两者都必须是StreamingContext的批次间隔的整数倍。
      - 窗口时长控制每次计算最近的多少个批次的数据，其实就是最近的 windowDuration/batchInterval 个批次。
      - 而滑动步长的默认值与批次间隔相等，用来控制对新的 DStream 进行计算的间隔。
    - Window的操作有如下原语：
      - window(windowLength, slideInterval): 基于对源DStream窗化的批次进行计算返回一个新的Dstream。
      - countByWindow(windowLength, slideInterval)：返回一个滑动窗口计数流中的元素。
      - reduceByWindow(func, windowLength, slideInterval)：通过使用自定义函数整合滑动区间流元素来创建一个新的单元素流。
      - reduceByKeyAndWindow(func, windowLength, slideInterval, [numTasks])：当在一个(K,V)对的DStream上调用此函数，会返回一个新(K,V)对的DStream，此处通过对滑动窗口中批次数据使用reduce函数来整合每个key的value值。
      - reduceByKeyAndWindow(func, invFunc, windowLength, slideInterval, [numTasks])：这个函数是上述函数的更高效版本，每个窗口的reduce值都是通过用前一个窗的reduce值来递增计算。通过reduce进入到滑动窗口数据并”反向reduce”离开窗口的旧数据来实现这个操作。
      - countByValueAndWindow(windowLength,slideInterval, [numTasks])：对(K,V)对的DStream调用，返回(K,Long)对的新DStream，其中每个key的值是其在滑动窗口中频率。
      
### 其他重要操作

  - Transform：
    - Transform原语允许DStream上执行任意的RDD-to-RDD函数。
    - 即使这些函数并没有在DStream的API中暴露出来，通过该函数可以方便的扩展Spark API。
    - 该函数每一批次调度一次。其实也就是对DStream中的RDD应用转换。
  - Join：
    - 连接操作（leftOuterJoin, rightOuterJoin, fullOuterJoin也可以），可以连接Stream-Stream，windows-stream to windows-stream、stream-dataset。

      
