# MySQL

## MySQL概述

  - MySQL是一个快速的，多线程的，多用户的，鲁棒的SQL数据库服务器
  - MySQL是一个数据库管理系统，数据库是一个结构性数据的集合
  - MySQL数据库是关系型的：
    - 关系型数据库在分离的表中存储数据，而不是把所有数据放在一个大的存储里
    - 数据库结构是由物理文件构成，这样可以对处理速度进行优化
    - 逻辑模型，包含：数据库，表，视图，行和列，提供了灵活的编程环境
    - 可以设置规则来管理不同的属性之间的关系，例如：一对一，一对多，唯一性，必须还是可选的，以及不同表之间的关联
  - 工作在客户端/服务端或嵌入的系统中
    - MySQL数据库是一个客户端/服务端系统，包含一个多线程的SQL服务器支持不同的后端，不同的客户端程序和类库，管理工具，以及大量的API
    - 也提供一个嵌入的，多线程的类库作为MySQL服务器，可以在应用中使用去构造一个更小，更快速，容易管理的独立产品
 
## MySQL主要特性：

  - 内部特性：
    - 由C and C++编写的
    - 可以在很多平台上工作
    - 提供事务和非事务的存储引擎
    - 使用快速的，支持索引压缩的B-tree磁盘表(MyISAM)
    - 使用一个快速的，基于线程的内存分配系统
    - 使用嵌套循环join，提高join速度
    - 实现了内存中的哈希表，用于临时表
  - 数据类型：
    - 数字类型：
      - 整数类型（精确值）：INTEGER, INT, SMALLINT, TINYINT, MEDIUMINT, BIGINT
        <img width="868" alt="image" src="https://user-images.githubusercontent.com/46510621/148359515-b08250e0-0f90-4b52-9ed9-a63be56a2680.png">
      
      - 定点类型（精确值）：DECIMAL, NUMERIC
        - 在MySQL中，NUMERIC等同于DECIMAL，以二进制格式存储DECIMAL值，可以指定精度和小数位数
        - 示例：
          ```
          salary DECIMAL(5,2) //5是精度，表示有效位数；2是小数位数，表示可在小数点后存储的位数
          ```
      - 浮点类型（近似值）：FLOAT, DOUBLE
        - FLOAT：单精度，4字节
        - DOUBLE：双精度，8字节
      - 位值类型：BIT
        - 用于存储bit数据，范围是1-64
      - 超出范围和溢出处理：
        - 如果启用了严格SQL模式，MySQL会拒绝超出范围的值并报错，插入会失败
        - 如果没有启用限制模式，MySQL将值截断到该列数据类型的合理范围
    - 时间类型：
      - DATE, DATETIME, and TIMESTAMP类型：
        - DATE类型：值只有日期部分，没有时间部分。格式为'YYYY-MM-DD'，范围是'1000-01-01' to '9999-12-31'
        - DATETIME类型：值包含日期和时间。格式为'YYYY-MM-DD hh:mm:ss'，范围是'1000-01-01 00:00:00' to '9999-12-31 23:59:59'
        - TIMESTAMP类型：值包含日期和时间。范围是'1970-01-01 00:00:01' UTC to '2038-01-19 03:14:07' UTC
          - TIMESTAMP值在存储时从当前时区转换为UTC时区，取值时从UTC时区转换为当前时区，默认当前的时区是服务器的时间
        - DATETIME和TIMESTAMP可以包含小数秒的部分（微秒，6位），格式为'YYYY-MM-DD hh:mm:ss[.fraction]'
          ```
          CREATE TABLE t1 (t TIME(3), dt DATETIME(6));
          ```
        - 无效的DATE, DATETIME, or TIMESTAMP值会转换成zero值，例如：'0000-00-00' or '0000-00-00 00:00:00'
        - DATETIME和TIMESTAMP列可以自动初始化，并更新为当前的日期和时间
          ```
          CREATE TABLE t1 (
            ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
            dt DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
          );
          ```
      - TIME类型：
        - 格式为hh:mm:ss'，范围是'-838:59:59' to '838:59:59'
        - 可以包含小数秒的部分（微秒，6位），格式为'-838:59:59.000000' to '838:59:59.000000'
      - YEAR类型：
        - 用一个字节表示年份值，YYYY格式
        - 范围是1901 to 2155，和0000
    - String类型：
      - CHAR and VARCHAR类型
        - CHAR和VARCHAR是类似的，区别在于存值和取值
        - CHAR和VARCHAR类型会声明一个长度，表示存储的最大字符长度，例如：CHAR(30)可以存储最多30个字符
        - 两者区别：
          - CHAR类型的列长度是固定的，范围是0-255，存储时用空格向右填充到指定长度，取值时填充的空格会被删除
          - VARCHAR类型的列是长度可变的，范围是0-65535，存储时会有1字节或2字节的前缀加上数据，前缀表示实际值的字节数。VARCHAR类型的值存储时不会被填充
          - 示例：
            <img width="643" alt="image" src="https://user-images.githubusercontent.com/46510621/148506023-fca66bf3-0c72-4c7f-9339-74ce9f43bb6d.png">

      - BINARY and VARBINARY类型
        - BINARY and VARBINARY存储二进制字子节符串
        - BINARY最大长度是255字节，VARBINARY最大长度是65535字节
        - 两者区别：
          - BINARY值存储时用0x00(zero byte)向右填充到指定长度，取值时不会删除填充的值
          - VARBINARY类型在存储时不会填充值，并且在取值时也不会删除前后的字节
      - BLOB and TEXT类型
        - BLOB是一个二进制大对象，可以存储可变数量的数据，4种BLOB类型是TINYBLOB, BLOB, MEDIUMBLOB, and LONGBLOB，区别在存储数据的最大长度
        - 4种TEXT类型是TINYTEXT, TEXT, MEDIUMTEXT, and LONGTEXT，长度和BLOB类型相对应
        - 两者区别：
          - BLOB值是二进制字符串，TEXT是非二进制字符串
          - BLOB可以看作是足够大的VARBINARY列，TEXT可以看作足够大的VARBINARY列
      - ENUM类型
        - ENUM是一个字符串对象，它的值是来自于创建表时该列规范中预定义的枚举值列表
        - ENUM类型优缺点：
          - 优点：
            - 当一个列的可能值是一个有限的集合，数据存储时的结构更紧凑，输入值会自动编码为数字
            - 查询和输出是可读的，在查询结果中数字会转换为相应的字符串
      - SET类型：
        - SET是一个字符串对象，可以有0个或多个值。set元素之间以,分隔
          ```
          SET('one', 'two')
          ```
        - SET列最多有64个不重复的元素，重复值会导致告警或报错
    - 数据类型默认值：
      - 显示的默认值：
        - 通过DEFAULT子句指定默认值，可以是一个文本常量或表达式
        - 示例：
          ```
          CREATE TABLE t1 (
            -- literal defaults
            i INT         DEFAULT 0,
            c VARCHAR(10) DEFAULT '',
            -- expression defaults
            f FLOAT       DEFAULT (RAND() * RAND()),
            b BINARY(16)  DEFAULT (UUID_TO_BIN(UUID())),
            d DATE        DEFAULT (CURRENT_DATE + INTERVAL 1 YEAR)
          );
          ```
        - BLOB, TEXT, GEOMETRY, and JSON类型只有在值用表达式时才能被指定默认值
        - 在MySQL 8.0.13之前，DEFAULT子句必须是文本常量，不能是函数或表达式
      - 隐式的默认值：
        - 如果没有使用DEFAULT显示指定默认值，则MySQL按照以下方式确定默认值：
          - 如果该列的值可以为NULL，则指定DEFAULT NULL
          - 如果该列的值不能为NULL，则不指定DEFAULT子句
    - 数据类型存储要求：
      - 表数据在磁盘上的存储要求取决于几个因素：
        - 不同的存储引擎表示和存储数据的方式不同
        - 对于某列或者某行表数据可能被压缩
      - 数字类型存储要求：
      
        <img width="560" alt="image" src="https://user-images.githubusercontent.com/46510621/149889748-d96ec441-0569-4782-8199-aac242c99c6b.png">
        
      - 时间类型存储要求：
      
        <img width="669" alt="image" src="https://user-images.githubusercontent.com/46510621/149890305-e8b6e776-89ad-49e0-b821-d5bf94052ecb.png">

        - 秒的小数精度：
        
          <img width="469" alt="image" src="https://user-images.githubusercontent.com/46510621/149890772-a7cf0241-804a-489f-90fb-651922f88a80.png">
  
      - 字符串类型存储要求：
      
        <img width="877" alt="image" src="https://user-images.githubusercontent.com/46510621/149891168-daab6e63-0933-408d-b637-bc9a673bda43.png">

