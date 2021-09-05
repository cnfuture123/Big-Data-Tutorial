# ElasticSearch相关概念

  - Elasticsearch是面向文档(document oriented)的，这意味着它可以存储整个对象或文档(document)。
  - 当文档被存储之后，它可以在1秒之内，近实时地被索引和搜索。
  - ES使用倒排索引的数据结构，支持快速的全文检索
  - ES默认会对每一个属性进行索引，并且每个被索引的属性都有一个专门的，优化的数据结构。例如：text属性是倒排结构，numeric属性是BKD树
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
  - Field(字段)
    - 相当于是数据表的字段。
    - key-value pairs
  - Mapping(映射)
    - Mapping是对处理数据的方式和规则方面做一些限制，如某个字段的数据类型、默认值、分析器、是否被索引等。
  - NRT(接近实时)
    - Elasticsearch是一个接近实时的搜索平台，从索引一个文档直到这个文档能够被搜索到有一个轻微的延迟（通常是1秒以内）。
  - Cluster(集群)
    - 一个集群就是由一个或多个节点组织在一起，它们共同持有整个的数据，并一起提供索引和搜索功能。
    - 一个集群由一个唯一的名字标识，这个名字默认就是“elasticsearch”。这个名字很重要，因为一个节点只能通过指定某个集群的名字，来加入这个集群。
    - ES会自动将数据和查询负载分布到可用的节点上
  - Node(节点)
    - 一个节点是集群中的一个服务器，作为集群的一部分，它存储数据，参与集群的索引和搜索功能。
    - 一个节点也是由一个名字来标识的，通过节点名字确定网络中的哪些服务器对应于ElasticSearch集群中的哪些节点。
    - 一个节点可以通过配置集群名称的方式来加入一个指定的集群。默认情况下，每个节点都会被安排加入到一个叫做“elasticsearch”的集群中。
  - Shards(分片)
    - Elasticsearch提供了将索引划分成多份的能力，这些份就叫做分片。
    - 当你创建一个索引的时候，可以指定分片的数量。
    - 分片的重要性：
      - 允许水平分割/扩展内容容量。
      - 允许在分片（位于多个节点上）之上进行分布式的、并行的操作，进而提高性能/吞吐量。
    - 一个索引实际上是一个或多个分片的逻辑分组，每个分片本身也是一个功能完善并且独立的“索引”。通过将文档分布到同一个索引的多个分片，将分片分布到多个节点上，ES可以保证一定的冗余，既可以在硬件失效时容错，又可以在新节点添加到集群时增加查询容量。当集群伸缩时，ES会自动迁移分片，使集群负载均衡。
    - 分片有2种类型：主分片和复制分片。每个文档属于一个主分片
    - 设置索引主分片的数量和分片大小时需要考虑考虑性能因素：分片越多，维护索引的开销越大；分片越大，移动分片做负载均衡时需要的时间越长。
      - 通常平均的分片大小在几GB和数十GB之间
      - 一个节点能容纳的分片数量正比于可用的堆内存大小，通常每GB堆内存的分片数量应该少于20
  - Replicas(复制)
    - 复制分片是主分片的一个拷贝。
    - 复制分片从不与原/主要（original/primary）分片置于同一节点上。
    - 主分片的数量在索引创建的时候指定，复制分片的数量可以在任意时间更改。
    - 默认情况下，ElasticSearch中的每个索引被分片5个主分片和1个复制分片。
    - 复制分片的重要性：
      - 在分片/节点失败的情况下，提供了高可用性。
      - 增加处理读请求的搜索量/吞吐量。
    
## 索引模块

  - 索引模块是每个索引创建的，并且控制这个索引所有设置
  
