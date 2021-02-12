# DML数据操作

## 数据导入

### 从文件中向表中装载数据(load)：
  - load data [local] inpath <file path> [overwrite] into table <table_name> [partition(col=val,...)];
  - 注意细节：
    - load data: 表示加载数据。
    - local: 表示从本地加载数据到hive表；否则从HDFS加载数据到hive表。
    - inpath: 表示加载数据的路径。
    - overwrite: 表示覆盖表中已有数据，否则表示追加。
    - into table: 表示加载到哪张表。
    - partition: 表示上传到指定分区。
  
### 通过查询语句向表中插入数据(insert)
  - 基本插入数据：
    - insert into table <table_name> values(...);
  - 基本模式插入（根据单张表查询结果）：
    - insert overwrite table student select id, name from student_2 where id > 10;
 
### 查询语句中创建表并加载数据(as select)

  - create table if not exists student_2 as select id, name from student;
  
### 导入数据到指定的Hive表(import)

  - import table <table_name> from <hdfs_file_path>;
  
## 数据导出

### insert导出
  - 将查询的结果导出到本地：
    - insert overwrite local directory <local_path> select * from <table_name>;
  - 将查询的结果格式化导出到本地：
    - insert overwrite local directory <local_path> row format delimited fields terminated by '\t' select * from <table_name>;
  - 将查询的结果导出到HDFS上(没有local)：
    - insert overwrite directory <hdfs_path> row format delimited fields terminated by '\t' select * from <table_name>;
  
### Hadoop命令导出到本地

  - dfs -get <hdfs_file_path> <local_file_path>
    
### Export导出到HDFS上

  - export table <table_name> to <hdfs_file_path>;

  
  
