# Spark SQL

  - Spark SQL的三大功能：
    - Spark SQL可以从各种结构化数据源（例如 JSON、Hive、Parquet 等）中读取数据。
    - Spark SQL不仅支持在 Spark 程序内使用 SQL 语句进行数据查询，也支持通过标准数据库连接器（JDBC/ODBC）连接 Spark SQL 进行查询。
    - Spark SQL 支持SQL与常规的Python/Java/Scala代码高度整合，包括连接 RDD 与 SQL 表、公开的自定义 SQL 函数接口等。
  - Spark SQL提供了一种特殊的RDD，叫作SchemaRDD。SchemaRDD是存放Row对象的RDD，每个Row对象代表一行记录。SchemaRDD还包含记录的结构信息（即数据字段）。SchemaRDD可以利用结构信息更加高效地存储数据。SchemaRDD可以从外部数据源创建，也可以从查询结果或普通RDD中创建。
  
## 连接Spark SQL

  - 包含Hive支持的Spark SQL可以支持Hive表访问、UDF（用户自定义函数）、SerDe（序列化格式和反序列化格式），以及Hive查询语言（HiveQL/HQL）。
  
## 在应用中使用Spark SQL

  - 参考更新的材料学习
