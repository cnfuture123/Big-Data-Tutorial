# Java客户端
  
## Jedis

  - 概述：
    - 使用时需要构建Jedis对象，一个Jedis对象代表一条和Redis服务进行连接的Socket通道
  - 需要的jar包：
    - jedis-3.1.0.jar
    - commons-pool-1.6.jar
  
  - Jedis代码示例：
    - 连接测试：
    
    ![连接测试](./代码/RedisConnection.java)
    
    - API使用：
    
    ![API使用](./代码/RedisAPI.java)
    
    - 交易示例：
    
    ![交易示例](./代码/RedisTransaction.java)
    
## JedisPool

  - JedisPoolConfig配置类：
    - 负责配置JedisPool参数，其中重要的参数包括：
      - maxTotal: 资源池中最大的连接数，默认值是8
        - 在并发量不大时，maxTotal设置过高会导致不必要的连接资源的浪费。可以根据实际总QPS和node节点数合理评估每个节点使用的最大连接数
      - maxIdel: 资源池允许最大空闲的连接数，默认值是8
        - maxIdel实际是业务可用的最大连接数，使得连接池达到最佳性能的设置是maxIdel = maxTotal
      - minIdel: 资源池确保最少空闲的连接数，默认值是0
  - JedisPool的创建和预热：
    - 创建JedisPool连接池的步骤：
      - 创建一个JedisPoolConfig配置实例
      - 以JedisPoolConfig实例、Redis IP、Redis Port和其他可选项为参数，构造一个JedisPool连接池实例
    - JedisPool的使用：
      - 调用pool.getResource()从连接池获取连接
      - JedisPool的池化连接连接在使用完后需要调用close()关闭连接，将其归还给连接池

## Spring的Redis缓存注解

  - Spring的三个缓存注解：
    - @CachePut: 设置缓存，先执行方法，然后将执行结果缓存起来
    - @CacheEvict: 删除缓存，在执行方法前删除缓存
    - @Cacheable: 查询缓存，首先检查注解中的key是否在缓存中，如果是则返回key的缓存值，不再执行方法；否则执行方法并将结果缓存起来

## SpEL

  - SpEL表达式可以在运行期间执行，其值可以动态装配到Spring Bean属性或构造函数中
  - JSP页面的表达式使用${}声明，SpEL表达式使用#{}声明
  
## RedisBloom

  - RedisBloom模块提供了4种数据结构：
    - Bloom filter: 用于判断（很大概率）是否某个元素是集合中的成员
      - Bloom filter在插入数据时有更好的性能和扩展性；Cuckoo filter进行检查操作时更快，并且支持删除
      - 命令行操作：
        - BF.RESERVE:
          - 格式：
            ```
            BF.RESERVE {key} {error_rate} {capacity}
            ```
          - 描述：
            - 以容量capacity，最大错误率error_rate，创建一个空的Bloom filter
              <img width="705" alt="image" src="https://user-images.githubusercontent.com/46510621/148055228-06487e20-04c2-4cca-b253-c6afb3c55fdf.png">
              
        - BF.ADD:
          - 格式：
            ```
            BF.ADD {key} {item}
            ```
          - 描述：
            - 添加元素到Bloom Filter，如果Filter不存在则创建
        - BF.MADD:
          - 格式：
            ```
            BF.MADD {key} {item ...}
            ```
          - 描述：
            - 添加一个或多个元素到Bloom Filter，如果Filter不存在则创建
        - BF.INSERT:
          - 格式：
            ```
            BF.INSERT {key} [CAPACITY {cap}] [ERROR {error}] [EXPANSION {expansion}] [NOCREATE][NONSCALING] ITEMS {item ...}
            ```
          - 描述：
            - BF.RESERVE and BF.ADD的组合，如果Filter不存在则创建
        - BF.EXISTS:
          - 格式：
            ```
            BF.EXISTS {key} {item}
            ```
          - 描述：
            - 判断元素是否存在于Filter中
        - BF.MEXISTS:
          - 格式：
            ```
            BF.MEXISTS {key} {item ...}
            ```
          - 描述：
            - 判断一个或多个元素是否存在于Filter中
        - BF.SCANDUMP:
          - 格式：
            ```
            BF.SCANDUMP {key} {iter}
            ```
          - 描述：
            - Bloom Filter的增量保存，适用于大数据量的Bloom Filter
        - BF.LOADCHUNK:
          - 格式：
            ```
            BF.LOADCHUNK {key} {iter} {data}
            ```
          - 描述：
            - 恢复之前通过SCANDUMP保存的Filter
        - BF.INFO:
          - 格式：
            ```
            BF.INFO {key}
            ```
          - 描述：
            - 返回关于key的信息
    - cuckoo filter: 类似Bloom Filter
    - count-min sketch：用于确定流中事件发生的频率
    - top-k：维护k个最常出现的项目
  - 参考：
    - https://oss.redis.com/redisbloom/

## JRedisBloom

  - 概述：
    - RedisBloom的Java客户端库，包含RedisBloom模块的API，实现了高性能的Bloom Filter
    - 导入依赖：
      ```
        <dependencies>
          <dependency>
            <groupId>com.redislabs</groupId>
            <artifactId>jrebloom</artifactId>
            <version>2.1.0</version>
          </dependency>
        </dependencies>
      ```
  - 使用示例：
    - 初始化客户端：
      ```
      //单个Redis实例
      import io.rebloom.client.Client
      Client client = new Client("localhost", 6379);
      
      //Redis集群
      import io.rebloom.client.ClusterClient;
      ClusterClient redisClient = new ClusterClient(new HostAndPort("localhost", 6379), jedisPool);
      ```
    - 添加元素：
      ```
      client.add("simpleBloom", "Mark");
      ```
    - 判断元素是否存在：
      ```
      client.exists("simpleBloom", "Mark");
      ```
    - 添加多个元素：
      ```
      client.addMulti("simpleBloom", "foo", "bar", "baz", "bat", "bag");
      ```
    - 判断多个元素是否存在：
      ```
      boolean[] rv = client.existsMulti("simpleBloom", "foo", "bar", "baz", "bat", "mark", "nonexist");
      ```
    - 创建一个Bloom Filter：
      ```
      client.createFilter("specialBloom", 10000, 0.0001);
      ```
    - 使用集群客户端的方式与使用单个实例客户端的方式相同，实际上都是调用Redis的相关操作
  - 参考：
    - https://github.com/RedisBloom/JRedisBloom
    

