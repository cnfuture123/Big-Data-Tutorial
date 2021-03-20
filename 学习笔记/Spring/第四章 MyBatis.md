# MyBatis

## 简介

  - MyBatis是一款优秀的持久层框架，它支持自定义SQL、存储过程以及高级映射。
  - MyBatis免除了几乎所有的JDBC代码以及设置参数和获取结果集的工作。
  - MyBatis可以通过简单的XML或注解来配置和映射原始类型、接口和Java POJO为数据库中的记录。

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

    - 图片：https://www.jianshu.com/p/15781ec742f2
    
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
    

    
    



    
    
    
