# SparkSQL编程

  - SparkSession：
    - SparkSession是Spark最新的SQL查询起始点，实质上是SQLContext和HiveContext的组合，所以在SQLContext和HiveContext上可用的API在SparkSession上同样是可以使用的。
    - SparkSession内部封装了SparkContext，所以计算实际上是由SparkContext完成的。
    - 创建SparkSession：
      ```
      val sparkSission = SparkSession
        .builder()
        .appName("Spark SQL Example")
        .config("spark.some.config.option", "some-value")
        .getOrCreate()
      ```
    
## DataFrame

### 创建DataFrame：

  - 创建DataFrame有三种方式：
    - 通过Spark的数据源进行创建（sparkSession.read）：
      - 示例：val df = sparkSession.read.json("/data/sample.json")
    - 从一个存在的RDD进行转换。
    - 从Hive Table进行查询返回。
 
### SQL风格语法

  - 临时表：
    - 对DataFrame创建一个临时表: 
      - df.createOrReplaceTempView("temp")
    - 通过SQL语句实现查询全表: 
      - val sqlDF = sparkSession.sql("SELECT * FROM temp")
    - 注意细节：临时表是Session范围内的，Session退出后，临时表就失效了。
  - 全局表：
    - 对于DataFrame创建一个全局表：
      - df.createGlobalTempView("global")
    - 通过SQL语句实现查询全表: 
      - spark.sql("SELECT * FROM global_temp.global").show()
    - 注意细节：全局表是跨Session有效的。
    
### RDD转换为DateFrame

  - 如果需要RDD与DF或者DS之间操作，那么都需要引入 import sparkSession.implicits._
  - 示例：
     ```
     case class People(name:String, age:Int)
     val peopleRDD = sc
      .textFile("examples/people.txt")
      .map(_.split(","))
      .map(attributes => Person(attributes(0), attributes(1).trim.toInt))
      .toDF()
    ```
    
### DateFrame转换为RDD

  - 直接调用rdd即可：val dfToRDD = df.rdd
  
## DataSet

  - Dataset是具有强类型的数据集合，需要提供对应的类型信息。
  
### 创建DataSet

  - 创建一个样例类: 
    - case class Person(name: String, age: Long)
  - 创建DataSet: 
    - val caseClassDS = Seq(Person("Andy", 32)).toDS()
  
### RDD转换为DataSet

  - 示例：
    ```
    case class People(name:String, age:Int)
    val peopleRDD = sc
      .textFile("examples/people.txt")
      .map(_.split(","))
      .map(attributes => Person(attributes(0), attributes(1).trim.toInt))
      .toDS()
    ```
    
### DataSet转换为RDD

  - 调用rdd方法即可：caseClassDS.rdd
  
## DataFrame与DataSet的互操作

  - DataFrame转换为DataSet:
    - 创建一个DateFrame：
      - val df = sparkSession.read.json("examples/people.json")
    - 创建一个样例类：
      - case class Person(name: String, age: Long)
    - 将DateFrame转化为DataSet：
      - df.as[Person]
  - DataSet转换为DataFrame：
    - 创建一个样例类：
      - case class Person(name: String, age: Long)
    - 创建DataSet：
      - val ds = Seq(Person("Andy", 32)).toDS()
    - 将DataSet转化为DataFrame：
      - val df = ds.toDF
  
## RDD、DataFrame、DataSet

  - RDD (Spark1.0) —> Dataframe(Spark1.3) —> Dataset(Spark1.6)
  - 如果同样的数据都给到这三个数据结构，他们分别计算之后，都会给出相同的结果。不同是的他们的执行效率和执行方式。
  - Spark 2.0开始，DataFrame和Dataset使用统一的Dataset API。
    - DataFrame当作一些通用对象Dataset[Row]的集合的一个别名，而一行就是一个通用的无类型的JVM对象。
    - Dataset就是一些有明确类型定义的JVM对象的集合，通过你在Scala中定义的Case Class或者Java中的Class来指定。
  - 三者的共性:
    - 全都是spark平台下的分布式弹性数据集，为处理超大型数据提供便利。
    - 三者都有惰性机制，只有在遇到Action如foreach时，三者才会开始遍历运算。
    - 三者都会根据spark的内存情况自动缓存运算，这样即使数据量很大，也不用担心会内存溢出。
    - 三者都有partition的概念。
    - 三者有许多共同的函数，如filter，排序等。
    - 在对DataFrame和Dataset进行操作许多操作都需要import spark.implicits._
  - 三者的区别：
    - RDD不支持Spark SQL操作。
    - DataFrame与Dataset均支持Spark SQL的操作。



