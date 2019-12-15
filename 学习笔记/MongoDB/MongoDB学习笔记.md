# MongoDB学习笔记

## 数据库

  - 数据库是按照数据结构来组织、存储和管理数据的仓库。
  - 如果程序都是在内存中运行的，一旦程序运行结束或者计算机断电，程序运行中的数据都会丢失。
  - 需要将一些程序运行的数据持久化到硬盘之中，以确保数据的安全性。而数据库就是数据持久化的最佳选择。
  - 数据库就是存储数据的仓库。
  - 数据库分类：
    - 关系型数据库：
      - MySQL、Oracle、DB2、SQL Server...
      - 关系数据库中全都是表。
    - 非关系型数据库：
      - MongoDB ---> 文档数据库
      - Redis ---> 键值对数据库
      
## MongoDB简介

  - MongoDB是一个NoSQL的数据库。
  - MongoDB是为快速开发互联网Web应用而设计的数据库系统。
  - MongoDB的数据模型是面向文档的，所谓文档是一种类似于JSON的结构，简单理解MongoDB这个数据库中存的是各种各样的JSON。（BSON）
  - 基本概念：
    - 文档(document)：文档数据库中的最小单位，我们存储和操作的内容都是文档。类似于JS中的对象，在MongoDB中每一条数据都是一个文档。
    - 集合(collection)：
      - 集合就是一组文档，也就是集合是用来存放文档的。
      - 集合中存储的文档可以是各种各样的，没有格式要求。
    - 文档 ---> 集合 ---> MongoDB数据库
    - 在MongoDB中，数据库和集合都不需要手动创建，当我们创建文档时，如果文档所在的集合或数据库不存在会自动创建数据库和集合。
  - 基本指令：
    - show dbs/databases: 显示当前的所有数据库
    - use database: 进入指定的数据库
    - show collections: 显示数据库中所有的集合
    - db: 显示当前所在的数据库
  - MongoDB CURD操作：
    - 插入文档：
      - db.<collection>.insert(): 可以向集合中插入一个或多个文档
      - db.<collection>.insertOne(): 向集合中插入一个文档
      - db.<collection>.insertMany(): 向集合中插入多个文档
    - 查询文档：
      - db.<collection>.find(): 可以根据指定条件从集合中查询所有符合条件的文档, 返回的是一个数组
      - db.<collection>.findOne(): 查询第一个符合条件的文档, 返回的是一个对象
      - db.<collection>.find().count(): 查询符合条件的文档的数量
    - 修改文档：
      - db.<collection>.update(): 可以修改、替换集合中的一个或多个文档
      - db.<collection>.updateOne(): 修改集合中的一个文档
      - db.<collection>.updateMany(): 修改集合中的多个文档
      - db.<collection>.replaceOne(): 替换集合中的一个文档
    - 删除文档：
      - db.<collection>.remove(): 删除集合中的一个或多个文档（默认删除多个）
      - db.<collection>.deleteOne(): 删除集合中的一个文档
      - db.<collection>.deleteMany(): 删除集合中的多个文档
      - db.<collection>.remove({}): 清空一个集合
      - db.<collection>.drop(): 删除一个集合
      - db.dropDatabase(): 删除一个数据库
    
    
    
      
