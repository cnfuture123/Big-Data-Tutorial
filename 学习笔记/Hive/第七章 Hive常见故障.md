# Hive常见故障

  - Hive启动失败报Cannot find hadoop installation
    - 原因分析：
      - 查看日志报错：Cannot find hadoop installation: \$HADOOP_HOME or \$HADOOP_PREFIX must be set or hadoop must be in the path
    - 解决方法：
      - Hive启动时，默认HADOOP_HOME环境变量指向/opt/huawei/Bigdata/hive-0.13.1/hadoop，但是发现用户手动在/etc/profile下配置了HADOOP_HOME环境变量，最终导致Hive启动失败
      - 手动删除/etc/profile中HADOOP_HOME配置即可
  -  怎样在Hive提交任务的时候指定队列？
    - set mapred.job.queue.name=QueueA; // 说明：队列的名称区分大小写
  - 执行load data inpath命令报错
    - 原因分析：
      - 报错信息：HiveAccessControlException Permission denied. Principal [name=user1, type=USER] does not have following privileges on Object
    - 解决方法： 
      - load data inpath命令有如下权限要求：
        - 文件的owner需要为执行命令的用户
        - 当前用户需要对该文件有读、写权限
        - 由于load操作会将该文件移动到表对应的目录中，所以要求当前用户需要对该文件的目录有写权限
  - 执行insert overwrite directory命令报错
    - 原因分析：
      - 报错信息：HiveAuthzPluginException Error getting permissions for /tmp/out/abc: Permission denied
    - 解决方法：
      - insert overwrite directory命令有如下权限要求：
        - 如果目录不存在：要求对此目录的父目录有读、写、执行权限
        - 如果目录存在，要求对此目录有读、写、执行权限
  - 执行set role admin报无权限
    - 原因分析：
      - 报错信息：FAILED: Execution Error, return code 1 from org.apache.hadoop.hive.ql.exec.DDLTask. dmp_B doesn't belong to role admin
    - 解决方法：
      - 编辑指定用户所绑定的role，选择Hive Admin Privilege
  - Hive状态为Bad问题总结
    - 可能原因：
      - DBservice服务不可用
      - HDFS服务不可用
      - ZooKeeper服务不可用
      - LDAP/KrbServer服务不可用
      - metastore实例不可用
  - Hive服务状态为Unknown总结
    - 可能原因：
      - Hive服务停止
      - hiveserver实例出现双主
      - NodeAgent未启动
  - 使用udf函数提示"Invalid function"
    - 原因分析：
      - 多个HiveServer之间或者Hive与Spark之间共用的元数据未同步，导致不同HiveServer实例内存数据不一致，造成UDF函数不生效
    - 解决方法：
      - 需要将新建的udf信息同步到HiveServer中；执行reload function操作即可
  - drop partition操作，有大量分区时操作失败
    - 原因分析：
      - 报错信息：MetaStoreClient lost connection. Attempting to reconnect
      - 查看对应MetaStore日志，有StackOverFlow异常
      - drop partition的处理逻辑是将找到所有满足条件的分区，将其拼接起来，最后统一删除。由于分区数过多，拼接元数据堆栈较深，抛出StackOverFlow
    - 解决方法：
      分批次删除分区
  - Beeline报错：Failed to execute session hooks: over max connections
    - 原因分析：
      - HiveServer连接的最大连接数默认为500，当超过500时会报这个错误
      - 业务量大导致连接HiveServer单个节点最大连接数超过了500，需要调大连接HiveServer实例的最大连接数
    - 解决方法： 
      - 修改hive.server.session.control.maxconnections配置的值到合适值，不能超过1000
      
      
        
        
