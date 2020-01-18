# SparkSQL编程

  - SparkSession：
    - SparkSession是Spark最新的SQL查询起始点，实质上是SQLContext和HiveContext的组合，所以在SQLContext和HiveContext上可用的API在SparkSession上同样是可以使用的。
    - SparkSession内部封装了SparkContext，所以计算实际上是由SparkContext完成的。
    
## DataFrame

### 创建DataFrame：

  - 创建DataFrame有三种方式：
    - 通过Spark的数据源进行创建（spark.read）：
      - 示例：读取json文件创建DataFrame：val df = spark.read.json("/data/sample.json")
    - 从一个存在的RDD进行转换。
    - 从Hive Table进行查询返回。
 
### SQL风格语法

  - 临时表：
    - 对DataFrame创建一个临时表: df.createOrReplaceTempView("temp")
    - 通过SQL语句实现查询全表: val sqlDF = spark.sql("SELECT * FROM temp")
    - 注意细节：临时表是Session范围内的，Session退出后，表就失效了。
  - 全局表：
    - 对于DataFrame创建一个全局表：df.createGlobalTempView("global")
    - 通过SQL语句实现查询全表: spark.sql("SELECT * FROM global_temp.global").show()
    
### RDD转换为DateFrame

  - 如果需要RDD与DF或者DS之间操作，那么都需要引入 import spark.implicits._，其中spark不是包名，而是sparkSession对象的名称。
  - 示例：
    - val peopleRDD = sc.textFile("examples/people.txt")
    - case class People(name:String, age:Int)
    - peopleRDD.map{ x => val para = x.split(","); People(para(0),para(1).trim.toInt)}.toDF
    
### DateFrame转换为RDD

  - 直接调用rdd即可：val dfToRDD = df.rdd
  
## DataSet

  - Dataset是具有强类型的数据集合，需要提供对应的类型信息。
  
### 创建DataSet

  - 创建一个样例类: case class Person(name: String, age: Long)
  - 创建DataSet: val caseClassDS = Seq(Person("Andy", 32)).toDS()
  
### RDD转换为DataSet

  - 示例：
    - val peopleRDD = sc.textFile("examples/people.txt")
    - case class People(name:String, age:Int)
    - peopleRDD.map(line => {val para = line.split(",");People(para(0),para(1).trim.toInt)}).toDS()
    
### DataSet转换为RDD

  - 调用rdd方法即可：caseClassDS.rdd
  
## DataFrame与DataSet的互操作

  - DataFrame转换为DataSet:
    - 创建一个DateFrame：val df = spark.read.json("examples/people.json")
    - 创建一个样例类：case class Person(name: String, age: Long)
    - 将DateFrame转化为DataSet：df.as[Person]
  - DataSet转换为DataFrame：
    - 创建一个样例类：case class Person(name: String, age: Long)
    - 创建DataSet：val ds = Seq(Person("Andy", 32)).toDS()
    - 将DataSet转化为DataFrame：val df = ds.toDF
    
### DataSet转DataFrame

  - 导入隐式转换：import spark.implicites._
  - 转换：val testDF = testDS.toDF
  
### DataFrame转DataSet

  - 导入隐式转换：import spark.implicites._
  - 创建样例类：case class Person(name: String, age: Long)
  - 转换：val testDS = testDF.as[Person]
  
## RDD、DataFrame、DataSet

  - RDD (Spark1.0) —> Dataframe(Spark1.3) —> Dataset(Spark1.6)
  - 如果同样的数据都给到这三个数据结构，他们分别计算之后，都会给出相同的结果。不同是的他们的执行效率和执行方式。
  - DataSet会逐步取代RDD和DataFrame成为唯一的API接口。
  - 三者的共性:
    - 全都是spark平台下的分布式弹性数据集，为处理超大型数据提供便利。
    - 三者都有惰性机制，只有在遇到Action如foreach时，三者才会开始遍历运算。
    - 三者都会根据spark的内存情况自动缓存运算，这样即使数据量很大，也不用担心会内存溢出。
    - 三者都有partition的概念。
    - 三者有许多共同的函数，如filter，排序等。
    - 在对DataFrame和Dataset进行操作许多操作都需要import spark.implicits._包进行支持。
  - 三者的区别：
    - RDD不支持sparksql操作。
    - DataFrame每一行的类型固定为Row，每一列的值没法直接访问，只有通过解析才能获取各个字段的值。
    - DataFrame与Dataset一般不与spark mlib同时使用。
    - DataFrame与Dataset均支持sparksql的操作。










  
  
  
  
  
  
  
  
  
  
      
  
