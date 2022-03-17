## Java API

  - 典型的应用目录结构：

    ![image](https://user-images.githubusercontent.com/46510621/112631615-12eefc00-8e72-11eb-88c5-76ccc89ddda3.png)

  - SqlSession
    - 使用MyBatis的主要Java接口就是SqlSession。你可以通过这个接口来执行命令，获取映射器示例和管理事务。
    - SqlSessionFactory对象包含创建SqlSession实例的各种方法。而SqlSessionFactory本身是由SqlSessionFactoryBuilder创建的，它可以从XML、注解或Java配置代码来创建SqlSessionFactory。
    - 当Mybatis与一些依赖注入框架（如Spring或者Guice）搭配使用时，SqlSession将被依赖注入框架创建并注入，所以你不需要使用 SqlSessionFactoryBuilder 或者 SqlSessionFactory
    - SqlSession包含了所有执行语句、提交或回滚事务以及获取映射器实例的方法
      - 语句执行方法：
        - 这些方法被用来执行定义在SQL映射XML文件中的 SELECT、INSERT、UPDATE 和 DELETE 语句。
        - 示例：
    
          ![image](https://user-images.githubusercontent.com/46510621/112632503-2a7ab480-8e73-11eb-92b1-8929a20de19b.png)

          - selectOne 和 selectList 的不同仅仅是 selectOne 必须返回一个对象或 null 值。如果返回值多于一个，就会抛出异常。如果你不知道返回对象会有多少，请使用 selectList。
          - selectMap将返回对象的其中一个属性作为 key 值，将对象作为 value 值，从而将多个结果集转为 Map 类型值。
          - 游标（Cursor）与列表（List）返回的结果相同，不同的是，游标借助迭代器实现了数据的惰性加载。
          - insert、update 以及 delete 方法返回的值表示受该语句影响的行数。
          - 还有 select 方法的三个高级版本，它们允许你限制返回行数的范围，或是提供自定义结果处理逻辑，通常在数据集非常庞大的情形下使用。

            ![image](https://user-images.githubusercontent.com/46510621/112633380-40d54000-8e74-11eb-818d-d1fd699e2835.png)

            - RowBounds参数会告诉MyBati 略过指定数量的记录，并限制返回结果的数量。RowBounds类的offset和limit值只有在构造函数时才能传入
            
              ![image](https://user-images.githubusercontent.com/46510621/112633525-682c0d00-8e74-11eb-978d-2ca7932313ba.png)
            
            - ResultHandler参数允许自定义每行结果的处理过程。你可以将它添加到List中、创建Map和Set，甚至丢弃每个返回值，只保留计算后的统计结果。
      - 立即批量更新方法:
        - 当你将ExecutorType设置为ExecutorType.BATCH 时，可以使用这个方法清除（执行）缓存在 JDBC 驱动类中的批量更新语句。
          ```
          List<BatchResult> flushStatements()
          ```
        - 事务控制方法:
          - 有四个方法用来控制事务作用域。如果你已经设置了自动提交或你使用了外部事务管理器，这些方法就没什么作用了。

            ![image](https://user-images.githubusercontent.com/46510621/112633964-f99b7f00-8e74-11eb-80c0-17ccd531adee.png)

        - 本地缓存：
          - Mybatis使用到了两种缓存：本地缓存（local cache）和二级缓存（second level cache）
          - 每当一个新session被创建，MyBatis就会创建一个与之相关联的本地缓存。任何在session执行过的查询结果都会被保存在本地缓存中，当再次执行参数相同的相同查询时，就不需要实际查询数据库了。本地缓存将会在做出修改、事务提交或回滚，以及关闭 session 时清空。
          - 默认情况下，本地缓存数据的生命周期等同于整个 session 的周期。可以通过设置 localCacheScope=STATEMENT 来只在语句执行时使用缓存
        - 确保SqlSession被关闭：
          ```
          void close()
          ```
        - 使用映射器:
          ```
          <T> T getMapper(Class<T> type)
          ```
          - 映射器接口不需要去实现任何接口或继承自任何类。只要方法签名可以被用来唯一识别对应的映射语句就可以了。
          - 如果你想（在有多个参数时）自定义参数的名称，那么你可以在参数上使用 @Param("paramName") 注解。
        - 映射器注解:
          - https://mybatis.org/mybatis-3/zh/java-api.html
        

