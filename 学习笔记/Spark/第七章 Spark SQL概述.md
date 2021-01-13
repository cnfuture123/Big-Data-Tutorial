# Spark SQL概述

  - Spark SQL是Spark用来处理结构化数据的一个模块，它提供了2个编程抽象：DataFrame和DataSet，并且作为分布式SQL查询引擎的作用。
  - 它是将Spark SQL转换成RDD，然后提交到集群执行，执行效率非常快。
  - Spark SQL的特点：
    - 易集成
    - 统一的数据访问方式
    - 兼容Hive
    - 标准的数据连接
    
## DataFrame

  - 与RDD相似， DataFrame也是数据的一个不可变分布式集合。但与RDD不同的是，数据都被组织到有名字的列中，就像关系型数据库中的表一样。
  - 没有明确的类型，Dataframe是Dataset的特列，DataFrame=Dataset[Row]。
  - DataFrames可以从多个数据源构造，包括：结构化的数据文件、Hive表、外部数据库、存在的RDD。
  - 性能上比RDD要高，主要原因：
    - 优化的执行计划：查询计划通过Spark catalyst optimiser进行优化。
  
## DataSet

  - 是Dataframe API的一个扩展，是Spark 1.6引入的数据抽象。
  - 有明确类型，比如可以有Dataset[Car]，Dataset[Person]；支持lambda函数，并且兼具Spark SQL的优化执行引擎。
  - 样例类被用来在Dataset中定义数据的结构信息，样例类中每个属性的名称直接映射到DataSet中的字段名称。
  - DataFrame有字段名称，但是没有明确的字段类型；而DataSet有字段名称，而且明确字段类型，所以有更严格的错误检查。
  
