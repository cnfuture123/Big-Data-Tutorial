# Java客户端
  
## Jedis

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
    - RedisBloom的Java客户端库，包含RedisBloom Redis模块的API，实现了高性能的Bloom Filter
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
    

