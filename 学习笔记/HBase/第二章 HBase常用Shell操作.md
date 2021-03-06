# HBase常用Shell操作

  - 进入HBase客户端命令行: bin/hbase shell
  - 查看当前数据库中有哪些表: list
  - 创建表: 
    - create 'table_name', 'column_family'
    - create 'table_name', {NAME => 'f1', VERSIONS => 1, TTL => 2592000}
    - 例子：create 'student', 'info'
  - 修改表结构：
    - 添加列族：
      - alter 'table_name', 'column_family'
    - 删除列族：
      - alter 'table_name', {NAME => 'column_family', METHOD => 'delete'}
    - 修改列族属性：
      - alter 't1', 'f1', {VERSIONS => 3}
  - 查看表结构:
    - desc/describe 'table_name'
    - 例子：desc 'student'
  - 判断表是否存在：
    - exists 'table_name'
  - 启用/禁用表
    - enable 'table_name'
    - disable 'table_name'
    - is_disabled 'table_name' //查询表是否禁用
  - 插入数据到表: 
    - put 'table_name', 'row_key', 'column_family:column', 'column_value'
    - 例子：put 'student','1001','info:age','18'
  - 扫描查看表数据：
    - scan 'table_name', {COLUMN => 'column_family'}
    - 例子：
      - scan 'student'
      - scan 'student', {STARTROW => '1001', STOPROW => '1101'}
  - 更新指定字段的数据:
    - put '<table_name>', '<row_key>', '<column_family:column>', '<new_column_value>'
    - 例子：put 'student','1001','info:age','28'
  - 查看“指定行”或“指定列族:列”的数据:
    - get 'table_name', 'row_key'
    - get 'table_name', 'row_key', 'column_family:column'
  - 删除数据：
    - 删除某rowkey的全部数据：deleteall 'table_name', 'row_key'
    - 删除某rowkey的某一列数据：delete 'table_name', 'row_key', 'column_family:column'
  - 清空表数据:
    - truncate 'table_name'
    - 清空表的操作顺序为先disable，然后再truncate。
  - 统计表数据行数:
    - count 'table_name'
  - 删除表:
    - disable 'table_name'
    - drop 'table_name'
  - 修饰词：
    - COLUMNS: 查询同一个列族的多个列
      - scan 'table_name' {COLUMNS => [column_family1:column1, column_family2:column2]}
    - TIMESTAMP: 指定时间戳
      - get 'table_name', 'row_key', {COLUMNS => 'c1', TIMESTAMP => ts1}
    - LIMIT: 返回的行数
      - scan 'table_name', {LIMIT => 行数}
  - Split/Merge操作：
    - split 'tableName', 'splitKey'
    - split 'regionName', 'splitKey'
    - merge_region 'region1', 'region2'
    - merge_region 'region1', 'region2', true
  - 查看HBase集群状态：
    - hbase hbck -details
    - hbase hbck table1, table2
    
  
  
