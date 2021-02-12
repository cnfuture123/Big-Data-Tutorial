# Hive常用操作

## Hive基本操作

  - 启动hive：bin/hive
  - 创建库：create database if not exists mydb;
  - 查看数据库：show databases;
  - 切换数据库：use mydb;
  - 删除库：drop database dbname;
  - 显示数据库里的表：show tables;
  - 创建表：create table student(id int, name string);
  - 修改表：
    - 重命名表：alter table student rename to stu;
    - 修改SerDe信息：alter table student set SERDEPROPERTIES ('field.delim' = '-');
    - alter table student add columns (course string);
  - 删除表：drop table if exists mytable;
  - 清空表：truncate table student;
  - 查看表的结构：desc student;
  - 向表中插入数据：insert into student values(1, "cn1");
  - 查询表中数据：select * from student;
  - Join查询：
    - Hive支持等值连接，外连接和左右连接，不支持非等值连接。
  - 退出hive：quit;
  
## 将本地文件导入Hive

  - 本地创建student.txt。包含student信息，以tab键间隔。
  - 创建新的hive表：
    - create table student(id int, name string) row format delimited fields terminated by '\t';
    - load data local inpath '/data/student.txt' into table student;
    
## Hive常用交互命令

  - 查看Hive常用命令：bin/hive -help
  - '-e' : 不进入hive的交互窗口执行sql语句：
    - bin/hive -e "select id from student;"
  - '-f' : 执行指定脚本文件中的sql语句：
    - bin/hive -f /data/student.sql
    
## Hive常见属性配置

  - Hive数据仓库位置配置：
    - 数据仓库的默认位置是：/user/hive/warehouse。
    - 可在hive-site.xml文件中配置。
  - Hive运行日志信息配置：
    - 日志默认存储路径是：/tmp/<user name>/hive.log
    - 可在hive-log4j.properties文件中配置：
      - hive.log.dir=<log path>
  - 参数配置方式：
    - 配置文件方式：
      - 默认配置文件：hive-default.xml。
      - 用户自定义配置文件：hive-site.xml。
      - Hive也会读入Hadoop的配置，因为Hive是作为Hadoop的客户端启动的，Hive的配置会覆盖Hadoop的配置。
      - hive-site.xml > hive-default.xml > Hadoop配置
    - 命令行参数方式：
      - 启动Hive时，可以在命令行添加-hiveconf param=value来设定参数。例如：bin/hive -hiveconf mapred.reduce.tasks=5;
      - 仅对本次Hive启动有效。
    - 参数声明方式：
      - 可以在HQL中使用SET关键字设定参数。
      
