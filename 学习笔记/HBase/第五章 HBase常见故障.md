# HBase常见故障

  - HBase启动失败，系统表无法上线
    - 原因分析：
      - 查看HBase源生页面发现RegionServer上的region能正常上线，但是部分region的状态一直处于offline状态，大量region处于RIT状态
      - 查看对应RegionServer的运行日志发现报错“OutOfMemory:Direct buffer memory”，RegionServer DirectMemory不足
    - 解决方法：
      - 修改RegionServer的GC中MaxDirectMemorySize为1G，保存配置重启HBase
  - acl表目录丢失导致HBase启动失败
    - 原因分析：
      - 报错信息：TableExistsException: hbase:acl
      - 检查HDFS上HBase的路径发现acl表路径丢失
    - 解决方法：
      - 删除zk中acl表信息：
        - deleteall /hbase/table/hbase:acl
        - deleteall /hbase/table-lock/hbase:acl
  - HBase version文件损坏导致启动失败
    - 原因分析：
      - HBase启动时会读取hbase.version文件，但是日志显示读取存在异常
      - 通过hadoop fs -cat /hbase/hbase.version文件不能正常查看，该文件损坏
    - 解决方法：
      - 从本地同版本集群中获取hbase.version文件上传进行替换
  - 残留进程导致Regionsever启动失败
    - 原因分析：
      - 看启动HBase服务时manager页面的详细打印信息，提示the previous process is not quit
    - 解决方法：
      - 登录节点，后台通过执行ps -ef | grep HRegionServer发现确实存在一个残留的进程
      - 确认进程可以kill后，kill掉该进程（如果kill无法终止该进程，需要使用kill -9来强制终止该进程）
  - 节点剩余内存不足导致HBase启动失败
    - 原因分析：
      - 查看RegionServer的日志发现报错信息：There is insufficient memory for the Java Runtime Environment to continue
      - 使用free指令查看，该节点没有足够内存
    - 解决方法：
      - 排查内存不足原因，确认是否有某些进程占用过多内存，或者由于服务器自身内存不足
  - Session control导致RegionServer一直Concerning
    - 原因分析：
      - 报错信息：org.apache.hadoop.hbase.DoNotRetryIOException: Overflow maximum number of peruser limit: 5
      - 查看Manager配置的Session Control，配置为5
    - 解决方法：
      - 关闭Session Control（hbase.sessioncontrol.enable设置为false）
  - RegionServer实例异常，处于Concerning状态
    - 原因分析：
      - 查看异常的RegionServer实例的运行日志，报错：ClockOutOfSyncException...，Reported time is too far out of sync with master
      - 说明异常的RegionServer实例和HMaster实例的时差大于允许的时差值30s（由参数hbase.regionserver.maxclockskew控制，默认30000ms），导致RegionServer实例异常
    - 解决方法：
      - 调整异常节点时间，确保节点间时差小于30s

      
     
