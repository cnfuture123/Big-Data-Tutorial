## XML映射文件

  - MyBatis的真正强大在于它的语句映射，这是它的魔力所在。映射器的XML文件相对简单,如果拿它跟具有相同功能的 JDBC 代码进行对比，你会立即发现省掉了将近 95% 的代码。
    
    ![image](https://user-images.githubusercontent.com/46510621/111897762-b4cead00-8a5c-11eb-9959-1bbd939fa3b3.png)

  - select元素：
    - 示例：

      ![image](https://user-images.githubusercontent.com/46510621/111897900-9917d680-8a5d-11eb-9dd1-a8a5a0e66d1e.png)

    - select元素的部分属性：

      ![image](https://user-images.githubusercontent.com/46510621/111898021-4a1e7100-8a5e-11eb-85c9-6fe5c2caff2c.png)

  - insert, update和delete元素：
    - 示例：

      ![image](https://user-images.githubusercontent.com/46510621/111898283-00cf2100-8a60-11eb-80c9-e08bbcb270f7.png)

    - 元素属性：

      ![image](https://user-images.githubusercontent.com/46510621/111898181-7686bd00-8a5f-11eb-92e0-c502fd3039e7.png)

  - 生成主键：
    - 如果你的数据库支持自动生成主键的字段（比如MySQL和SQL Server），那么你可以设置useGeneratedKeys=”true”，然后再把keyProperty设置为目标属性
    
      ![image](https://user-images.githubusercontent.com/46510621/111898425-f7928400-8a60-11eb-893e-b7461fa7fb7f.png)

  - sql元素：
    - 用来定义可重用的SQL代码片段，以便在其它语句中使用。 参数可以静态地（在加载的时候）确定下来，并且可以在不同的include元素中定义不同的参数值
    - 示例：
      ```
      <sql id="userColumns"> ${alias}.id,${alias}.username,${alias}.password </sql>
      ```
      
      - 这个SQL片段可以在其它语句中使用：
      
      ![image](https://user-images.githubusercontent.com/46510621/111898595-0e85a600-8a62-11eb-9dbf-eaed0a0b5923.png)
      
## 参数

  - 单个普通参数，取值：#{参数}。
  
    ![image](https://user-images.githubusercontent.com/46510621/111906076-d4c89580-8a89-11eb-8213-d90be966f74b.png)

  - 多个参数，被包装成一个Map传入。key是param1, param2...，值是参数的值。
    - 取值：#{param1 param2 ...}
  - Pojo对象，取值对象的属性名
  
    ![image](https://user-images.githubusercontent.com/46510621/111906350-13128480-8a8b-11eb-9ac3-79c959d1fca6.png)

## 结果映射（resultMap）    

  - resultMap元素是MyBatis中最重要最强大的元素。它可以让你从90%的JDBC ResultSets数据提取代码中解放出来
  - ResultMap的设计思想是，对简单的语句做到零配置，对于复杂一点的语句，只需要描述语句之间的关系就行了
  - 示例：

    ![image](https://user-images.githubusercontent.com/46510621/111906807-6d144980-8a8d-11eb-9031-4f344a8be5d4.png)
  
  - resultMap子元素：

    ![image](https://user-images.githubusercontent.com/46510621/111906914-f88dda80-8a8d-11eb-8944-65602ced4481.png)

  - id & result：
    - id和result元素都将一个列的值映射到一个简单数据类型（String, int, double, Date 等）的属性或字段。
    - 这两者之间的唯一不同是，id 元素对应的属性会被标记为对象的标识符，在比较对象实例时使用。 
    - 属性:

      ![image](https://user-images.githubusercontent.com/46510621/111907037-88338900-8a8e-11eb-8c85-0fdfcafd05cc.png)

  - 关联（association）
    - 关联（association）元素处理“有一个”类型的关系。需要指定目标属性名以及属性的javaType（很多时候MyBatis可以推断出来），在必要的情况下你还可以设置 JDBC类型，如果你想覆盖获取结果值的过程，还可以设置类型处理器。
    - MyBatis有两种不同的方式加载关联：
      - 嵌套Select查询：通过执行另外一个SQL映射语句来加载期望的复杂类型
        - 属性：

          ![image](https://user-images.githubusercontent.com/46510621/111907360-ead95480-8a8f-11eb-825e-4b8e48f5b9c2.png)

        - 示例：

          ![image](https://user-images.githubusercontent.com/46510621/111907428-35f36780-8a90-11eb-903c-8cf8f37ee750.png)

      - 嵌套结果映射：使用嵌套的结果映射来处理连接结果的重复子集
        - 属性：

          ![image](https://user-images.githubusercontent.com/46510621/111907472-69ce8d00-8a90-11eb-825f-182ef8d4f601.png)
   
        - 示例：

          ![image](https://user-images.githubusercontent.com/46510621/111907572-e6616b80-8a90-11eb-857d-11f796285b35.png)

  - 集合（collection）
    - 集合的嵌套Select查询：
      - 示例：
        
        ![image](https://user-images.githubusercontent.com/46510621/112324276-959a7e80-8ced-11eb-9428-cbed251236a2.png)
      
        - ofType属性：用来将JavaBean（或字段）属性的类型和集合存储的类型区分开来
  - 自动映射：
    - 当自动映射查询结果时，MyBatis会获取结果中返回的列名并在Java类中查找相同名字的属性（忽略大小写）。
    - 通常数据库列使用大写字母组成的单词命名，单词间用下划线分隔；而Java属性一般遵循驼峰命名法约定。为了在这两种命名方式之间启用自动映射，需要将 mapUnderscoreToCamelCase 设置为 true。
    - 有三种自动映射等级：
      - NONE - 禁用自动映射。仅对手动映射的属性进行映射
      - PARTIAL - 对除在内部定义了嵌套结果映射（也就是连接的属性）以外的属性进行映射。默认值
      - FULL - 自动映射所有属性
  - 缓存：
    - 默认情况下，只启用了本地的会话缓存，它仅仅对一个会话中的数据进行缓存。 要启用全局的二级缓存，只需要在你的 SQL 映射文件中添加一行：
      ```
      <cache/>
      ```
    - 开启缓存的作用：
      - 映射语句文件中的所有 select 语句的结果将会被缓存
      - 映射语句文件中的所有 insert、update 和 delete 语句会刷新缓存
      - 缓存会使用最近最少使用算法（LRU, Least Recently Used）算法来清除不需要的缓存
      - 缓存会被视为读/写缓存，这意味着获取到的对象并不是共享的，可以安全地被调用者修改，而不干扰其他调用者或线程所做的潜在修改
    - 缓存只作用于 cache 标签所在的映射文件中的语句。如果你混合使用 Java API 和 XML 映射文件，在共用接口中的语句将不会被默认缓存。你需要使用 @CacheNamespaceRef 注解指定缓存作用域。
    - 示例：

      ![image](https://user-images.githubusercontent.com/46510621/112330129-baddbb80-8cf2-11eb-9592-24fbc71f6ae6.png)
    
    - 可用的清除策略有：
      - LRU – 最近最少使用：移除最长时间不被使用的对象。默认值
      - FIFO – 先进先出：按对象进入缓存的顺序来移除它们。
      - SOFT – 软引用：基于垃圾回收器状态和软引用规则移除对象。
      - WEAK – 弱引用：更积极地基于垃圾收集器状态和弱引用规则移除对象。
      
      
