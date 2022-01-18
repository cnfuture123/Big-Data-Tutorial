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

        
