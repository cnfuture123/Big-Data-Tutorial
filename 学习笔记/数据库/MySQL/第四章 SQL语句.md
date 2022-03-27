## 数据定义语句

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
  - ALTER TABLE Statement:（重要）
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

  - CREATE TABLE Statement:（重要）
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

## 数据操作语句

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
  - SELECT Statement:（重要）
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

## 事务和锁语句

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
    - 获取表锁：
      - LOCK TABLES语句可以获取到当前会话的表锁
        - READ [LOCAL] lock: 读锁
          - 持有锁的的会话可以从表中读数据，不能写数据
          - 多个会话可以同时获取表的读锁
        - WRITE lock: 写锁
          - 持有锁的的会话可以从表中读数据和写数据
          - 只有持有锁的可以访问表数据，其他的会话在释放锁之前不能访问表数据
        - 示例：
          ```
          LOCK TABLE t READ;
          ```
    - 释放表锁：
      - 会话可以使用UNLOCK TABLES语句显式地释放锁
      - 在获取新锁之前，已经存在的锁会隐式地释放
  - SET TRANSACTION Statement：
    - 语法：
      ```
      SET [GLOBAL | SESSION] TRANSACTION
          transaction_characteristic [, transaction_characteristic] ...

      transaction_characteristic: {
          ISOLATION LEVEL level
        | access_mode
      }

      level: {
           REPEATABLE READ
         | READ COMMITTED
         | READ UNCOMMITTED
         | SERIALIZABLE
      }

      access_mode: {
           READ WRITE
         | READ ONLY
      }
      ```
    - 用于定义事务的特性，例如：事务的隔离界别和访问模式
      - 事务隔离级别：
        - 使用ISOLATION LEVEL指定
        - 可选值包括：READ COMMITTED, READ UNCOMMITTED, REPEATABLE READ, SERIALIZABLE; 默认值是REPEATABLE READ
      - 事务访问模式：
        - 使用READ WRITE or READ ONLY指定
        - 默认是READ WRITE模式，在事务操作时允许对表数据进行读写
        - READ ONLY模式不允许写操作
- Prepared Statements：
  - 预准备的语句优势：
    - 执行时解析语句的开销更小
    - 防止SQL注入攻击
  - PREPARE, EXECUTE, and DEALLOCATE PREPARE Statements：
    - PREPARE：准备要执行的语句
      - 语法：
        ```
        PREPARE stmt_name FROM preparable_stmt
        ```
    - EXECUTE：执行预准备的语句
      - 语法：
        ```
        EXECUTE stmt_name
          [USING @var_name [, @var_name] ...]
        ```
    - DEALLOCATE PREPARE：释放预准备的语句
      - 语法：
        ```
        {DEALLOCATE | DROP} PREPARE stmt_name
        ```
        
## 复合语句

  - BEGIN ... END语句：
    - 语法：
      ```
      [begin_label:] BEGIN
          [statement_list]
      END [end_label]
      ```
    - 用于写复合语句，多个语句可以写在BEGIN and END关键字之间
  - DECLARE语句：
    - DECLARE用在BEGIN ... END语句中，定义一些局部的变量，条件和处理器，或游标
    - 示例：
      ```
      DECLARE var_name [, var_name] ... type [DEFAULT value]
      ```
  - 流程控制语句：
    - CASE语句：
      - 语法：
        ```
        CASE case_value
            WHEN when_value THEN statement_list
            [WHEN when_value THEN statement_list] ...
            [ELSE statement_list]
        END CASE

        CASE
            WHEN search_condition THEN statement_list
            [WHEN search_condition THEN statement_list] ...
            [ELSE statement_list]
        END CASE
        ```
    - IF语句：
      - 语法：
        ```
        IF search_condition THEN statement_list
            [ELSEIF search_condition THEN statement_list] ...
            [ELSE statement_list]
        END IF
        ```
    - ITERATE语句：
      - 语法：
        ```
        ITERATE label
        ```
      - ITERATE只能出现在LOOP, REPEAT, and WHILE语句
    - LEAVE语句：
      - 语法：
        ```
        LEAVE label
        ```
      - 退出流程控制语句
    - LOOP语句：
      - 语法：
        ```
        [begin_label:] LOOP
            statement_list
        END LOOP [end_label]
        ```
    - REPEAT语句：
      - 语法：
        ```
        [begin_label:] REPEAT
            statement_list
        UNTIL search_condition
        END REPEAT [end_label]
        ```
    - WHILE语句：
      - 语法：
        ```
        [begin_label:] WHILE search_condition DO
            statement_list
        END WHILE [end_label]
        ```
        
## 工具语句（重要）

  - DESCRIBE语句/EXPLAIN语句：
    - DESCRIBE和EXPLAIN语句是同义的，通常DESCRIBE用于获取表结构信息，EXPLAIN用于查询执行计划
    - 语法：
      ```
      {EXPLAIN | DESCRIBE | DESC}
          tbl_name [col_name | wild]

      {EXPLAIN | DESCRIBE | DESC}
          [explain_type]
          {explainable_stmt | FOR CONNECTION connection_id}

      {EXPLAIN | DESCRIBE | DESC} ANALYZE [FORMAT = TREE] select_statement

      explain_type: {
          FORMAT = format_name
      }

      format_name: {
          TRADITIONAL
        | JSON
        | TREE
      }

      explainable_stmt: {
          SELECT statement
        | TABLE statement
        | DELETE statement
        | INSERT statement
        | REPLACE statement
        | UPDATE statement
      }
      ```
    - EXPLAIN语句提供MySQL如何执行语句的信息：
      - 获取执行计划信息：
        - EXPLAIN适用于SELECT, DELETE, INSERT, REPLACE, UPDATE, TABLE等语句
        - MySQL会显示如何处理语句，包括：如何以及按照什么顺序join表
        - 通过EXPLAIN，可以发现在什么地方加索引使语句执行更快；也可以查看是否join的执行是最优的顺序
      - EXPLAIN ANALYZE语句：
        - EXPLAIN ANALYZE会执行语句，并提供以下信息：
          - 预估的执行花销
          - 预估的返回行数
          - 返回第一行的时间
          - 返回所有数据的时间
          - 循环次数
        - 示例：

          ![image](https://user-images.githubusercontent.com/46510621/150635088-ebe42708-24e9-4537-baec-175ebe7be2ea.png)

  - MySQL慢查询
    - 开启慢查询：
      - my.ini配置文件增加配置: 主要是慢查询的定义时间（超过2秒就是慢查询），以及慢查询log日志记录（slow_query_log）
    - 分析慢查询日志，利用explain关键字可以模拟优化器执行SQL查询语句，来分析sql慢查询语句
    - 慢查询优化：
      - 索引没起作用的情况:
        - 使用LIKE关键字的查询语句: 如果匹配字符串的第一个字符为“%”，索引不会起作用。只有“%”不在第一个位置索引才会起作用
        - 使用多列索引的查询语句：对于多列索引，只有查询条件使用了这些字段中的第一个字段时，索引才会被使用
      - 优化数据库结构：
        - 分库，分表，分区
      - 分解关联查询：
        - 对关联查询进行分解，就是可以对每一个表进行一次单表查询，然后将查询结果在应用程序中进行关联，很多场景下这样会更高效
  - USE语句
    - 语法：
      ```
      USE db_name
      ```
    - 使用指定的数据库用于后续的语句
    - 示例：
      ```
      USE db1;
      SELECT COUNT(*) FROM mytable;   # selects from db1.mytable
      USE db2;
      SELECT COUNT(*) FROM mytable;   # selects from db2.mytable
      ```

