# Spark SQL概述

  - Spark SQL是Spark用来处理结构化数据的一个模块，它提供了2个编程抽象：DataFrame和DataSet，并且作为分布式SQL查询引擎的作用。
  - 它是将Spark SQL转换成RDD，然后提交到集群执行，执行效率非常快。
  - Spark SQL的特点：
    - 易集成
    - 统一的数据访问方式
    - 兼容Hive
    - 标准的数据连接
    
## DataFrame

  - 与RDD类似，DataFrame也是一个分布式数据容器。
  - DataFrame更像传统数据库的二维表格，除了数据以外，还记录数据的结构信息，即schema。
  - 与Hive类似，DataFrame也支持嵌套数据类型（struct、array和map）。
  - 从API易用性的角度上看，DataFrame API提供的是一套高层的关系操作，比函数式的RDD API要更加友好，门槛更低。
  - DataFrame是为数据提供了Schema的视图。可以把它当做数据库中的一张表来对待，DataFrame也是懒执行的。
  - 性能上比RDD要高，主要原因：
    - 优化的执行计划：查询计划通过Spark catalyst optimiser进行优化。
  
## DataSet

  - 是Dataframe API的一个扩展，是Spark最新的数据抽象。
  - 用户友好的API风格，既具有类型安全检查也具有Dataframe的查询优化特性。
  - Dataset支持编解码器，当需要访问非堆上的数据时可以避免反序列化整个对象，提高了效率。
  - 样例类被用来在Dataset中定义数据的结构信息，样例类中每个属性的名称直接映射到DataSet中的字段名称。
  - Dataframe是Dataset的特列，DataFrame=Dataset[Row] ，所以可以通过as方法将Dataframe转换为Dataset。Row是一个类型，跟Car、Person这些的类型一样，所有的表结构信息我都用Row来表示。
  - DataSet是强类型的。比如可以有Dataset[Car]，Dataset[Person]。
  - DataFrame只是知道字段，但是不知道字段的类型；而DataSet不仅仅知道字段，而且知道字段类型，所以有更严格的错误检查。
  
  
  
  
  
  
  
