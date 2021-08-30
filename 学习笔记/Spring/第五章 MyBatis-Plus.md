# MyBatis-Plus

## 简介

  - 定义：MyBatis-Plus（简称 MP）是一个 MyBatis的增强工具，在MyBatis的基础上只做增强不做改变，为简化开发、提高效率而生。
  - 特性：
    ![image](https://user-images.githubusercontent.com/46510621/131303636-bdfb10c8-6496-416b-b205-67e441d31c96.png)
  - 框架结构：
    ![image](https://user-images.githubusercontent.com/46510621/131304843-88dbeeab-1280-4b94-bd4e-9372453f1fd6.png)

## 安装

  - Spring Boot:
    ```
    <dependency>
        <groupId>com.baomidou</groupId>
        <artifactId>mybatis-plus-boot-starter</artifactId>
        <version>mybatis-plus-latest-version</version>
    </dependency>
    ```
  - Spring MVC:
    ```
    <dependency>
        <groupId>com.baomidou</groupId>
        <artifactId>mybatis-plus</artifactId>
        <version>mybatis-plus-latest-version</version>
    </dependency>
    ```

## 注解

  - @TableName: 表名注解
    ![image](https://user-images.githubusercontent.com/46510621/131311573-16cd591f-12f4-43b6-9b08-77151a780d77.png)
  - @TableId: 主键注解
    ![image](https://user-images.githubusercontent.com/46510621/131311973-ce1f7357-23f7-4efd-b7ab-9f04f404f40e.png)
    - IdType可选项：
      ![image](https://user-images.githubusercontent.com/46510621/131312241-006252dc-1e27-478c-840d-65c733947eb9.png)
  - @TableField: 字段注解（非主键）
    - FieldStrategy: 
      ![image](https://user-images.githubusercontent.com/46510621/131318388-d501f9ed-6bcc-4c29-b427-cc496d19fac9.png)
    - FieldFill:
      ![image](https://user-images.githubusercontent.com/46510621/131318421-ecab77ed-3bfb-4376-afcc-62d8d2c138c2.png)
  - @Version: 乐观锁注解，标记在字段上
  - @EnumValue: 枚举类注解(注解在枚举字段上)  
  - @TableLogic: 逻辑删除
    ![image](https://user-images.githubusercontent.com/46510621/131318895-871079bc-78e9-4aea-b5a0-524656958752.png)

  

