# Flyway

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

