# HDFS概述

  - HDFS(Hadoop Distributed File System)是一个文件系统，用于存储文件，通过目录树来定位文件；其次它是分布式的，由很多服务器联合实现其功能，集群中的服务器有各自的角色。
  - HDFS使用场景：适合一次写入，多次读出的场景，且不支持文件的修改。
  
## HDFS优缺点

  - HDFS优点：
  
  ![HDFS优点](./图片/HDFS优点.PNG)
  
  - HDFS缺点：
  
  ![HDFS缺点](./图片/HDFS缺点.PNG)
  
## HDFS架构

  - HDFS架构：
  
  ![HDFS架构1](./图片/HDFS架构1.PNG)
  
  ![HDFS架构2](./图片/HDFS架构2.PNG)
  
## HDFS文件块大小

  - HDFS文件块大小：
  
  ![HDFS文件块大小](./图片/HDFS文件块大小.PNG)
  
  - 注意细节：
    - HDFS文件块设置太小，会增加寻址时间。
    - HDFS文件块设置太大，传输数据的时间会增加。
    - HDFS文件块设置主要取决于磁盘传输速率。
    
    
  
  
  
