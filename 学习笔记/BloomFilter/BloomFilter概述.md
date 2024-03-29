# BloomFilter概述

## 原理

  - 布隆过滤器是节省空间，概率性的数据结构。
  - 它的原理是：当一个元素被加入集合时，通过K个散列函数将这个元素映射成一个位数组中的K个点，把它们置为1。检索时，如果这些点有任何一个0，则被检元素一定不在；如果都是1，则被检元素很可能在。
  - 优点和缺点：
    - 优点：
      - 空间和时间的优势，布隆过滤器存储空间和插入/查询时间都是常数O(k)。
    - 缺点：
      - 随着存入的元素数量增加，误判率随之增加。
      
## 哈希函数

  - 布隆滤波器使用的哈希函数需要是独立，均匀分布的，否则哈希冲突会很常见，导致较高的误判率。
  - 哈希速度要快，像sha1这样的加密哈希不适用，murmur, fnv, HashMix更合适一些。
  
## Guava Bloom Filter

  - 创建Bloom Filter:
    ```
    static <T> BloomFilter<T> create(
      Funnel<? super T> funnel, long expectedInsertions, double fpp, Strategy strategy) {
    checkNotNull(funnel);
    checkArgument(expectedInsertions >= 0, "Expected insertions (%s) must be >= 0", expectedInsertions);
    checkArgument(fpp > 0.0, "False positive probability (%s) must be > 0.0", fpp);
    checkArgument(fpp < 1.0, "False positive probability (%s) must be < 1.0", fpp);
    checkNotNull(strategy);
    ```
    ```
    示例：
    BloomFilter.create(Funnels.stringFunnel(Charset.defaultCharset()), this.filterSize, this.fpp);
    ```
    - 参数说明：
      - funnel: 输入的数据
      - expectedInsertions：预计插入的元素总数
      - fpp: 期望误判率
      - strategy: 实现Strategy的实例
  - 计算bit数组的长度以及哈希函数的个数:
    ```
    // n为expectedInsertions，p为fpp
    static long optimalNumOfBits(long n, double p) {
      if (p == 0) {
        p = Double.MIN_VALUE;
      }
      return (long) (-n * Math.log(p) / (Math.log(2) * Math.log(2)));
    }
    // n为expectedInsertions，m为numBits
    static int optimalNumOfHashFunctions(long n, long m) {
      return Math.max(1, (int) Math.round((double) m / n * Math.log(2)));
    }
    ```
    - 根据expectedInsertions和fpp计算bit数组的长度
    - 根据expectedInsertions和bit数组的长度计算哈希函数的个数
  - 核心方法：
    ```
    // 添加元素
    public boolean put(T object) {
      return this.strategy.put(object, this.funnel, this.numHashFunctions, this.bits)
    }
    // 判断元素是否存在
    public boolean mightContain(T object) {
      return this.strategy.mightContain(object, this.funnel, this.numHashFunctions, this.bits)
    }
    ```
    
## Redis Bloom Filter

  - RedisBloom模块提供布隆滤波器，这种数据结构以牺牲精确率换取内存使用的效率，适用于大数据和流式应用。
  - 布隆滤波器的扩展性：
    - 当单个布隆滤波器达到容量阈值时，则创建一个新的布隆滤波器。通常新的布隆滤波器容量大于旧的，减小创建新布隆滤波器的概率。
  - Java使用JReBloom库实现Redis Bloom Filter
    - ClusterClient用来连接Redis集群，常用的方法如下：
      ```
      // 创建Bloom Filter
      public boolean createFilter(final String name, final long initCapacity, final double errorRate){};
      // 添加元素
      public boolean add(String name, String value){};
      public boolean add(String name, final byte[] value){};
      // 批量添加元素
      public boolean[] addMulti(String name, String... values){};
      // 查询
      public boolean exists(String name, String value){};
      // 批量查询
      public boolean[] existsMulti(final String name, final String... values){};
      ```

## 参考
  
  - https://llimllib.github.io/bloomfilter-tutorial/
  - https://www.geeksforgeeks.org/bloom-filters-introduction-and-python-implementation/
  - https://segmentfault.com/a/1190000012620152
  - https://oss.redislabs.com/redisbloom/#references
