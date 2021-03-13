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

  - 检测损坏数据的常用方式是计算数据的校验和。如果新产生的校验和与初始的不一致，则该数据被损坏了。这个技术只是错误检测，不能对数据进行修复。
  - HDFS的数据完整性：
    - Datanodes在接收到数据时校验数据以及它的校验和，可能发生在从客户端接收数据或者从其他节点复制数据。如果Datanode检测到错误，客户端会收到IOException。
    - 每个Datanode保存checksum校验的日志，因此它知道上一次数据块是什么时候校验的。
    - 除了在客户端读数据时校验，每个Datanode后台运行DataBlockScanner周期检验Datanode上所有的数据块。
   
 ## 压缩
 
  - 文件压缩有两个好处：
    - 减少存储文件需要的空间
    - 加速数据在网络中或磁盘间的传输
  - 文件压缩格式：
  
    ![image](https://user-images.githubusercontent.com/46510621/110920527-764c3a80-8358-11eb-93e5-772b02254aa7.png)
    
    - 所有的压缩算法面临空间/时间的折中：更快地压缩和解压速率通常占用的存储空间更大。
    - Splittable表示是否可以查询到数据流的任意位置，并开始读取数据。
  
## Codecs

  - Codec是压缩解压算法的实现。Hadoop codec表示为CompressionCodec接口的实现。
  
    ![image](https://user-images.githubusercontent.com/46510621/111014279-f8327700-83dd-11eb-9650-0bdec814f022.png)

## 序列化

  - 序列化是将结构化的对象转化为字节流的过程，以便在网络传输或者持久化数据时使用。反序列化是相反的过程：将字节流转化为结构化对象。
  - 序列化主要用用于两个场景：进程间通信和持久化数据。Hadoop使用远程程序调用（RPC）实现进程间通信。
  - Hadoop使用自身的序列化格式：Writables，它是完整的，快速的，但是不容易扩展，且对于Java之外的语言不容易使用。
  
    ![image](https://user-images.githubusercontent.com/46510621/111014356-447db700-83de-11eb-8b46-7a787c561856.png)

  - Writable类：

    ![image](https://user-images.githubusercontent.com/46510621/111014372-55c6c380-83de-11eb-80ba-f0acdca1de2e.png)

    - Text is a Writable for UTF-8 sequences。它可以看做java.lang.String。

## SequenceFile

  - Hadoop SequenceFile为键值对提供了持久化的数据结构。它也可以用于小文件的容器，可以将小文件打包成SequenceFile使得存储和计算更加高效。
  - SequenceFile格式：
    - 一个SequenceFile包含一个header，跟着一条或多条记录。Header包含：键值类的名称，压缩信息，用户定义的元数据以及同步标记。同步标记允许reader同步记录的界限，并从任意位置读取数据。
    - Sequence内部结构：

      ![image](https://user-images.githubusercontent.com/46510621/111014535-09c84e80-83df-11eb-8c71-b35cfd1cf15d.png)

    - Block Compression同时压缩多条记录，它比单条记录的压缩完整性更好，因为它可以利用记录间的相似性。

      ![image](https://user-images.githubusercontent.com/46510621/111014549-1cdb1e80-83df-11eb-8557-7d57fb618b9e.png)


