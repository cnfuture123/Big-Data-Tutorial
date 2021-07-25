# BloomFilter概述

## 原理

  - 布隆过滤器是节省空间，概率性的数据结构。
  - 它的原理是：当一个元素被加入集合时，通过K个散列函数将这个元素映射成一个位数组中的K个点，把它们置为1。检索时，如果这些点有任何一个0，则被检元素一定不在；如果都是1，则被检元素很可能在。
  - 优点和缺点：
    - 优点：
      - 空间和时间的优势，布隆过滤器存储空间和插入/查询时间都是常数O(k)。
    - 缺点：
      - 随着存入的元素数量增加，误算率随之增加。
      
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
    - 



## 参考
  
  - https://llimllib.github.io/bloomfilter-tutorial/
  - https://www.geeksforgeeks.org/bloom-filters-introduction-and-python-implementation/
