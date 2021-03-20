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

    


    
    
    
