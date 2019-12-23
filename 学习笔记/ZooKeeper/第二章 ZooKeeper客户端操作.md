# ZooKeeper客户端操作

## 命令行操作

  - 启动客户端: bin/zkCli.sh
  - 显示所有操作命令: help
  - 查看当前Znode中所包含的内容: ls /
  - 查看当前节点详细数据: ls2 /
  - 创建普通节点: create /path "content"
  - 获得节点的值: get /path
  - 创建短暂节点: create -e /temPath "temp content"
  - 创建带序号的节点: create -s /seqPath "seq content"
  - 修改节点数据值: set /path "new content"
  - 节点的值变化监听: get /path watch
  - 节点的子节点变化监听（路径变化）: ls /path watch
  - 删除节点: delete /path
  - 递归删除节点: rmr /path/childPath
  - 查看节点状态: stat /path
 
## API操作

  - ![Java Code](./代码/ZooKeeperClient.java)
