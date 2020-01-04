# DataNode

## DataNode工作机制

  - DataNode工作机制：
  
  ![DataNode工作机制](./图片/DataNode工作机制.PNG)
  
  - 注意细节：
    - 一个数据块在DataNode上以文件形式存储在磁盘上，包括两个文件，一个是数据本身，一个是元数据包括数据块的长度，块数据的校验和，以及时间戳。
    - DataNode启动后向NameNode注册，通过后，周期性（1小时）的向NameNode上报所有的块信息。
    - 心跳是每3秒一次，心跳返回结果带有NameNode给该DataNode的命令如复制块数据到另一台机器，或删除某个数据块。
    - 如果超过10分钟没有收到某个DataNode的心跳，则认为该节点不可用。
    - 集群运行中可以安全加入和退出一些机器。
    
## 数据完整性

  - DataNode节点保证数据完整性的方法：
    - 当DataNode读取Block的时候，它会计算CheckSum。
    - 如果计算后的CheckSum，与Block创建时值不一样，说明Block已经损坏。
    - Client读取其他DataNode上的Block。
    - DataNode在其文件创建后周期验证CheckSum。
  
