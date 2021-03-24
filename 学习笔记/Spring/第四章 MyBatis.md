# MyBatis

## 简介

  - MyBatis是一款优秀的持久层框架，它支持自定义SQL、存储过程以及高级映射。
  - MyBatis免除了几乎所有的JDBC代码以及设置参数和获取结果集的工作。
  - MyBatis可以通过简单的XML或注解来配置和映射原始类型、接口和Java POJO为数据库中的记录。
  - 参考：
    - https://mybatis.org/mybatis-3/zh/configuration.html
    - https://www.w3cschool.cn/mybatis/

## 几种持久化技术的对比

  - JDBC：
    - SQL语句和Java代码耦合在一起，不易维护和扩展
    - 操作相对复杂，需要操作底层大量对象，并且需要准确关闭
  - Hibernate/JPA：
    - 采用ORM来替代JDBC，本质上ORM是对JDBC的封装
    - ORM关系映射对象，把数据表和POJO对象进行映射，从而使得对数据库的操作更加面向对象
    - 对于复杂的操作全表映射不灵活，无法根据不同条件组装sql，对多表联查执行性较差
    - 内部自动产生的SQL，不易做SQL优化，导致HQL的性能较差
  - MyBatis:
    - 解除sql与程序代码的耦合：通过提供DAL层，将业务逻辑和数据访问逻辑分离，使系统的设计更清晰，更易维护
    - 提供映射标签，支持对象与数据库的ORM字段关系映射
    - 提供xml标签，支持编写动态sql，sql写在xml里，便于统一管理和优化
    - 需要自己编写SQL，配置比Hibernate多，工作量较大

## MyBatis安装使用

  - 要使用MyBatis， 只需将mybatis-x.x.x.jar文件置于类路径（classpath）中即可
  - 如果使用Maven来构建项目，则需将下面的依赖代码置于pom.xml文件中：
    ```
    <dependency>
      <groupId>org.mybatis</groupId>
      <artifactId>mybatis</artifactId>
      <version>x.x.x</version>
    </dependency>
    ```
    
