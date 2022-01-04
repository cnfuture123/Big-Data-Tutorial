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
    - Bloom filter，cuckoo filter: 用于判断（很大概率）是否某个元素是集合中的成员
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
            
    - count-min sketch：用于确定流中事件发生的频率
    - top-k：维护k个最常出现的项目
  - 参考：
    - https://oss.redis.com/redisbloom/