## 函数和操作符

  - 流程控制函数：
    - CASE:
      - 格式：
        ```
        CASE value WHEN compare_value THEN result [WHEN compare_value THEN result ...] [ELSE result] END
        示例：
        SELECT CASE 1 WHEN 1 THEN 'one' WHEN 2 THEN 'two' ELSE 'more' END；
        ```
        ```
        CASE WHEN condition THEN result [WHEN condition THEN result ...] [ELSE result] END
        示例：
        SELECT CASE WHEN 1>0 THEN 'true' ELSE 'false' END;
        ```
    - IF(expr1,expr2,expr3)：
      - if expr1 is true, then return expr2, else return expr3
      - 示例：
        ```
        SELECT IF(1<2,'yes','no');
        ```
    - IFNULL(expr1,expr2):
      - If expr1 is not NULL, IFNULL() returns expr1; otherwise it returns expr2
      - 示例：
        ```
        SELECT IFNULL(NULL,10);
        ```
    - NULLIF(expr1,expr2):
      - Returns NULL if expr1 = expr2 is true, otherwise returns expr1
      - 示例：
        ```
        SELECT NULLIF(1,1);
        ```
  - 日期时间函数：
    - CURDATE()/CURRENT_DATE()：当前日期，格式为'YYYY-MM-DD' or YYYYMMDD
    - CURTIME()：当前时间，格式为'hh:mm:ss' or hhmmss
    - DATE(expr)：获取expr表达式的日期部分值
    - DATEDIFF(expr1,expr2)：expr1 − expr2，表示两者相差的天数
    - DATE_ADD(date,INTERVAL expr unit)/DATE_SUB(date,INTERVAL expr unit)：
      - date参数指定开始日期
      - expr表达式指定距开始日期需要加或减的时间间隔
      - unit是表达式的单位
      - 示例：
        ```
        SELECT DATE_ADD('2018-05-01',INTERVAL 1 DAY);
        SELECT DATE_SUB('2018-05-01',INTERVAL 1 YEAR);
        SELECT DATE_SUB('1998-01-02', INTERVAL 31 DAY);
        ```
    - DATE_FORMAT(date,format)：
      - 根据format格式化date日期
      - 示例：
        ```
        SELECT DATE_FORMAT('2007-10-04 22:23:00', '%H:%i:%s');
        SELECT DATE_FORMAT('2009-10-04 22:23:00', '%W %M %Y');
        ```
    - STR_TO_DATE(str,format):
      - 根据format格式从str字符串中提取日期或时间部分值
      - 示例：
        ```
        SELECT STR_TO_DATE('01,5,2013','%d,%m,%Y');
        SELECT STR_TO_DATE('a09:30:17','a%h:%i:%s');
        ```
    - TIME(expr):
      - 提取表达式中时间部分的值
      - 示例：
        ```
        SELECT TIME('2003-12-31 01:02:03.000123');
        ```
    - TIMEDIFF(expr1,expr2):
      - expr1 − expr2，两者相差的时间值
      - 示例：
        ```
        SELECT TIMEDIFF('2008-12-31 23:59:59.000001', '2008-12-30 01:01:01.000002');
        ```
    - TIMESTAMP(expr):
      - 将expr表示为一个datetime值
      - 示例：
        ```
        SELECT TIMESTAMP('2003-12-31');
        ```
    - TIMESTAMP(expr1,expr2):
      - 将expr2的时间加到expr1，返回datetime值
      - 示例：
        ```
        SELECT TIMESTAMP('2003-12-31 12:00:00','12:00:00');
        ```
    - TIMESTAMPADD(unit,interval,datetime_expr):
      - 将interval加到datetime_expr，unit表示interval的单位
      - 示例：
        ```
        SELECT TIMESTAMPADD(MINUTE,1,'2003-01-02');
        SELECT TIMESTAMPADD(WEEK,1,'2003-01-02');
        ```
    - TIMESTAMPDIFF(unit,datetime_expr1,datetime_expr2):
      - datetime_expr2 − datetime_expr1, unit指定返回结果的单位
      - 示例：
        ```
        SELECT TIMESTAMPDIFF(MONTH,'2003-02-01','2003-05-01');
        ```
    - UTC_DATE, UTC_DATE():
      - 当前的UTC日期，格式为'YYYY-MM-DD' or YYYYMMDD
      - 示例：
        ```
        SELECT UTC_DATE(), UTC_DATE() + 0;
        ```
    - UTC_TIME, UTC_TIME([fsp]):
      - 当前的UTC时间，格式为'hh:mm:ss' or hhmmss
      - 示例：
        ```
        SELECT UTC_TIME(), UTC_TIME() + 0;
        ```
  - 字符串函数和操作符：
    - CONCAT(str1,str2,...)：
      - 将参数串联在一起的字符串返回，如果任意参数为NULL则返回NULL
      - 示例：
        ```
        SELECT CONCAT('My', 'S', 'QL');
        SELECT CONCAT('My', NULL, 'QL');
        ```
    - CONCAT_WS(separator,str1,str2,...):
      - 基于separator分隔符串联参数
      - 示例：
        ```
        SELECT CONCAT_WS(',','First name','Second name','Last Name');
        ```
    - LOWER(str)/UPPER(str):
      - 将str转换为小写/大写
    - REVERSE(str)：
      - 将str顺序反转
  - 转换函数和操作符：
    - BINARY expr：
      - 将expr转换为二进制字符串
    - CAST(expr AS type [ARRAY])：
      - 将expr转换为type类型
      - 示例：
        ```
        SELECT CAST(1944.35 AS YEAR), CAST(1944.50 AS YEAR);
        ```
  - 聚合函数：
    - AVG([DISTINCT] expr) [over_clause]：返回expr的平均值
    - COUNT(expr) [over_clause]：返回SELECT查询结果行中expr的非NULL值的数量
      - COUNT(*)返回结果中包含NULL值
    - COUNT(DISTINCT expr,[expr...])：返回匹配expr不同的非NULL的行数
    - MAX([DISTINCT] expr) [over_clause]：返回expr的最大值
    - MIN([DISTINCT] expr) [over_clause]：返回expr的最小值
    - STD(expr) [over_clause]：返回总体标准差
    - SUM([DISTINCT] expr) [over_clause]：返回expr的和
    - VARIANCE(expr) [over_clause]：返回总体标准方差
    - GROUP BY：
      - 支持按照某些列维度计算统计信息，产生摘要
      - 示例：
        ```
        SELECT year, SUM(profit) AS profit FROM sales
        GROUP BY year;
        ```
  - 窗函数：
    - DENSE_RANK() over_clause：
      - 返回当前行在其分区的排名，没有间隔
      - 相同值的行排名相同，并且排名是连续的
    - RANK() over_clause：
      - 返回当前行在其分区的排名，有间隔
      - 相同值的行排名相同，排名是不连续的
    - ROW_NUMBER() over_clause：
      - 返回当前行在其分区的行数
      - 不同行的行数是不相同的
    - DENSE_RANK(), RANK(), ROW_NUMBER()的区别：
      
      <img width="803" alt="image" src="https://user-images.githubusercontent.com/46510621/149930623-4fed95f9-0802-42bf-ae17-b4e1e461dc00.png">

    - LAG(expr [, N[, default]]) [null_treatment] over_clause:
      - 返回当前行在其分区内之前N行的expr值
    - LEAD(expr [, N[, default]]) [null_treatment] over_clause：
      - 返回当前行在其分区内之后N行的expr值
      - 示例：
      
        <img width="697" alt="image" src="https://user-images.githubusercontent.com/46510621/149932269-42136103-7a9c-497c-88e4-7eaccdeff4a1.png">

    - 窗口函数概念：
      - 窗口函数在一组行数据上执行类聚合的操作，区别是聚合操作将多行数据聚合产生单个结果行，然而窗口函数是为每一行都产生一个结果
      - OVER子句：
        - 语法：
          ```
          {OVER (window_spec) | OVER window_name}
          ```
        - OVER (window_spec)语法：
          ```
          window_spec:
            [window_name] [partition_clause] [order_clause] [frame_clause]
          ```
          - window_name: WINDOW子句定义的窗口名称
          - partition_clause: PARTITION BY子句指定如何对查询的行进行分组，如果没有指定PARTITION BY，则包含所有的查询行
            ```
            PARTITION BY expr [, expr] ...
            ```
          - order_clause: ORDER BY子句指定在分区内如何对行数据进行排序
            ```
            ORDER BY expr [ASC|DESC] [, expr [ASC|DESC]] ...
            ```
          - frame_clause: frame是当前分区的一个子集，frame子句用于指定如何定义子集
        - 示例：
          
          <img width="675" alt="image" src="https://user-images.githubusercontent.com/46510621/150061330-08aac27a-e2a6-4ec1-96fb-a18a85621df5.png">

      - 窗口函数的限制：
        - SQL标准规定窗口函数不能用于UPDATE or DELETE语句中
        - MySQL不支持以下窗口函数特性：
          - DISTINCT用于聚合窗口函数
          - 嵌套的窗口函数
          - 依赖当前行值的动态frame endpoint

