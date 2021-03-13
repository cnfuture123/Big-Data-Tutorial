# MapReduce框架原理

## InputFormat数据输入

  - MapTask并行度决定机制：
    - 数据切片：数据切片只是在逻辑上对输入进行分片，它只是引用到输入数据的位置。
    - InputFormat负责创建输入分段，并划分为一系列的记录。
    - 客户端通过getSplits()计算分段，然后发送到Application Master。AM将map任务调度到距离分段数据近的节点。Map任务调用createRecordReader()获取分段的RecordReader，它用来迭代记录，并生成键值对传给map函数。
    - 图示：
    
      ![MapTask并行度决定机制](./图片/MapTask并行度决定机制.PNG)
    
    - InputFormat类分级：
      
     
    
### FileInputFormat

  - 使用文件作为数据源的基类，做两件事情：定义输入数据包含哪些文件，生成输入文件的分段。
  - FileInputFormat切片机制：
  
    ![FileInputFormat切片机制](./图片/FileInputFormat切片机制.PNG)
  
  - 切片大小的计算：
  
    ![切片大小的计算](./图片/切片大小的计算.PNG)
  
### CombineTextInputFormat

  - CombineTextInputFormat用于小文件过多的场景，它可以将多个小文件从逻辑上规划到一个切片中，这样，多个小文件就可以交给一个MapTask处理。
  - 处理小文件缺点：
    - 大量的小文件需要对应数量的map任务，每个map任务会引入额外的开销
    - 大量的小文件增加查找文件的开销。
    - 存储大量的小文件浪费NameNode内存
  - CombineTextInputFormat切片机制：
  
    ![CombineTextInputFormat切片机制](./图片/CombineTextInputFormat切片机制.PNG)
  
### TextInputFormat

  - TextInputFormat是默认的InputFormat，每条记录是输入的一行。键是每行起点的字节偏移量，值是每行内容。
  
    ![TextInputFormat](./图片/TextInputFormat.PNG)
  
### KeyValueTextInputFormat

  - KeyValueTextInputFormat：
  
    ![KeyValueTextInputFormat](./图片/KeyValueTextInputFormat.PNG)
  
### NLineInputFormat

  - NLineInputFormat：N指的是每个mapper接收N行数据。
  
    ![NLineInputFormat](./图片/NLineInputFormat.PNG)
    
### SequenceFileInputFormat

  - SequenceFileInputFormat: 存储二进制键值对

### DBInputFormat

  - DBInputFormat支持用JDBC从关系型数据库读取数据
  - 另一种在HDFS和关系型数据库移动数据的方式是Sqoop
  
## MapReduce工作流程

  - MapReduce工作流程：
  
    ![MapReduce工作流程1](./图片/MapReduce工作流程1.PNG)
  
    ![MapReduce工作流程2](./图片/MapReduce工作流程2.PNG)
  
  - 注意细节：
    - Shuffle过程详解：
      - MR保证reduce的输入都是根据key排序。这个排序过程以及，将map输出传输到reduce作为输入的过程被称为shuffle
      - MapTask收集我们的map()方法输出的kv对，放到内存缓冲区中。
      - 从内存缓冲区不断溢出本地磁盘文件，可能会溢出多个文件。
      - 多个溢出文件会被合并成大的溢出文件。
      - 在溢出过程及合并的过程中，都要调用Partitioner进行分区和针对key进行排序。
      - ReduceTask根据自己的分区号，去各个MapTask机器上取相应的结果分区数据。
      - ReduceTask会取到同一个分区的来自不同MapTask的结果文件，ReduceTask会将这些文件再进行合并（归并排序）。
      - 合并成大文件后，Shuffle的过程也就结束了，后面进入ReduceTask的逻辑运算过程（从文件中取出一个一个的键值对Group，调用用户自定义的reduce()方法）。
    - Shuffle中的缓冲区大小会影响到MapReduce程序的执行效率，原则上说，缓冲区越大，磁盘io的次数越少，执行速度就越快。缓冲区的大小可以通过参数调整，参数：io.sort.mb默认100M。
    
### Partition分区

  - 默认分区是根据key的hashcode对ReduceTasks个数取模得到的。用户没法控制哪个key存储到哪个分区。
  - Partition注意细节：
  
    ![Partition注意细节](./图片/Partition注意细节.PNG)
  
### WritableComparable排序

  - Maptask和ReduceTask都会按照key进行排序，该操作是Hadoop默认行为。默认排序是按照字典顺序排序，实现的方法是快速排序。
  - 排序概述：
  
  ![排序概述](./图片/排序概述.PNG)
  
  - 排序分类：
  
  ![排序分类](./图片/排序分类.PNG)
  
### Combiner合并

  - Combiner概述：
  
    ![Combiner概述](./图片/Combiner概述.PNG)
  
### GroupingComparator分组（辅助排序）

  - 对Reduce阶段的数据根据某一个或几个字段进行分组。
  - 分组排序步骤：
    - 自定义类继承WritableComparator。
    - 重写compare()方法。
    - 创建一个构造将比较对象的类传给父类。
    
## MapTask工作机制

  - MapTask工作机制：
  
    ![MapTask工作机制](./图片/MapTask工作机制.PNG)
  
## ReduceTask工作机制

  - ReduceTask工作机制：
  
    ![ReduceTask工作机制](./图片/ReduceTask工作机制.PNG)
  
  - 设置ReduceTask并行度（个数）：ReduceTask的并行度同样影响整个Job的执行并发度和执行效率，但与MapTask的并发数由切片数决定不同，ReduceTask数量的决定是可以直接手动设置：
    - example : job.setNumReduceTasks(4);
  - ReduceTask注意细节：
  
   ![ReduceTask注意细节](./图片/ReduceTask注意细节.PNG)
  
## OutputFormat数据输出

  - OutputFormat概述：
  
    ![OutputFormat概述](./图片/OutputFormat概述.PNG)
    
  - MultipleOutputs：可以写数据到文件，并且文件名字可以从键和值中构造。Map输出文件名格式为name-m-nnnnn，reduce输出文件名格式为name-r-nnnnn。nnnnn是对应分区数。
  - LazyOutputFormat：输出文件只有包含记录时才会被创建
    
  
## MapReduce开发总结

  - MapReduce开发总结：
  
  ![MapReduce开发总结1](./图片/MapReduce开发总结1.PNG)
  
  ![MapReduce开发总结2](./图片/MapReduce开发总结2.PNG)
  
  ![MapReduce开发总结3](./图片/MapReduce开发总结3.PNG)
  
  ![MapReduce开发总结4](./图片/MapReduce开发总结4.PNG)
  
  ![MapReduce开发总结5](./图片/MapReduce开发总结5.PNG)
  
  