### 索引设置

  - 索引设置包含2种：
    - 静态的：在创建索引时设置
      - index.number_of_shards：每个索引的主分片数，默认是1
      - index.number_of_routing_shards：和index.number_of_shards一起使用，路由documents到主分片
    - 动态的：可以通过更新索引的API去修改
      - index.number_of_replicas：每个主分片的复制分片，默认是1
      - index.search.idle.after：分片不能被搜索或请求的搜索空闲时间，默认是30秒
      - index.refresh_interval：多久刷新一次，将最近对索引的修改对搜索和查询可见，默认是1秒，设置为-1会禁用刷新操作。
      - index.max_terms_count：Terms Query中terms数量的最大值，默认是65536
      - index.routing.allocation.enable：控制索引的分配分配
        ![image](https://user-images.githubusercontent.com/46510621/131835485-c0e9a23b-26f2-4af7-935f-9d08fbd78346.png)
      - index.routing.rebalance.enable: 启用分片的负载均衡
        ![image](https://user-images.githubusercontent.com/46510621/131835633-28e9d62c-2e56-4a5a-bf38-91dc577ed334.png)
      - index.routing.allocation.total_shards_per_node：一个节点能分配的最大数量的分片
 
### 慢查询日志

  - 搜索慢查询日志：
    - 分片级别的慢查询日志可以写入特定的日志文件，慢查询日志包含query and fetch语句
      ![image](https://user-images.githubusercontent.com/46510621/131873559-0a128d5a-b234-4d73-a8dc-d1f876945803.png)
    - 搜索慢查询日志可以在log4j2.properties文件中配置
  - 索引慢查询日志：
    - 功能上类似于搜索慢查询日志，日志名以_index_indexing_slowlog.log结尾
      ![image](https://user-images.githubusercontent.com/46510621/131875035-1d5587b1-1d50-4273-9d9c-deb1aa42f5d3.png)

### 历史数据留存

  - ES有时需要重新执行已经在分片上执行过的操作，ES使用叫做软删除（soft deletes）的特性保存最近索引上的删除操作，使得它们可以被重新执行
  - ES只会保存某些近期删除的文档，因为软删除的文档依然会占据空间，最终ES会完全丢弃这些软删除文档来清理空间
  - ES会使用分片历史保存租约（shard history retention leases）记录它期望将来需要重新执行的操作，每个需要重新执行操作的分片副本首先会创建一个自身的分片历史保存租约。每个保存租约会跟踪第一次操作的序列号，当分片副本接收到新的操作时，它会增加租约的序列号，表明未来不会重新执行这些操作。ES会丢弃那些不再被任何租约持有的操作
  - 历史留存配置：
    - index.soft_deletes.enabled：启用或禁用软删除
    - index.soft_deletes.retention_lease.period：分片历史保存租约最大有效期，默认有效期是12h

### 索引排序

  - ES在创建新索引时，可以配置index.sort.*指定每个分片的分段内的文档如何根据属性排序
    ![image](https://user-images.githubusercontent.com/46510621/132089854-22bb8a5f-38bb-451d-aa1e-693ff4a7b1f5.png)
  - 提前终止搜索请求：
    - 可以通过size属性指定文档的数量
      ![image](https://user-images.githubusercontent.com/46510621/132090094-21342d2f-0ba4-461c-8782-172243c1d9e3.png)
    - 可以通过track_total_hits属性控制是否处理除size外的剩余文档
      ![image](https://user-images.githubusercontent.com/46510621/132090153-0558cfa3-03cb-4cc3-adbd-513ae7756715.png)
    
## Mapping

  - Mapping是定义文档及其属性如何存储和索引的过程
  - 每个文档是一群属性的集合，每个属性有各自的数据类型。Mapping定义包含了这些属性的定义和元数据属性
  - 可以使用动态mapping或显示mapping定义数据
  - 映射限制：
    - 可以使用如下属性限制属性映射的数量（包含手动或动态创建的），防止文档出现映射爆炸
      - index.mapping.total_fields.limit：索引中属性数量的最大值，默认是1000
      - index.mapping.depth.limit：属性的最大深度，由内嵌对象的数量决定，默认是20
      - index.mapping.nested_fields.limit：索引中嵌套映射的最大数量，默认是50
      - index.mapping.nested_objects.limit：单个文档包含嵌套Json对象的最大数量，默认是10000
      - index.mapping.field_name_length.limit：属性名称的最大长度，默认Long.MAX_VALUE，即没有限制

### 动态Mapping

  - 动态映射是自定义的mapping，可以基于匹配条件动态地添加属性
  - 动态属性映射：
    - 当ES检测到文档中出现新的属性，它动态地将这个属性添加到映射中
    - 可以通过dynamic参数为true或runtime，指示ES如何基于接收的文档动态创建属性
      ![image](https://user-images.githubusercontent.com/46510621/132091033-14b6c71a-b562-45e1-8a26-633da74e8934.png)
    - 可以禁用document和object级别的动态映射，设置dynamic参数为false会忽略新的属性，设置为strict会在发现unknown属性时拒绝文档
  - 可以自定义日期和数字类型数据检测的动态属性映射的规则：
    - 日期检测：
      - 如果启用date_detection，string类型的属性会被检测它们的内容是否匹配dynamic_date_formats参数定义的日期类型。如果能匹配，会添加一个相应类型的日期属性
      - 设置date_detection为false可以禁用动态日期检测
      - 通过dynamic_date_formats参数设置被检测的日期格式
        ![image](https://user-images.githubusercontent.com/46510621/132091407-12a87e79-5598-46ce-b5b4-49516f9d6e96.png)
    - 数字检测：
      ![image](https://user-images.githubusercontent.com/46510621/132091674-ed52c201-9d8a-4d9a-9eaf-2c695e6569d2.png)
  - 动态模版：
    - 动态模版可以在默认的动态属性映射规则之外增加更多对映射数据方式的控制，可以通过模版自定义映射方式，基于匹配的条件动态添加属性

### 显示映射

  - 显示映射可以更加精准的控制映射的定义
  - 可以在创建索引或添加属性到已有索引时创建属性映射
    - 创建带显示映射的索引：
      ![image](https://user-images.githubusercontent.com/46510621/132091887-77054d70-200f-42e0-90c7-931bae048575.png)
    - 添加属性到已有索引:
      ![image](https://user-images.githubusercontent.com/46510621/132091901-6329b682-b830-4fda-b86c-d44a52ff5f04.png)
    - 查看索引的映射：
      ![image](https://user-images.githubusercontent.com/46510621/132091929-5fe6a2d5-3148-4284-869d-8fc4db64c036.png)
    - 查看指定属性的映射：
      ![image](https://user-images.githubusercontent.com/46510621/132091947-e7a0aecb-1ea2-4770-88f0-f06d66e25214.png)

## 文本分析

  - ES在索引或搜索text类型属性时进行文本分析，它会进行全文检索，搜索结果包含所有相关的结果，不只是完全匹配的结果
  - Tokenization:
    - 全文检索可以通过令牌化进行分析，将一个text划分为更小的块，成为tokens。通常这些token就是独立的单词
  - 标准化：
    - Tokenization启用对独立的项(term)进行匹配，但是每一项依然按照字面做匹配，例如：搜索Quick不会匹配到quick
    - 文本分析可以通过标准化将这些tokens转换为标准格式，因此可以匹配到相似格式，不是完全一致的内容
  - 文本分析通过分析器完成，它是一系列控制整个过程的规则，ES包含一个默认的分析器

### 简介

  - 分析器：
    - 分析器主要包括3部分：character filters, tokenizers, and token filters
    - 字符过滤器：将接收到的原始文本作为字符流处理，可以添加，删除或更新字符
    - Tokenizer：接收字符流，将它划分为tokens，并输出tokens流
    - Token过滤器：接收token流，可以添加，删除或更新token
  - 索引和搜索分析：
    - 文本分析发生在2个时间：
      - 索引时间：当文档被索引时，任意text类型属性值会被分析
      - 搜索时间：当对text属性进行全文检索时，查询语句会被分析
    - 分析过程：
      - 一个文档包含如下text属性：
        ![image](https://user-images.githubusercontent.com/46510621/132093661-1da9ef6e-134d-43b3-88c9-f2342dc66f5e.png)
      - 索引分析器将值转换为tokens，并标准化。如下图：每个token是一个值，每个token都被索引
        ![image](https://user-images.githubusercontent.com/46510621/132093720-e429a960-cf2d-45f9-82ab-9be69aa5a152.png)
      - 当搜索"Quick fox"时，查询语句会被分析器转换为[quick, fox]，因此搜索命中

## 搜索数据

  - query是一次请求ES数据信息
  - 一次search包含一个或多个query，匹配query的文档会在hits(搜索结果)中返回
  - Search API:
    ![image](https://user-images.githubusercontent.com/46510621/132093996-edfa83fd-55e2-4c4d-a178-b96e6c7c0c2b.png)
  - 通用搜索选项：
    - Query DSL: 
      - 支持很多查询类型，包括：
        - Boolean和其他复合查询：基于多个条件组合查询和匹配结果
        - Term-level查询：过滤和查询完全匹配的数据
        - 全文检索
        - 地理和空间查询
    - 聚合：
      - 获取搜索结果的统计数据和其他分析数据
    - 搜索多个数据流和指数：
      - 可以使用逗号分隔的值和grep形式在同一个请求中搜索多个数据流和指数
    - 分页搜索结果：
      - 默认搜索时返回前10个匹配的结果，可以使用from和size参数分页比较大的结果集
        - from参数指定忽略的命中数
        - size参数定义返回的最大命中数
    - 获取选择的属性：
      - 默认搜索结果中的每个hit包含文档的_source，可以通过一下2种方式从查询中获取选择的属性：
        - 使用fields选项取出索引映射中的属性值
        - 使用_source选项获取在索引时间传递的原始值
    - 排序搜索结果：
      - 默认搜索结果的命中文档通过_score（表示文档和查询语句的匹配程度）排序
  - 追踪所有的命中文档：
    - track_total_hits参数可以控制被追踪的总命中文档数
      - 当设置为true时，搜索响应会追踪匹配查询数量的命中文档
      - 当设置为整数时，指定命中文档的数量：
        ![image](https://user-images.githubusercontent.com/46510621/132099600-31a4da3b-52d3-4405-b34c-59727edf9844.png)
  - 过滤搜索结果
    - 有2种方式可以过滤搜索结果：
      - 使用带filter子句的布尔查询条件，搜索请求会将布尔过滤应用到搜索命中和聚合中
      - 使用搜索API的post_filter参数，搜索请求会将post过滤应用到搜索命中，不包括聚合

## Query DSL

  - Query DSL(Domain Specific Language)是基于Json定义查询，包含2种类型的子句：
    - 叶子查询子句：
      - 搜索特定属性的特定值，例如： match, term or range queries
    - 复合查询子句：
      - 复合查询子句包裹其他叶子或复合查询，用于合并多个查询条件
  - 查询和过滤语法：
    - 相关性分数：
      - ES根据相关性分数排序匹配的搜索结果
      - 相关性分数是一个浮点数，用_score表示，值越大表示文档是越相关的
    - 查询语法：
      - 如果将查询子句传递给搜索API中的query参数，则查询语法就生效了
    - 过滤语法：
      - 如果将查询子句传递给filter参数，例如：布尔查询中的filter or must_not参数，或filter聚合
  - 复合查询：
    - bool query：用于组合多个叶子或复合查询子句，例如：must, should, must_not, or filter子句
    - boosting query：返回匹配positive查询的文档，并降低匹配negative查询文档的相关性分数。可以使用boosting查询对某些文档降级，而不将它们从搜索结果中排除
    - constant_score query：包裹其他查询的查询，在filter上下文中执行。所有匹配的文档有固定的_score
    - dis_max query：接收多个查询的查询，返回匹配任意查询的文档。bool 查询结合了所有匹配查询的分数，而dis_max查询使用单个最佳匹配查询子句的分数
    - function_score query：使用函数修改返回文档的分数，函数考虑流行度，新近度，距离，自定义算法等
  - 全文检索：
    - intervals query：允许对匹配项的排序和接近度进行细粒度控制的全文查询
    - match query：全文检索的标准查询，返回匹配text, number, date or boolean value的文档
      ![image](https://user-images.githubusercontent.com/46510621/132116324-6a765c32-dab7-4dde-a0a3-0c5d405f2521.png)
    - match_bool_prefix query：创建一个bool查询：作为term查询匹配每一项，除了最后一项作为prefix查询匹配
    - match_phrase query：类似于 match 查询，但用于匹配精确的短语或单词接近匹配
    - match_phrase_prefix query：类似于 match_phrase 查询，但对最终单词进行通配符搜索
    - multi_match query：match查询的多属性版本
      ![image](https://user-images.githubusercontent.com/46510621/132116428-9047c1c4-5fc4-43c7-be74-a0320f701253.png)
    - query_string query：
      - 使用具有严格语法的解析器根据提供的查询字符串返回文档
      - 可以使用 query_string 查询来创建包含通配符、跨多个字段的搜索等的复杂搜索
      ![image](https://user-images.githubusercontent.com/46510621/132116546-4b24f738-fe3c-4011-a90a-bf89caedf8f9.png)
    - simple_query_string query：一个更简单、更健壮的 query_string 语法版本，适合直接向用户公开
  - 联合查询：
    - nested query：文档可能包含嵌套类型的属性，这些属性用于索引对象的数组，其中每个对象可以作为独立的文档索引
      ![image](https://user-images.githubusercontent.com/46510621/132117491-e5a7da30-9c90-4500-90eb-7bea8d87c3e0.png)
    - has_child and has_parent查询：
      - has_child query：返回子文档匹配查询的父文档
      - has_parent query：返回父文档匹配查询的子文档
  - Match All查询：
    - 匹配所有文档，_score为1.0
      ![image](https://user-images.githubusercontent.com/46510621/132117577-d54beaca-8b0c-41e6-bc95-42fd002a9ffb.png)
  - 专用的查询：
    - script query: 基于提供的脚本过滤文档
      ![image](https://user-images.githubusercontent.com/46510621/132117776-571083c9-f83d-40de-87d5-86f0fe0cac96.png)
    - wrapper query：接收其他查询作为base64编码的字符串
      ![image](https://user-images.githubusercontent.com/46510621/132117846-597579e1-0642-432b-8ca8-c5872d3501f3.png)
  - Term-level查询
    - term-level查询用于基于精确值去查询文档
    - 不同于全文检索，term-level查询不会分析搜索项，相反是属性的完全匹配
    - term-level查询类型：
      - exists query：返回包含字段索引值的文档
      - fuzzy query：返回包含与搜索项相似的文档，ES使用Levenshtein edit distance衡量相似性或模糊性
        ![image](https://user-images.githubusercontent.com/46510621/132118078-7de8089a-fbd3-48c3-b0b6-4336f2f577b8.png)
      - ids query：基于IDs返回文档，文档ID存在_id属性
      - range query：返回包含特定范围项的文档
        ![image](https://user-images.githubusercontent.com/46510621/132118252-9fc367d9-e201-47c3-a72d-c09b7b84c07a.png)
      - regexp query：返回包含匹配正则表达式项的文档
        ![image](https://user-images.githubusercontent.com/46510621/132118353-622b7cc1-c3be-4beb-97e9-379f7c31ba28.png)
      - term query：返回属性包含确定项的文档
        ![image](https://user-images.githubusercontent.com/46510621/132118402-978234ae-275f-4ebe-8251-af048a71e5d8.png)
      - terms query：返回属性包含一个或多个确定项的文档
        ![image](https://user-images.githubusercontent.com/46510621/132118442-961ec9f4-178b-4ccc-8191-c50849f80c60.png)
      - wildcard query：返回包含匹配通配符项的文档
      
        
      
