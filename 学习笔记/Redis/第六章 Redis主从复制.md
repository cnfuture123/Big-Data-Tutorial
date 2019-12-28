# Redis主从复制

  - 主从复制，就是主机数据更新后根据配置和策略，自动同步到备机的master/slaver机制，Master以写为主，Slave以读为主。
  
## 主从复制相关概念

  - 主从复制作用：
    - 读写分离
    - 容灾恢复
  - 常用命令：
    - info replication: 打印主从复制的相关信息。
    - slaveof <ip> <port> : 成为某个实例的从服务器。
  - 主从复制原理：
    - slave启动成功连接到master后会发送一个sync命令。
    - Master接到命令启动后台的存盘进程，同时收集所有接收到的用于修改数据集命令，在后台进程执行完毕之后，master将传送整个数据文件到slave,以完成一次完全同步。
    - 全量复制：而slave服务在接收到数据库文件数据后，将其存盘并加载到内存中。
    - 增量复制：Master继续将新的所有收集到的修改命令依次传给slave,完成同步。
    - 只要是重新连接master,一次完全同步（全量复制)将被自动执行。
  - 常用的配置模式：
    - 一主二仆：
      - 一个Master两个Slave
      
      ![一主二仆](./图片/一主二仆.PNG)
      
    - 薪火相传：
      - 上一个slave可以是下一个slave的Master，slave同样可以接收其他slaves的连接和同步请求。
      - 那么该slave作为了链条中下一个的master, 可以有效减轻master的写压力,去中心化降低风险。
      - 中途变更转向:会清除之前的数据，重新建立拷贝最新的。
      - 风险是一旦某个slave宕机，后面的slave都没法备份。
      
      ![薪火相传](./图片/薪火相传.PNG)
      
    - 反客为主：
      - 当一个master宕机后，后面的slave可以立刻升为master，其后面的slave不用做任何修改。
      - slaveof no one : 使当前数据库停止与其他数据库的同步，转成主数据库。
      
    - 哨兵模式：
      - 反客为主的自动版，能够后台监控主机是否故障，如果故障了根据投票数自动将从库转换为主库。
      - 故障恢复：
        - Sentinel之间进行选举，在剩余活着的机器里选举出一个leader，由选举出的leader进行failover（故障迁移）。
        - Sentinel leader选取slave节点中的一个slave作为新的Master节点。
        - Sentinel leader会在上一步选举的新master上执行slaveof no one操作，将其提升为master节点。
        - Sentinel leader向其它slave发送命令，让剩余的slave成为新的master节点的slave。
        - Sentinel leader会让原来的master降级为slave，当其恢复正常工作。
        - Sentinel leader会发送命令让其从新的master进行复制。上述的failover操作均由sentinel自己独自完成，完全无需人工干预。
      - 选取新master的方式：
        - 选择优先级靠前的，由slave-priority设置优先级。
        - 选择偏移量最大的，即已经复制数据量最大的从节点。
        - 选择runid最小的从服务，每个Redis实例启动后都会随机生成一个40位的runid。
      
      ![哨兵模式](./图片/哨兵模式.PNG)
      


      