## Mybatis架构

  - 功能架构：

    ![图片](https://user-images.githubusercontent.com/46510621/111876243-d1c19c80-89d8-11eb-9ad5-d679937e9a88.png)
    
    - API接口层：提供给外部使用的接口API，开发人员通过这些本地API来操纵数据库。接口层一接收到调用请求就会调用数据处理层来完成具体的数据处理。
    - 数据处理层：负责具体的SQL查找、SQL解析、SQL执行和执行结果映射处理等。它主要的目的是根据调用的请求完成一次数据库操作。
    - 基础支撑层：负责最基础的功能支撑，包括连接管理、事务管理、配置加载和缓存处理，这些都是共用的东西，将他们抽取出来作为最基础的组件。为上层的数据处理层提供最基础的支撑。
      
## XML配置

  - MyBatis的配置文件包含了会深深影响MyBatis行为的设置和属性信息，顶层结构如下：
  
    ![image](https://user-images.githubusercontent.com/46510621/111865610-0f0b3780-89a3-11eb-9e3f-1a206092b544.png)

  - 属性（properties）
    - 这些属性可以在外部进行配置，并可以进行动态替换。既可以在典型的Java属性文件中配置这些属性，也可以在properties元素的子元素中设置
    - 示例：
      
      ![image](https://user-images.githubusercontent.com/46510621/111865795-0d8e3f00-89a4-11eb-8589-ef54e7b9de18.png)

      ![image](https://user-images.githubusercontent.com/46510621/111865805-1a129780-89a4-11eb-8fad-f86fae85a92f.png)

    - 如果一个属性在不只一个地方进行了配置，那么，MyBatis将按照下面的顺序来加载：
      - 首先读取在properties元素体内指定的属性
      - 然后根据properties元素中的resource属性读取类路径下属性文件，或根据url属性指定的路径读取属性文件，并覆盖之前读取过的同名属性
      - 最后读取作为方法参数传递的属性，并覆盖之前读取过的同名属性
  - 设置（settings）
    - 这是MyBatis中极为重要的调整设置，它们会改变MyBatis的运行时行为。部分配置如下：

      ![image](https://user-images.githubusercontent.com/46510621/111893266-86d97080-8a3c-11eb-8521-ec61b8a52cd4.png)

    - 完整的settings元素的示例：
      
      ![image](https://user-images.githubusercontent.com/46510621/111893255-64475780-8a3c-11eb-9544-f1d2719c3a57.png)

  - 类型别名（typeAliases）
    - 类型别名可为Java类型设置一个缩写名字。 它仅用于XML配置，意在降低冗余的全限定类名书写
    - 示例:
    
      ![image](https://user-images.githubusercontent.com/46510621/111893330-fb141400-8a3c-11eb-8e38-26ace36ebe56.png)

  - 类型处理器（typeHandlers）
    - MyBatis在设置预处理语句（PreparedStatement）中的参数或从结果集中取出一个值时， 都会用类型处理器将获取到的值以合适的方式转换成Java类型  
  - 对象工厂（objectFactory）
    - 每次MyBatis创建结果对象的新实例时，它都会使用一个对象工厂（ObjectFactory）实例来完成实例化工作。 默认的对象工厂需要做的仅仅是实例化目标类，要么通过默认无参构造方法，要么通过存在的参数映射来调用带有参数的构造方法。  
    - setProperties方法可以被用来配置ObjectFactory，在初始化ObjectFactory实例后， objectFactory元素体中定义的属性会被传递给setProperties方法
  - 插件（plugins）
    - MyBatis允许你在映射语句执行过程中的某一点进行拦截调用。默认情况下，MyBatis允许使用插件来拦截的方法调用包括：
      
      ![image](https://user-images.githubusercontent.com/46510621/111893612-f2bcd880-8a3e-11eb-8b49-0c3132cbdb0e.png)

    - 示例：
      
      ![image](https://user-images.githubusercontent.com/46510621/111893760-d8372f00-8a3f-11eb-9d89-0448427066b3.png)

  - 环境配置（environments）
    - MyBatis可以配置成适应多种环境，这种机制有助于将SQL映射应用于多种数据库之中，但每个SqlSessionFactory实例只能选择一种环境
    - 每个数据库对应一个SqlSessionFactory实例:
      - 可以接受环境配置的两个方法签名是：
        ```
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(reader, environment);
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(reader, environment, properties);
        ```
    - environments元素定义了如何配置环境:

      ![image](https://user-images.githubusercontent.com/46510621/111893877-fd786d00-8a40-11eb-8985-faa6b81586b1.png)

      - 事务管理器（transactionManager）: MyBatis中有两种类型的事务管理器（type="[JDBC|MANAGED]"）
        - JDBC：直接使用了JDBC的提交和回滚设施，它依赖从数据源获得的连接来管理事务作用域
        - MANAGED：它从不提交或回滚一个连接，而是让容器来管理事务的整个生命周期（比如JEE应用服务器的上下文）。将closeConnection属性设置为false来阻止默认的关闭行为。
      - 数据源（dataSource）：
        - dataSource元素使用标准的JDBC数据源接口来配置JDBC连接对象的资源
        - 三种内建的数据源类型（type="[UNPOOLED|POOLED|JNDI]"）：
          - UNPOOLED：这个数据源的实现会每次请求时打开和关闭连接。性能表现则依赖于使用的数据库，UNPOOLED类型的数据源仅仅需要配置以下5种属性：

            ![image](https://user-images.githubusercontent.com/46510621/111894021-45e45a80-8a42-11eb-9c9a-d7ed46bc78d5.png)

          - POOLED：这种数据源的实现利用“池”的概念将JDBC连接对象组织起来，避免了创建新的连接实例时所必需的初始化和认证时间。
    
            ![image](https://user-images.githubusercontent.com/46510621/111894060-9c519900-8a42-11eb-8d67-f47bb1263a02.png)

          - JNDI：这个数据源实现是为了能在如EJB或应用服务器这类容器中使用，容器可以集中或在外部配置数据源，然后放置一个JNDI上下文的数据源引用

            ![image](https://user-images.githubusercontent.com/46510621/111894123-108c3c80-8a43-11eb-9abc-5d10af90e280.png)
      
      - 实际开发中使用Spring管理数据源，并进行事务控制来覆盖上述配置
    - 映射器（mappers）
      - 用于在MyBatis初始化的时候，告诉MyBatis需要引入哪些映射文件
      - mapper逐个注册SQL映射文件：
        - resource：引入类路径下的文件
        - url：引入网络路径或磁盘路径下的文件
        - class：映射器接口实现类

          ![image](https://user-images.githubusercontent.com/46510621/111894244-2817f500-8a44-11eb-9d49-a54f63c7ce6a.png)

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

## 动态SQL

  - if
    - 使用动态 SQL 最常见情景是根据条件包含 where 子句的一部分。
    - 示例：

      ![image](https://user-images.githubusercontent.com/46510621/112331809-37bd6500-8cf4-11eb-9baf-cba338354b63.png)

  - choose、when、otherwise
    - MyBatis提供了choose元素，它有点像Java中的switch语句，从多个条件中选择一个使用。
    - 示例：
      
      ![image](https://user-images.githubusercontent.com/46510621/112332731-f4afc180-8cf4-11eb-8ba3-25fdfbab8f5a.png)

  - trim、where、set
    - where 元素只会在子元素返回任何内容的情况下才插入 “WHERE” 子句。而且，若子句的开头为 “AND” 或 “OR”，where 元素也会将它们去除。
      - 示例：

        ![image](https://user-images.githubusercontent.com/46510621/112333646-c088d080-8cf5-11eb-8d7e-121f81a41688.png)
    
    - 用于动态更新语句的类似解决方案叫做 set。set 元素可以用于动态包含需要更新的列，忽略其它不更新的列。
      - 示例：

        ![image](https://user-images.githubusercontent.com/46510621/112333961-034aa880-8cf6-11eb-8466-26fd17b4bede.png)
  
  - foreach
    - 动态 SQL 的另一个常见使用场景是对集合进行遍历（尤其是在构建 IN 条件语句的时候）。
    - foreach允许你指定一个集合，声明可以在元素体内使用的集合项（item）和索引（index）变量。也可以指定开头与结尾的字符串以及集合项迭代之间的分隔符。
    - 你可以将任何可迭代对象（如 List、Set 等）、Map 对象或者数组对象作为集合参数传递给 foreach。当使用可迭代对象或者数组时，index 是当前迭代的序号，item 的值是本次迭代获取到的元素。当使用 Map 对象（或者 Map.Entry 对象的集合）时，index 是键，item 是值。
    - 示例：

      ![image](https://user-images.githubusercontent.com/46510621/112334555-7a803c80-8cf6-11eb-850f-78587ec38bee.png)

  - script
    - 要在带注解的映射器接口类中使用动态 SQL，可以使用 script 元素。
    - 示例：

      ![image](https://user-images.githubusercontent.com/46510621/112334861-b915f700-8cf6-11eb-9c5b-e6b743fcdb01.png)

  - bind
    - bind 元素允许你在 OGNL 表达式以外创建一个变量，并将其绑定到当前的上下文。
    - 示例：

      ![image](https://user-images.githubusercontent.com/46510621/112335104-ea8ec280-8cf6-11eb-83f9-066d6a934074.png)

  
