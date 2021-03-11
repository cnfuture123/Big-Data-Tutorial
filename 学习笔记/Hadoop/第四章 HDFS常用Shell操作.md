# HDFS常用Shell操作

  - 基本语法：
    - bin/hadoop fs <具体命令>：使用范围最广，可以指向任何的文件系统如local，HDFS等
    - bin/hdfs dfs <具体命令>：只HDFS文件系统相关（包括与Local FS间的操作），已经Deprecated
    - dfs是fs的实现类
    
  - 常用命令：
    - 启动Hadoop集群：
      - sbin/start-dfs.sh 
      - sbin/start-yarn.sh
    - 显示目录信息：hadoop fs -ls 
    - 在HDFS上创建目录：hadoop fs -mkdir temp
    - 从本地剪切粘贴到HDFS：hadoop fs -moveFromLocal cn.txt /temp
    - 追加一个文件到已经存在的文件末尾：hadoop fs -appendToFile cn2.txt /temp/cn.txt
    - 显示文件内容：hadoop fs -cat /temp/cn.txt
    - 修改文件所属权限：-chgrp, -chmod, -chown, 与Linux文件系统中的用法一样
    - 从本地文件系统中拷贝文件到HDFS路径：hadoop fs -copyFromLocal/put cn3.txt /temp
    - 从HDFS拷贝到本地：hadoop fs -copyToLocal/get /temp/cn3.txt ./
    - 从HDFS的一个路径拷贝到HDFS的另一个路径：hadoop fs -cp /temp/cn.txt /temp2
    - 在HDFS目录中移动文件：hadoop fs -mv /temp/cn3.txt /temp2
    - 合并下载多个文件：hadoop fs -getmerge /temp/* ./local.txt
    - 显示一个文件的末尾：hadoop fs -tail /temp/cn.txt
    - 删除文件或文件夹：hadoop fs -rm /temp/cn3.txt
    - 删除空目录：hadoop fs -rmdir /test
    - 查看磁盘空间：hadoop fs -du -h /temp
    - 清理磁盘空间：hadoop fs -rm -r -skipTrash /temp/*
    - 设置HDFS中文件的副本数量：hadoop fs -setrep 5 /temp/cn.txt
    - 检查数据文件：hdfs fsck /temp
    - 恢复文件租约：hdfs debug recoverLease -path <path-of-the-file> -retries <retry times>

    
