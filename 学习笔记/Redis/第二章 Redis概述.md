# Redis概述

  - Redis是一个开源的key-value存储系统，基于内存运行，分布式，并支持持久化的NoSQL数据库。
  - C语言编写，遵守BSD协议。
  - Redis通常被称为数据结构服务器，因为值（value）可以是字符串(String)、哈希(Hash)、列表(list)、集合(sets)和有序集合(sorted sets) 、位数组(Bit Arrays)、Streams等类型。
  
## Redis特点

  - Redis支持数据的持久化，可以将内存中的数据保持在磁盘中，重启的时候可以再次加载进行使用。
  - Redis不仅仅支持简单的key-value类型的数据，同时还提供list，set，zset，hash等数据结构的存储。
  - Redis支持数据的备份，即master-slave模式的数据备份。
  
## Redis适用场景

  - 内存存储和持久化：Redis支持异步将内存中的数据写到硬盘上，同时不影响继续服务。
  - 会话缓存（Session Cache）。
  - 队列：Reids提供list和set操作，这使得Redis能作为一个很好的消息队列平台来使用。我们常通过Reids的队列功能做购买限制。对用户购买行为进行限制，限制今天只能购买几次商品。
  - 排名点赞：Redis在内存中对数字进行递增或递减的操作实现得非常好。在很多排名的场景中会应用Redis来进行，比如小说网站对小说进行排名，根据排名，将排名靠前的小说推荐给用户。
  
## 基础知识

  - 单进程。
  - 默认16个数据库，类似数组下表从零开始，初始默认使用0号库。
    - 可以通过调整Redis的配置文件redis/redis.conf中的databases来修改这个值，设置完毕后重启Redis便完成配置。
  - select命令切换数据库。
  - dbsize查看当前数据库的key的数量。
  - flushdb：清空当前库。
  - flushall；通杀全部库。
  - 统一密码管理，16个库都是同样密码，要么都OK要么一个也连接不上。
  - Redis索引都是从零开始。
  - 默认端口是6379。
  
