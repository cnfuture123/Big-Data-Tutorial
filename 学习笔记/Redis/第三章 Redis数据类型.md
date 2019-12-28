# Redis数据类型

## Redis的五大数据类型

  - String: 
    - String是Redis最基本的类型，一个key对应一个value。
    - String类型是二进制安全的。意味着Redis的string可以包含任何数据。比如jpg图片或者序列化的对象。
    - 一个Redis中字符串value最多可以是512M。
  - List:
    - 单键多值。
    - Redis列表是简单的字符串列表，按照插入顺序排序。
    - 可以添加一个元素导列表的头部（左边）或者尾部（右边）。
    - 底层实际是个双向链表，对两端的操作性能很高，通过索引下标的操作中间的节点性能会较差。
  - Set:
    - 单键多值。
    - Set是string类型的无序集合。
    - 底层其实是一个value为null的hash表。
    - 添加，删除，查找的复杂度都是O(1)。
    - 可以自动排重,没有重复元素。
    - 提供了判断某个成员是否在一个set集合内的重要接口，这个也是list所不能提供的。
  - Hash:
    - 是一个键值对集合。KV模式不变，但V是一个键值对。这个评分（score）被用来按照从最低分到最高分的方式排序集合中的成员。
    - 是一个string类型的field和value的映射表，适合用于存储对象。
    - 类似Java里面的Map<String,Object>。
  - Zset(sorted set):
    - zset与普通集合set非常相似，是一个没有重复元素的string集合。
    - 不同之处是有序集合的所有成员都关联了一个评分（score），这个评分（score）被用来按照从最低分到最高分的方式排序集合中的成员。
    - 集合的成员是唯一的，但是评分可以是重复。

## 常用操作命令

  - Key:
    - keys * : 查询当前库的所有键
    - exists <key> : 判断某个键是否存在
    - type <key> : 查看键的类型
    - del <key> : 删除某个键
    - expire <key> <seconds> : 为键值设置过期时间，单位秒
    - ttl <key> : 查看key还有多少秒过期，-1表示永不过期，-2表示已过期
    - dbsize : 查看当前数据库的key的数量
    - flushdb : 清空当前库
    - flushall : 通杀全部库
  - String:
    - set <key> <value> : 添加键值对
    - get <key> : 查询对应键值
    - append <key> <value> : 将给定的<value>追加到原值的末尾
    - strlen <key> : 获得值的长度
    - setnx <key> <value> : 只有在key不存在时设置key的值
    - incr <key> : 将key中储存的数字值增1, 只能对数字值操作，如果为空，新增值为1
    - decr <key> : 将key中储存的数字值减1, 只能对数字值操作，如果为空，新增值为-1
    - incrby/decrby <key> <步长> : 将key中储存的数字值按步长增减
    - mset <key1> <value1> <key2> <value2> ... : 同时设置一个或多个key-value对
    - mget <key1> <key2> ... : 同时获取一个或多个value
    - msetnx <key1> <value1> <key2> <value2> ... : 同时设置一个或多个key-value对，当且仅当所有给定key都不存在。
    - setrange <key> <起始位置> <value> : 用<value>覆写<key>所储存的字符串值，从<起始位置>开始
    - getrange <key> <起始位置> <结束位置> : 获得值的范围，类似java中的substring
    - setex <key> <过期时间> <value> : 设置键值的同时，设置过期时间，单位秒
    - getset <key> <value> : 以新换旧，设置了新值同时获得旧值。
  - List:
    - lpush/rpush <key> <value1> <value2> <value3> ... : 从左边/右边插入一个或多个值
    - lpop/rpop <key> : 从左边/右边吐出一个值
    - rpoplpush <key1> <key2> : 从<key1>列表右边吐出一个值，插到<key2>列表左边
    - lrange <key> <start> <stop> : 按照索引范围获得元素(从左到右)
    - lindex <key> <index> : 按照索引下标获得元素(从左到右)
    - llen <key> : 获得列表长度
    - linsert <key> before/after <value> <new value> : 在<value>的前面或者后面插入<newvalue>
    - lrem <key> <n> <value> : 从左边删除n个value(从左到右)
    - ltrim <key> <start> <stop> : 截取指定范围的值后再赋值给key
  - Set:
    - sadd <key> <value1> <value2> ... : 将一个或多个member元素加入到集合key当中，已经存在于集合的member元素将被忽略
    - smembers <key> : 取出该集合的所有值
    - sismember <key> <value> : 判断集合<key>是否含有该<value>值，有返回1，没有返回0
    - scard <key> : 返回该集合的元素个数
    - srem <key> <value1> <value2> ... : 删除集合中的某个元素
    - spop <key> : 随机从该集合中吐出一个值
    - srandmember <key> <n> : 随机从该集合中取出n个值, 不会从集合中删除
    - sinter <key1> <key2> : 返回两个集合的交集元素
    - sunion <key1> <key2> : 返回两个集合的并集元素
    - sdiff <key1> <key2> : 返回两个集合的差集元素
  - Hash:
    - hset <key> <field> <value> : 给<key>集合中的<field>键赋值<value>
    - hget <key> <field> : 从<key>集合<field>取出value
    - hmset <key> <field1> <value1> <field2> <value2> ... : 批量设置hash的值
    - hexists <key> <field> : 查看哈希表key中，给定域field是否存在
    - hkeys <key> : 列出该hash集合的所有field
    - hvals <key> : 列出该hash集合的所有value
    - hincrby <key> <field> <increment> : 为哈希表key中的域field的值加上增量increment
    - hsetnx <key> <field> <value> : 将哈希表key中的域field的值设置为value ，当且仅当域field不存在
  - Zset:
    - zadd <key> <score1> <value1> <score2> <value2> ... : 将一个或多个member元素及其score值加入到有序集key当中
    - zrange <key> <start> <stop> [withscores] : 返回有序集key中，下标在<start> <stop>之间的元素, 带withscores，分数一起和值返回到结果集
    - 


  
  