## SQL语句

  - 数据定义语句：
    - ALTER DATABASE Statement:
      - 用于修改数据库的整体特性，需要对数据库有ALTER权限
      - 语法：
        ```
        ALTER {DATABASE | SCHEMA} [db_name]
            alter_option ...

        alter_option: {
            [DEFAULT] CHARACTER SET [=] charset_name
          | [DEFAULT] COLLATE [=] collation_name
          | [DEFAULT] ENCRYPTION [=] {'Y' | 'N'}
          | READ ONLY [=] {DEFAULT | 0 | 1}
        }
        ```
        - CHARACTER SET: 修改默认的数据库字符集
        - COLLATE: 修改默认的数据库校验
        - ENCRYPTION: 
          - 数据库的加密方式，会被数据库中的表继承。
          - 可选值为'Y' (encryption enabled) and 'N' (encryption disabled)
        - READ ONLY: 
          - 控制是否允许对数据库的修改
          - 可选值为DEFAULT or 0 (not read only) and 1 (read only)
    - ALTER PROCEDURE Statement：
      - 用于修改存储过程的特性
      - 语法：
        ```
        ALTER PROCEDURE proc_name [characteristic ...]

        characteristic: {
            COMMENT 'string'
          | LANGUAGE SQL
          | { CONTAINS SQL | NO SQL | READS SQL DATA | MODIFIES SQL DATA }
          | SQL SECURITY { DEFINER | INVOKER }
        }
        ```
    - ALTER TABLE Statement:
      - 用于修改表结构，例如：可以增删列，创建或销毁索引，修改已有列的类型，重命名列名等
      - 语法：
        ```
        ALTER TABLE tbl_name
            [alter_option [, alter_option] ...]
            [partition_options]
        ```
      - 表可选项：
        - ENGINE, AUTO_INCREMENT, AVG_ROW_LENGTH, MAX_ROWS, ROW_FORMAT, or TABLESPACE等
        - 常用示例：
          - 修改存储引擎：
            ```
            ALTER TABLE t1 ENGINE = InnoDB;
            ```
          - 修改行存储的格式：
            ```
            ALTER TABLE t1 ROW_FORMAT = COMPRESSED;
            ```
          - 重置当前的自动自增值：
            ```
            ALTER TABLE t1 AUTO_INCREMENT = 13;
            ```
          - 修改表的字符集：
            ```
            ALTER TABLE t1 CHARACTER SET = utf8;
            ```
          - 修改表的注释：
            ```
            ALTER TABLE t1 COMMENT = 'New table comment';
            ```
      - 并发控制：
        - 可以使用LOCK子句控制表在被修改时并发读写的程度
        - LOCK子句的参数：DEFAULT, NONE, SHARED, EXCLUSIVE
      - 增删列：
        - ADD：增加列，默认是在最后添加列
        - DROP：删除列
      - 重命名，重定义，重排序列
        - CHANGE: 
          - 可以重命名列或者改变列定义
          - 使用 FIRST or AFTER可以改变列的顺序
        - MODIFY: 改变列定义
        - RENAME COLUMN: 重命名列
        - 示例：
          ```
          ALTER TABLE t1 CHANGE a b BIGINT NOT NULL;
          ALTER TABLE t1 MODIFY b INT NOT NULL;
          ALTER TABLE t1 RENAME COLUMN b TO a;
          ```
      - 增删外键：
        - 语法：
          ```
          ADD CONSTRAINT name FOREIGN KEY (....) ...
          ALTER TABLE tbl_name DROP FOREIGN KEY fk_symbol;
          ```
      - 修改分区：
        - 设置分区：
          ```
          //Hash分区
          ALTER TABLE t1 PARTITION BY HASH(id) PARTITIONS 8;
          //Range分区
          CREATE TABLE t1 (
              id INT,
              year_col INT
          )
          PARTITION BY RANGE (year_col) (
              PARTITION p0 VALUES LESS THAN (1991),
              PARTITION p1 VALUES LESS THAN (1995),
              PARTITION p2 VALUES LESS THAN (1999)
          );
          ALTER TABLE t1 ADD PARTITION (PARTITION p3 VALUES LESS THAN (2002));
          ```
        - 删除分区：DROP PARTITION
          ```
          ALTER TABLE t1 DROP PARTITION p0, p1;
          ```
        - 删除某个分区数据：
          ```
          ALTER TABLE t1 TRUNCATE PARTITION p0;
          ```
        - 合并分区，减少分区数：
          ```
          ALTER TABLE t2 COALESCE PARTITION 2;
          ```
      - 增加索引：
        ```
        ALTER TABLE t2 ADD INDEX (d), ADD UNIQUE (a);
        ```
    - CREATE DATABASE Statement:
      - 语法：
        ```
        CREATE {DATABASE | SCHEMA} [IF NOT EXISTS] db_name
            [create_option] ...

        create_option: [DEFAULT] {
            CHARACTER SET [=] charset_name
          | COLLATE [=] collation_name
          | ENCRYPTION [=] {'Y' | 'N'}
        }
        ```
      - 概念：
        - MySQL中的数据库对应一个目录，包含的文件对应数据库中的表
        - MySQL对于数据库的数量没有限制，但是底层的文件系统对于目录的数量是有限制的
    - CREATE INDEX Statement：
      - 语法：
        ```
        CREATE [UNIQUE | FULLTEXT | SPATIAL] INDEX index_name
            [index_type]
            ON tbl_name (key_part,...)
            [index_option]
            [algorithm_option | lock_option] ...
        ```
      - 索引类型：
        - 列前缀作为key:
          - 对于字符串类型的列，可以使用列前缀部分创建索引，col_name(length)可以指定索引前缀的长度
          - 示例：
            ```
            CREATE INDEX part_of_name ON customer (name(10));
            ```
        - UNIQUE索引：
          - UNIQUE索引创建了一个约束是索引中的所有值都是不同的
        - FULLTEXT索引： 
          - FULLTEXT索引支持InnoDB and MyISAM表，包含CHAR, VARCHAR, and TEXT列
        - 多值索引：
          - InnoDB支持多值索引，它是定义在包含数组值的列上的二级索引
        - 空间索引：
          -  MyISAM, InnoDB, NDB, and ARCHIVE支持表示空间的列，例如：POINT and GEOMETRY
        - 每种存储引擎的索引类型：

          <img width="621" alt="image" src="https://user-images.githubusercontent.com/46510621/150103330-6f7e70f3-7533-4a75-8796-507f7ddde758.png">

    - CREATE TABLE Statement:
      - 语法：
        ```
        CREATE [TEMPORARY] TABLE [IF NOT EXISTS] tbl_name
            (create_definition,...)
            [table_options]
            [partition_options]
        ```
      - 临时表：
        - 创建表时可以使用TEMPORARY关键字
        - 临时表只在当前会话中是可见的，会话结束时自动被删除
        - 临时表和数据库的关系是松耦合的，删除数据库不会自动删除临时表
        - 示例：
          ```
          CREATE TEMPORARY TABLE new_tbl SELECT * FROM orig_tbl LIMIT 0;
          ```
      - 表的克隆和拷贝：
        - CREATE TABLE ... LIKE:
          - 基于另一个表的定义创建一个空表，包含列的属性和索引
          - 示例：
            ```
            CREATE TABLE new_tbl LIKE orig_tbl;
            ```
        - [AS] query_expression:
          - 拷贝表
          - 示例：
            ```
            CREATE TABLE new_tbl AS SELECT * FROM orig_tbl;
            ```
      - 列的数据类型和属性：
        - data_type：列定义的数据类型
        - NOT NULL | NULL：
          - 默认是NULL
          - InnoDB, MyISAM, and MEMORY存储引擎支持在有NULL值的列上索引
        - DEFAULT：给列指定默认值
        - VISIBLE, INVISIBLE：
          - 指定列的可见性，表中至少有一个可见的列
          - 不可见的列对查询是隐藏的
        - AUTO_INCREMENT：
          - 整型或浮点型的列可以指定AUTO_INCREMENT属性
          - AUTO_INCREMENT序列值从1开始，通常值是自增的，被设置为当前值+1
        - COMMENT：
          - 用于指定列的注释，最多1024个字符
      - 索引，外键，CHECK约束：
        - 索引：
          - CONSTRAINT symbol：
            - SQL标准指定所有类型的约束，包括：primary key, unique index, foreign key, check属于同一个命名空间。在MySQL中每种约束类型有自己的命名空间
          - PRIMARY KEY：
            - 唯一索引，并且所有主键列是NOT NULL
            - 一个表只能有一个主键
            - 主键可以是一个多列的索引，使用PRIMARY KEY(key_part, ...)子句定义
          - KEY | INDEX：
            - KEY和INDEX语义相同
          - UNIQUE：唯一索引
          - FOREIGN KEY：
            - 用于跨表交叉引用相关数据，使分散的数据保持一致性
            - 外键关系包含一个父表保存初始的列值，一个子表保存的列值引用父表的列值，而外键是在子表中定义的
            - 语法：
              ```
              [CONSTRAINT [symbol]] FOREIGN KEY
                  [index_name] (col_name, ...)
                  REFERENCES tbl_name (col_name,...)
                  [ON DELETE reference_option]
                  [ON UPDATE reference_option]
              ```
            - 删除外键：
              ```
              ALTER TABLE tbl_name DROP FOREIGN KEY fk_symbol;
              ```
            - 二级索引：
              - InnoDB支持在虚拟生成的列上使用二级索引，也称为虚拟索引
        - CHECK：
          - 创建约束检查表中数据
          - 语法：
            ```
            [CONSTRAINT [symbol]] CHECK (expr) [[NOT] ENFORCED]
            ```
            - expr指定约束条件为一个布尔表达式，用于检查表的每一行数据
          - CHECK约束可以是表约束或列约束
      - 存储引擎分类：

        <img width="862" alt="image" src="https://user-images.githubusercontent.com/46510621/150126470-b3c40be7-12dc-47db-a6d5-07c1900ae3d2.png">

      - 表分区：
        - 参考：https://dev.mysql.com/doc/refman/8.0/en/partitioning.html
    
    - DROP DATABASE Statement：
      - 语法：
        ```
        DROP {DATABASE | SCHEMA} [IF EXISTS] db_name
        ```
    - DROP INDEX Statement：
      - 语法：
        ```
        DROP INDEX index_name ON tbl_name
            [algorithm_option | lock_option] ...

        algorithm_option:
            ALGORITHM [=] {DEFAULT | INPLACE | COPY}

        lock_option:
            LOCK [=] {DEFAULT | NONE | SHARED | EXCLUSIVE}
        ```
      - 示例：
        ```
        DROP INDEX `PRIMARY` ON t;
        ```
    - DROP TABLE Statement:
      - 语法：
        ```
        DROP [TEMPORARY] TABLE [IF EXISTS]
          tbl_name [, tbl_name] ...
          [RESTRICT | CASCADE]
        ```
    - RENAME TABLE Statement:
      - 语法：
        ```
        RENAME TABLE
            tbl_name TO new_tbl_name
            [, tbl_name2 TO new_tbl_name2] ...
        ```
      - 示例：
        ```
        RENAME TABLE 
               old_table1 TO new_table1,
               old_table2 TO new_table2,
               old_table3 TO new_table3;
        ```
    - TRUNCATE TABLE Statement:
      - 语法：
        ```
        TRUNCATE [TABLE] tbl_name
        ```
      - 删除表中所有数据
      - TRUNCATE TABLE和DELETE区别：
        - Truncate操作是先drop然后重建表，删除的效率更高
        - Truncate操作会隐式地提交，并且不能被回滚
  - 数据操作语句：
    - CALL Statement：
      - 语法：
        ```
        CALL sp_name([parameter[,...]])
        CALL sp_name[()]
        ```
      - 用于调用存储过程
    - DELETE Statement：
      - 语法：
        ```
        DELETE [LOW_PRIORITY] [QUICK] [IGNORE]
            tbl_name[.*] [, tbl_name[.*]] ...
            FROM table_references
            [WHERE where_condition]
        ```
      - 用于删除表中的行数据，返回的是删除的行数
      - 示例：
        ```
        DELETE FROM somelog WHERE user = 'jcole' ORDER BY timestamp_column LIMIT 1;
        
        DELETE FROM t1, t2 USING t1 INNER JOIN t2 INNER JOIN t3
          WHERE t1.id=t2.id AND t2.id=t3.id;
        ```
    - DO Statement:
      - 语法：
        ```
        DO expr [, expr] ...
        ```
      - DO执行expr表达式，但是不返回结果。大多数情况DO是SELECT expr的简洁用法，优点是在不关心执行结果的时候速度更快一些
      - 示例：
        ```
        DO SLEEP(5);
        ```
    - INSERT Statement:
      - 用法：
        - INSERT ... VALUES, INSERT ... VALUES ROW(), and INSERT ... SET这几种格式显式地指定要插入的值
        - INSERT ... SELECT格式插入的值来自于另一张表
          - 示例：
            ```
            INSERT INTO tbl_temp2 (fld_id)
                SELECT tbl_temp1.fld_order_id
                FROM tbl_temp1 WHERE tbl_temp1.fld_order_id > 100;
            ```
        - INSERT带ON DUPLICATE KEY UPDATE子句的格式在UNIQUE index or PRIMARY KEY上有重复值时会更新已有的数据
          - 示例：
            ```
            INSERT INTO t1 (a,b,c) VALUES (1,2,3)
                ON DUPLICATE KEY UPDATE c=3;
            ```
    - LOAD DATA Statement:
      - 语法：
        ```
        LOAD DATA
            [LOW_PRIORITY | CONCURRENT] [LOCAL]
            INFILE 'file_name'
            [REPLACE | IGNORE]
            INTO TABLE tbl_name
        ```
      - 用于从文本文件中读取行数据到表中
      - 用法：
        - LOCAL修饰符指定读取的文件是在客户端还是服务端
      - 示例：
        ```
        LOAD DATA INFILE 'data.txt' INTO TABLE db2.my_table;
        ```
    - SELECT Statement:
      - 语法：
        ```
        SELECT
            [ALL | DISTINCT | DISTINCTROW ]
            select_expr [, select_expr] ...
            [into_option]
            [FROM table_references
              [PARTITION partition_list]]
            [WHERE where_condition]
            [GROUP BY {col_name | expr | position}, ... [WITH ROLLUP]]
            [HAVING where_condition]
            [WINDOW window_name AS (window_spec)
                [, window_name AS (window_spec)] ...]
            [ORDER BY {col_name | expr | position}
              [ASC | DESC], ... [WITH ROLLUP]]
            [LIMIT {[offset,] row_count | row_count OFFSET offset}]
            [into_option]
            [FOR {UPDATE | SHARE}
                [OF tbl_name [, tbl_name] ...]
                [NOWAIT | SKIP LOCKED]
              | LOCK IN SHARE MODE]
            [into_option]
        ```
      - 用于从表中获取行数据
      - 常见用法：
        - HAVING子句：
          - 类似WHERE子句，用于指定筛选条件
          - WHERE子句是在select列表中指定条件，不能用于聚合函数；HAVING子句通常作用于GROUP BY形成的分组数据
          - 示例：
            ```
            SELECT COUNT(col1) AS col2 FROM t GROUP BY col2 HAVING col2 = 2;
            ```
        - LIMIT子句：
          - 用于限制返回行数据的数量
          - 示例：
            ```
            SELECT * FROM tbl LIMIT 5,10;  # Retrieve rows 6-15
            ```
        - SELECT ... INTO Statement:
          - 用于将查询结果存储在变量中，或写入到文件
          - SELECT ... INTO var_list：将查询数据存入变量
          - SELECT ... INTO OUTFILE：将查询数据写入文件
          - SELECT ... INTO DUMPFILE：将查询数据整合为一行写入文件
        - JOIN子句：
          - 示例：
            ```
            SELECT * FROM t1 LEFT JOIN (t2, t3, t4)
                 ON (t2.a = t1.a AND t3.b = t1.b AND t4.c = t1.c)
            ```
        - UNION子句：
          - 语法：
            ```
            SELECT ...
                UNION [ALL | DISTINCT] SELECT ...
                [UNION [ALL | DISTINCT] SELECT ...]
            ```
          - 用于将多个SELECT查询结果组合成一个结果集
          - 示例：
            ```
            SELECT * FROM t1 UNION SELECT * FROM t2;
            ```
    - 子查询：
      - 子查询的优点：
        - 允许结构化的查询来将语句的每一部分隔离
        - 子查询相比joins和unions可读性更好，可以作为一种替代方式
      - 示例：
        ```
        SELECT * FROM t1 WHERE column1 = (SELECT column1 FROM t2);
        ```
    - TABLE Statement：
      - MySQL 8.0.19引入的语句，查询表中所有数据
      - 语法：
        ```
        TABLE table_name [ORDER BY column_name] [LIMIT number [OFFSET number]]
        ```
      - 示例：
        ```
        TABLE t;

        SELECT * FROM t;
        ```
    - UPDATE Statement：
      - 语法：
        ```
        UPDATE [LOW_PRIORITY] [IGNORE] table_reference
            SET assignment_list
            [WHERE where_condition]
            [ORDER BY ...]
            [LIMIT row_count]
        ```
      - 示例：
        ```
        UPDATE t1 SET col1 = col1 + 1, col2 = col1;
        ```
  - 事务和锁语句
    - START TRANSACTION, COMMIT, and ROLLBACK Statements：
      - 语法：
        ```
        START TRANSACTION
            [transaction_characteristic [, transaction_characteristic] ...]

        transaction_characteristic: {
            WITH CONSISTENT SNAPSHOT
          | READ WRITE
          | READ ONLY
        }

        BEGIN [WORK]
        COMMIT [WORK] [AND [NO] CHAIN] [[NO] RELEASE]
        ROLLBACK [WORK] [AND [NO] CHAIN] [[NO] RELEASE]
        SET autocommit = {0 | 1}
        ```
        - START TRANSACTION or BEGIN：开始一个新的事务
        - COMMIT：提交当前的事务，使修改持久化
        - ROLLBACK：回滚当前的事务
          - 有些语句不能回滚，例如DDL语句：create or drop databases, those that create, drop, or alter tables
        - SET autocommit：禁用或启用当前会话默认的自动提交模式
    - SAVEPOINT, ROLLBACK TO SAVEPOINT, and RELEASE SAVEPOINT Statements：
      - 语法：
        ```
        SAVEPOINT identifier
        ROLLBACK [WORK] TO [SAVEPOINT] identifier
        RELEASE SAVEPOINT identifier
        ```
        - SAVEPOINT语句设置一个事务的保存点
        - ROLLBACK TO [SAVEPOINT]语句回滚事务到某个保存点，并且不会终止事务，当前事务在保存点之后的修改会被回滚
        - RELEASE SAVEPOINT语句删除保存点
        - 执行COMMIT或ROLLBACK语句后，当前事务所有的保存点都会被删除
    - LOCK INSTANCE FOR BACKUP and UNLOCK INSTANCE Statements：
      - 语法：
        ```
        LOCK INSTANCE FOR BACKUP
        UNLOCK INSTANCE
        ```
        - LOCK INSTANCE FOR BACKUP: 
          - 获取一个实例级别的备份锁，允许在备份过程中的DML操作，但是阻止会造成不一致快照的操作
          - 多个会话可以使用同一个备份锁
        - UNLOCK INSTANCE：
          - 释放当前会话持有的备份锁
          - 会话终止时会自动释放
    - LOCK TABLES and UNLOCK TABLES Statements：
      - 语法：
        ```
        LOCK TABLES
            tbl_name [[AS] alias] lock_type
            [, tbl_name [[AS] alias] lock_type] ...

        lock_type: {
            READ [LOCAL]
          | [LOW_PRIORITY] WRITE
        }

        UNLOCK TABLES
        ```
        
        
        
