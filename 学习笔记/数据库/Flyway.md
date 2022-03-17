## 简介

  - Flyway是开源的数据库迁移工具
  - 包含7个基本的命令：Migrate, Clean, Info, Validate, Undo, Baseline, Repair
  - 数据迁移可以基于SQL或Java实现
  - 数据库迁移的作用：
    - 从头开始重建一个数据库
    - 时刻清楚数据库处于什么状态
    - 以一种确定的方式将当前版本的数据库迁移到新版本
  - Flyway工作方式：
    - 首先尝试定位schema history table，默认表名是flyway_schema_history，这个表用于记录数据库的状态
    - 然后Flyway会扫描应用的文件系统或类路径的迁移脚本，由SQL或Java实现
    - 迁移脚本会根据版本号排序，然后依次执行。每个迁移脚本执行时，schema history table会被更新
      ![image](https://user-images.githubusercontent.com/46510621/131506114-fa4ae64b-4674-4eea-b138-eb52a2ad2478.png)
    - 当需要迁移数据库时，只需要创建一个新的迁移脚本，并设置它的版本号高于当前的版本号。等下次Flyway启动时，会定位到新增的迁移脚本，并更新数据库

## Migrations

### 简介

  - Flyway对数据库做的所有修改被称为迁移，迁移可以是有版本的或者重复的，有版本的迁移支持2种形式：常规，撤销
  - 有版本的迁移：
    - 包含版本，描述，检验和。版本是唯一的，校验和用来检测偶发的修改
    - 最常用的迁移形式，按顺序只执行一次
    - 可以通过相同版本的undo migration撤销有版本的迁移
  - 重复的迁移：
    - 包含描述，校验和，没有版本。每次校验和改变时都会执行，而不是只执行一次
    - 在一次迁移中，重复的迁移在所有有版本的迁移之后执行
    
### SQL_based 迁移

  - SQL_based 迁移用于：
    - DDL修改(CREATE/ALTER/DROP statements for TABLES,VIEWS,TRIGGERS,SEQUENCES,…)
    - Simple reference data changes (CRUD in reference data tables)
    - Simple bulk data changes (CRUD in regular data tables)
  - 命名：
    ![image](https://user-images.githubusercontent.com/46510621/131607409-af06478a-7c59-4109-af5a-9c21e74f8b0a.png)
  - 找到Flyway脚本：
    - 通过locations属性找到迁移脚本所在的位置
    - 示例：
      ```
      flyway.locations=flyway
      ```
      ![image](https://user-images.githubusercontent.com/46510621/131607767-f7e85ea6-e13c-4502-b7d3-cfd1fca8bb9e.png)
    
## 参考

  - https://flywaydb.org/documentation/concepts/migrations#discovery
  
  
