## XML配置

  - MyBatis的配置文件包含影响MyBatis行为的设置和属性信息，顶层结构如下：
  
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
