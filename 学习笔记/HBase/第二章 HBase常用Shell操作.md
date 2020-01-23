# HBase常用Shell操作

  - 进入HBase客户端命令行: bin/hbase shell
  - 查看当前数据库中有哪些表: list
  - 创建表: 
    - create '<table_name>', '<column_family>'
    - 例子：create 'student', 'info'
  - 插入数据到表: 
    - put '<table_name>', '<row_key>', '<column_family:column>', '<column_value>'
    - 例子：put 'student','1001','info:age','18'
  - 扫描查看表数据：
    - scan '<table_name>'
    - 例子：
      - scan 'student'
      - scan 'student', {STARTROW => '1001', STOPROW => '1101'}
  - 查看表结构:
    - desc/describe '<table_name>'
    - 例子：desc 'student'
  - 更新指定字段的数据:
    - put '<table_name>', '<row_key>', '<column_family:column>', '<new_column_value>'
    - 例子：put 'student','1001','info:age','28'
  - 查看“指定行”或“指定列族:列”的数据:
    - get '<table_name>', '<row_key>'
    - get '<table_name>', '<row_key>', '<column_family:column>'
  - 统计表数据行数:
    - count '<table_name>'
  - 删除数据：
    - 删除某 rowkey 的全部数据：deleteall '<table_name>', '<row_key>'
    - 删除某 rowkey 的某一列数据：delete '<table_name>', '<row_key>', '<column_family:column>'
  - 清空表数据:
    - truncate '<table_name>'
    - 清空表的操作顺序为先disable，然后再truncate。
  - 删除表:
    - disable '<table_name>'
    - drop '<table_name>'
  - 变更表信息:
    - alter 'student',{NAME=>'info',VERSIONS=>3}
    
  
  
  
  
  
  
