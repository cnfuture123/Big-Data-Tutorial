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

## 代码生成器

  - 添加代码生成器依赖：
    ```
    <dependency>
        <groupId>com.baomidou</groupId>
        <artifactId>mybatis-plus-generator</artifactId>
        <version>3.4.1</version>
    </dependency>
    ```
  - 添加模版引擎依赖：
    - MyBatis-Plus支持Velocity（默认）、Freemarker、Beetl，用户可以选择自己熟悉的模板引擎，如果都不满足您的要求，可以采用自定义模板引擎。
    ```
    <dependency>
        <groupId>org.freemarker</groupId>
        <artifactId>freemarker</artifactId>
        <version>latest-freemarker-version</version>
    </dependency>
    ```
    - 如果选择了非默认引擎，需要在AutoGenerator中设置模板引擎：
      ```
      AutoGenerator generator = new AutoGenerator();
      // set freemarker engine
      generator.setTemplateEngine(new FreemarkerTemplateEngine());
      // set beetl engine
      generator.setTemplateEngine(new BeetlTemplateEngine());
      // set custom engine (reference class is your custom engine class)
      generator.setTemplateEngine(new CustomTemplateEngine());
      ```
  - 编写配置：
    - 配置GlobalConfig:
      ```
      GlobalConfig globalConfig = new GlobalConfig();
      globalConfig.setOutputDir(System.getProperty("user.dir") + "/src/main/java");
      globalConfig.setAuthor("jobob");
      globalConfig.setOpen(false);
      ```
    - 配置DataSourceConfig:
      ```
      DataSourceConfig dataSourceConfig = new DataSourceConfig();
      dataSourceConfig.setUrl("jdbc:mysql://localhost:3306/ant?useUnicode=true&useSSL=false&characterEncoding=utf8");
      dataSourceConfig.setDriverName("com.mysql.jdbc.Driver");
      dataSourceConfig.setUsername("root");
      dataSourceConfig.setPassword("password");
      ```
  - 自定义模版引擎：
    - 继承类 com.baomidou.mybatisplus.generator.engine.AbstractTemplateEngine
  - 自定义代码模版：
    ```
    //指定自定义模板路径, 位置：/resources/templates/entity2.java.ftl(或者是.vm)
    //注意不要带上.ftl(或者是.vm), 会根据使用的模板引擎自动识别
    TemplateConfig templateConfig = new TemplateConfig()
        .setEntity("templates/entity2.java");
    AutoGenerator mpg = new AutoGenerator();
    //配置自定义模板
    mpg.setTemplate(templateConfig);
    ```
    
## CRUD接口

### Service CRUD接口

  - 说明：
    - 通用Service CRUD封装IService接口，进一步封装CRUD采用 get查询单行，remove删除，list查询集合，page分页前缀命名方式区分Mapper层避免混淆
    - 建议如果存在自定义通用Service方法的可能，请创建自己的IBaseService继承 Mybatis-Plus 提供的基类
    - 对象Wrapper为条件构造器
  - Save:
    
          

