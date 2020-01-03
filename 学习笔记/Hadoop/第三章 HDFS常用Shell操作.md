# HDFS常用Shell操作

  - 基本语法：
    - bin/hadoop fs <具体命令>
    - bin/hdfs dfs <具体命令>
    - dfs是fs的实现类
    
  - 常用命令：
    - 启动Hadoop集群：
      - sbin/start-dfs.sh 
      - sbin/start-yarn.sh
    - 显示目录信息：bin/hadoop fs -ls 
    - 在HDFS上创建目录：bin/hadoop fs -mkdir temp
    - 从本地剪切粘贴到HDFS：bin/hadoop fs -moveFromLocal cn.txt /temp
    - 追加一个文件到已经存在的文件末尾：bin/hadoop fs -appendToFile cn2.txt /temp/cn.txt
    - 显示文件内容：bin/hadoop fs -cat /temp/cn.txt
    - 修改文件所属权限：-chgrp 、-chmod、-chown，与Linux文件系统中的用法一样
    - 从本地文件系统中拷贝文件到HDFS路径去：bin/hadoop fs -copyFromLocal/put cn3.txt /temp
    - 从HDFS拷贝到本地：bin/hadoop fs -copyToLocal/get /temp/cn3.txt ./
    - 从HDFS的一个路径拷贝到HDFS的另一个路径：bin/hadoop fs -cp /temp/cn.txt /temp2
    - 在HDFS目录中移动文件：bin/hadoop fs -mv /temp/cn3.txt /temp2
    - 从HDFS下载文件到本地：bin/hadoop fs -get /temp/cn3.txt ./
    - 合并下载多个文件：bin/hadoop fs -getmerge /temp/* ./local.txt
    - 显示一个文件的末尾：bin/hadoop fs -tail /temp/cn.txt
    - 删除文件或文件夹：bin/hadoop fs -rm /temp/cn3.txt
    - 删除空目录：bin/hadoop fs -rmdir /test
    - 统计文件夹的大小信息：bin/hadoop fs -du -h /temp
    - 设置HDFS中文件的副本数量：bin/hadoop fs -setrep 5 /temp/cn.txt
    
    
