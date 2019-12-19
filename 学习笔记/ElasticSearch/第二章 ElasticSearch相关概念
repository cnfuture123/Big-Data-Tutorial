# ElasticSearch相关概念

  - Elasticsearch是面向文档(document oriented)的，这意味着它可以存储整个对象或文档(document)。
  - 它不仅仅是存储，还会索引(index)每个文档的内容使之可以被搜索。
  - 在Elasticsearch中，你可以对文档（而非成行成列的数据）进行索引、搜索、排序、过滤。
  - ES与传统数据库的关系：
    ``` 
      Relational DB ‐> Databases ‐> Tables ‐> Rows      ‐> Columns
      ElasticSearch ‐> Index     ‐> Types  ‐> Documents ‐> Fields
    ```
    
## ElasticSearch核心概念

  - Index(索引)
    - 一个索引就是一个拥有相似特征的文档的集合。
    - 一个索引由一个名字来标识（必须全部是小写字母的），并且当我们要对对应于这个索引中的文档进行索引、搜索、更新和删除的时候，都要使用到这个名字。
    - 在一个集群中，可以定义任意多的索引。
  - Type(类型)
    - 在一个索引中，你可以定义一种或多种类型。
    - 一个类型是索引的一个逻辑上的分类/分区。
    - 通常，会为具有一组共同字段的文档定义一个类型。
  - Document(文档)
    - 一个文档是一个可被索引的基础信息单元。
    - 文档以JSON格式来表示。
    - 在一个index/type里面，你可以存储任意多的文档。
  
