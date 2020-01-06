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

