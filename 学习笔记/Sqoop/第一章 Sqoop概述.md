# Sqoop概述

## Sqoop简介

  - Sqoop是一款开源的工具，主要用于在Hadoop(Hive)与传统的数据库(mysql、postgresql...)间进行数据的传递。
  - 可以将一个关系型数据库中的数据导进到Hadoop的HDFS中，也可以将HDFS的数据导进到关系型数据库中。
  - Sqoop2的最新版本是1.99.7。请注意，2与1不兼容，且特征不完整，它并不打算用于生产部署。
  
## Sqoop原理

  - 将导入或导出命令翻译成MapReduce程序来实现。
  - 在翻译出的MapReduce中主要是对inputformat和outputformat进行定制。
