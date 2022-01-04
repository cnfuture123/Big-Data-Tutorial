# Redis数据类型

## Redis的数据类型

  - Redis keys:
    - 可以使用任意的二进制序列作为key，从"foo"的字符串到JPEG文件都是支持的，空字符串也是有效的
    - 关于keys的一些规则：
      - key的长度不要过长，因为会占用较多的内存，并且key的查找比较耗时
      - key的长度不要过短，会造成可读性降低。例如："u1000flw"可以被写成"user:1000:followers"，牺牲较小的内存空间可以提升可读性，需要在两者之间寻找平衡
      - 坚持一种符合业务逻辑的key设计，例如：userId:itemId:action
      - 允许的最大key大小为512MB
  - String: 
    - String是Redis最基本的类型，一个key对应一个value。
    - String类型是二进制安全的。意味着Redis的string可以包含任何数据。比如jpg图片或者序列化的对象。
    - 一个Redis中字符串value最多可以是512M。
  - List:
    - 单键多值。
    - Redis列表是简单的字符串列表，按照插入顺序排序。
    - 可以添加一个元素到列表的头部（左边）或者尾部（右边）。
    - 底层实现是双向链表，对两端的操作性能很高，可以以固定时间插入元素到头或尾，
    - 通过索引下标中间的节点性能会较差
    - 常用的用例：
      - 记录用户发布到社交网上的最新的更新内容
        - 例如：每次用户发布新照片时，我们都会使用LPUSH将其ID添加到列表中。当用户访问主页时，我们使用LRANGE 0 9得到最新的10张照片
      - 进程间通信，使用消费者-生产者模式：生产者将物品推送到列表中，消费者消费这些物品，并执行某些行为
  - Set:
    - 单键多值。
    - Set是string类型的无序集合。
    - 底层其实是一个value为null的hash表。
    - 添加，删除，查找的复杂度都是O(1)。
    - 可以自动排重,没有重复元素。
    - 提供了判断某个成员是否在一个set集合内的重要接口，这个也是list所不能提供的。
  - Hash:
    - 是一个键值对集合。KV模式不变，但V是一个键值对；是一个string类型的field和value的映射表，适合用于存储对象。
    - Redis的Hash实际是内部存储的Value为一个HashMap，并提供了直接存取这个Map成员的接口
  - Zset(sorted set):
    - zset与普通集合set非常相似，是一个没有重复元素的string集合
    - 不同之处是sorted set的所有成员都关联了一个浮点数值的评分（score），这个评分（score）被用来按照从最低分到最高分的方式排序集合中的成员
      - 排序规则：
        - 如果A.score > B.score，则A > B
        - 如果A.score = B.score，则按字典顺序排序
    - 集合的成员是唯一的，但是评分可以是重复
    - 底层实现是包含跳表和哈希表
    - 常用的用例：
      - 排行榜：sorted set中元素的分数可以在任意时间通过zadd命令更新，并且时间复杂度是O(log(N))
  - Bitmap:
    - Bitmap不是实际的数据类型，而是对于string类型定义的一组位操作
    - 位操作分为2组：
      - 恒定时间的单个位操作
      - 一组位操作
    - Bitmap最大的优势是存储信息时节省大量空间
      
## 常用操作命令

  - Key:
    - keys * : 查询当前库的所有键
    - exists key : 判断某个键是否存在
    - type key : 查看值的类型
    - del key : 删除某个键
    - expire key seconds : 为键值设置过期时间，单位秒
    - ttl key : 查看key还有多少秒过期，-1表示永不过期，-2表示已过期
    - PEXPIRE key milliseconds : 为键值设置过期时间，单位毫秒
    - PTTL key : 查看key还有多少毫秒过期
    - dbsize : 查看当前数据库的key的数量
    - flushdb : 清空当前库
    - flushall : 通杀全部库
  
  - String:
    - set key value : 添加键值对
    - get key : 查询对应键值
    - append key value : 将给定的value追加到原值的末尾
    - strlen key : 获得值的长度
    - setnx key value : 只有在key不存在时设置key的值
    - incr key : 将key中储存的数字值增1, 只能对数字值操作，如果为空，新增值为1
      - incr是原子性操作，执行read-increment-set操作，不会发生多个客户端同一时间执行命令
    - decr key : 将key中储存的数字值减1, 只能对数字值操作，如果为空，新增值为-1
    - incrby/decrby key 步长 : 将key中储存的数字值按步长增减
    - mset key1 value1 key2 value2 ... : 同时设置一个或多个key-value对
    - mget key1 key2 ... : 同时获取一个或多个value，返回的是值的数组
    - msetnx key1 value1 key2 value2 ... : 同时设置一个或多个key-value对，当且仅当所有给定key都不存在。
    - setrange key 起始位置 value : 用value覆写key所储存的字符串值，从起始位置开始
    - getrange key 起始位置 结束位置 : 获得值的范围，类似java中的substring
    - setex key 过期时间 value : 设置键值的同时，设置过期时间，单位秒
    - getset key value : 以新换旧，设置了新值同时获得旧值。
      
  - List:
    - lpush/rpush key value1 value2 value3 ... : 从左边/右边插入一个或多个值
    - lpop/rpop key : 从左边/右边吐出一个值，并会从列表中删除这个值
    - blpop/brpop key : 类似lpop/rpop，但如果列表为空则会阻塞，返回调用者，直到新元素加入列表才会继续处理
    - rpoplpush key1 key2 : 从key1列表右边吐出一个值，插到key2列表左边
    - lrange key start stop : 按照索引范围获得元素(从左到右)
    - lindex key index : 按照索引下标获得元素(从左到右)
    - llen key : 获得列表长度
    - linsert key before/after value new_value : 在value的前面或者后面插入new_value
    - lrem key n value : 从左边删除n个value(从左到右)
    - ltrim key start stop : 截取指定范围的值后再赋值给key
  
  - Set:
    - sadd key value1 value2 ... : 将一个或多个member元素加入到集合key当中，已经存在于集合的member元素将被忽略
    - smembers key : 取出该集合的所有值
    - sismember key value : 判断集合key是否含有该value值，有返回1，没有返回0
    - scard key : 返回该集合的元素个数
    - srem key value1 value2 ... : 删除集合中的某个元素
    - spop key : 随机从该集合中吐出一个值
    - srandmember key n : 随机从该集合中取出n个值, 不会从集合中删除
    - sinter key1 key2 : 返回两个集合的交集元素
    - sunion key1 key2 : 返回两个集合的并集元素
    - sdiff key1 key2 : 返回两个集合的差集元素
  
  - Hash:
    - hset key field value : 给key集合中的field赋值value
    - hget key field : 从key集合field取出value
    - hmset key field1 value1 field2 value2 ... : 批量设置hash的键值
    - hexists key field : 查看哈希表key中，field是否存在
    - hkeys key : 列出该hash集合的所有field
    - hvals key : 列出该hash集合的所有value
    - hincrby key field increment : 为哈希表key中的field值加上增量increment
    - hsetnx key field value : 将哈希表key中的field值设置为value ，当且仅当field不存在
    
  - Zset:
    - zadd key score1 value1 score2 value2 ... : 将一个或多个member元素及其score值加入到有序集key当中
    - zrange key start stop [withscores] : 返回有序集key中，下标在start stop之间的元素, 带withscores，分数一起和值返回到结果集
    - zrevrange key start stop [withscores] : 倒序排序的结果
    - zrangebyscore key min max [withscores] : 返回有序集key中，所有score值介于min和max之间(包括等于min或max)的成员。有序集成员按score 值递增(从小到大)次序排列。
    - zrevrangebyscore key min max [withscores] : 同上，改为从大到小排列。
    - zincrby key increment value : 为元素的score加上增量
    - zrem key value 删除该集合下，指定值的元素
    - zremrangebyscore key min max : 删除该集合分数在min和max之间的元素
    - zcount key min max : 统计该集合，分数区间内的元素个数
    - zcard key: 统计该集合中key包含的元素个数
    - zrank key value : 返回该值在集合中的排名，从0开始
    - zrevrank key value : 返回该值在集合中的逆序排名
    
  - Bitmap:
    - setbit key offset bit : 在key的offset位置设置值为0或1。offset在0到2的32次幂之间
    - getbit key offset : 获取key的offset位置上的值
    - bitcount key [start end] : 返回bits设置为1的数量
    - bitpos key bit [start [end]] : 返回第一个设置为0或1的bit位置
    - bitop operation destkey key [key ...] : 对多个key进行按位操作，结果存在destkey中
      - 支持AND, OR, XOR and NOT按位操作
        ![image](https://user-images.githubusercontent.com/46510621/133881120-1e792315-f34e-499f-b26e-c831a91830b7.png)

  
