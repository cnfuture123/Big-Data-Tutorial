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
    ```
    // 插入一条记录（选择字段，策略插入）
    boolean save(T entity);
    // 插入（批量）
    boolean saveBatch(Collection<T> entityList);
    // 插入（批量）
    boolean saveBatch(Collection<T> entityList, int batchSize);
    ```
  - SaveOrUpdate:
    ```
    // TableId 注解存在更新记录，否插入一条记录
    boolean saveOrUpdate(T entity);
    // 根据updateWrapper尝试更新，否继续执行saveOrUpdate(T)方法
    boolean saveOrUpdate(T entity, Wrapper<T> updateWrapper);
    // 批量修改插入
    boolean saveOrUpdateBatch(Collection<T> entityList);
    // 批量修改插入
    boolean saveOrUpdateBatch(Collection<T> entityList, int batchSize);
    ```
  - Remove:
    ```
    // 根据 entity 条件，删除记录
    boolean remove(Wrapper<T> queryWrapper);
    // 根据 ID 删除
    boolean removeById(Serializable id);
    // 根据 columnMap 条件，删除记录
    boolean removeByMap(Map<String, Object> columnMap);
    // 删除（根据ID 批量删除）
    boolean removeByIds(Collection<? extends Serializable> idList);
    ```
  - Update:
    ```
    // 根据 UpdateWrapper 条件，更新记录 需要设置sqlset
    boolean update(Wrapper<T> updateWrapper);
    // 根据 whereWrapper 条件，更新记录
    boolean update(T updateEntity, Wrapper<T> whereWrapper);
    // 根据 ID 选择修改
    boolean updateById(T entity);
    // 根据ID 批量更新
    boolean updateBatchById(Collection<T> entityList);
    // 根据ID 批量更新
    boolean updateBatchById(Collection<T> entityList, int batchSize);
    ```
  - Get:
    ```
    // 根据 ID 查询
    T getById(Serializable id);
    // 根据 Wrapper，查询一条记录。结果集，如果是多个会抛出异常，随机取一条加上限制条件 wrapper.last("LIMIT 1")
    T getOne(Wrapper<T> queryWrapper);
    // 根据 Wrapper，查询一条记录
    T getOne(Wrapper<T> queryWrapper, boolean throwEx);
    // 根据 Wrapper，查询一条记录
    Map<String, Object> getMap(Wrapper<T> queryWrapper);
    // 根据 Wrapper，查询一条记录
    <V> V getObj(Wrapper<T> queryWrapper, Function<? super Object, V> mapper);
    ```
  - List:
    ```
    // 查询所有
    List<T> list();
    // 查询列表
    List<T> list(Wrapper<T> queryWrapper);
    // 查询（根据ID 批量查询）
    Collection<T> listByIds(Collection<? extends Serializable> idList);
    // 查询（根据 columnMap 条件）
    Collection<T> listByMap(Map<String, Object> columnMap);
    // 查询所有列表
    List<Map<String, Object>> listMaps();
    // 查询列表
    List<Map<String, Object>> listMaps(Wrapper<T> queryWrapper);
    // 查询全部记录
    List<Object> listObjs();
    // 查询全部记录
    <V> List<V> listObjs(Function<? super Object, V> mapper);
    // 根据 Wrapper 条件，查询全部记录
    List<Object> listObjs(Wrapper<T> queryWrapper);
    // 根据 Wrapper 条件，查询全部记录
    <V> List<V> listObjs(Wrapper<T> queryWrapper, Function<? super Object, V> mapper);
    ```
  - Page:
    ```
    // 无条件分页查询
    IPage<T> page(IPage<T> page);
    // 条件分页查询
    IPage<T> page(IPage<T> page, Wrapper<T> queryWrapper);
    // 无条件分页查询
    IPage<Map<String, Object>> pageMaps(IPage<T> page);
    // 条件分页查询
    IPage<Map<String, Object>> pageMaps(IPage<T> page, Wrapper<T> queryWrapper);
    ```
  - Count:
    ```
    // 查询总记录数
    int count();
    // 根据 Wrapper 条件，查询总记录数
    int count(Wrapper<T> queryWrapper);
    ```
  - Chain:
    - query:
      ```
      // 链式查询 普通
      QueryChainWrapper<T> query();
      // 链式查询 lambda 式。注意：不支持 Kotlin
      LambdaQueryChainWrapper<T> lambdaQuery(); 
      // 示例：
      query().eq("column", value).one();
      lambdaQuery().eq(Entity::getId, value).list();
      ```
    - update:
      ```
      // 链式更改 普通
      UpdateChainWrapper<T> update();
      // 链式更改 lambda 式。注意：不支持 Kotlin 
      LambdaUpdateChainWrapper<T> lambdaUpdate();
      // 示例：
      update().eq("column", value).remove();
      lambdaUpdate().eq(Entity::getId, value).update(entity);
      ```
    
### Mapper CRUD接口

  - 说明：
    - 通用CRUD封装BaseMapper接口，为Mybatis-Plus启动时自动解析实体表关系映射转换为Mybatis内部对象注入容器
    - 参数Serializable为任意类型主键，Mybatis-Plus不推荐使用复合主键，约定每一张表都有自己的唯一id主键
    - 对象Wrapper为条件构造器
  - Insert:
    ```
    // 插入一条记录
    int insert(T entity);
    ```
  - Delete:
    ```
    // 根据 entity 条件，删除记录
    int delete(@Param(Constants.WRAPPER) Wrapper<T> wrapper);
    // 删除（根据ID 批量删除）
    int deleteBatchIds(@Param(Constants.COLLECTION) Collection<? extends Serializable> idList);
    // 根据 ID 删除
    int deleteById(Serializable id);
    // 根据 columnMap 条件，删除记录
    int deleteByMap(@Param(Constants.COLUMN_MAP) Map<String, Object> columnMap);
    ```
  - Update:
    ```
    // 根据 whereWrapper 条件，更新记录
    int update(@Param(Constants.ENTITY) T updateEntity, @Param(Constants.WRAPPER) Wrapper<T> whereWrapper);
    // 根据 ID 修改
    int updateById(@Param(Constants.ENTITY) T entity);
    ```
  - Select:
    ```
    // 根据 ID 查询
    T selectById(Serializable id);
    // 根据 entity 条件，查询一条记录
    T selectOne(@Param(Constants.WRAPPER) Wrapper<T> queryWrapper);

    // 查询（根据ID 批量查询）
    List<T> selectBatchIds(@Param(Constants.COLLECTION) Collection<? extends Serializable> idList);
    // 根据 entity 条件，查询全部记录
    List<T> selectList(@Param(Constants.WRAPPER) Wrapper<T> queryWrapper);
    // 查询（根据 columnMap 条件）
    List<T> selectByMap(@Param(Constants.COLUMN_MAP) Map<String, Object> columnMap);
    // 根据 Wrapper 条件，查询全部记录
    List<Map<String, Object>> selectMaps(@Param(Constants.WRAPPER) Wrapper<T> queryWrapper);
    // 根据 Wrapper 条件，查询全部记录。注意： 只返回第一个字段的值
    List<Object> selectObjs(@Param(Constants.WRAPPER) Wrapper<T> queryWrapper);

    // 根据 entity 条件，查询全部记录（并翻页）
    IPage<T> selectPage(IPage<T> page, @Param(Constants.WRAPPER) Wrapper<T> queryWrapper);
    // 根据 Wrapper 条件，查询全部记录（并翻页）
    IPage<Map<String, Object>> selectMapsPage(IPage<T> page, @Param(Constants.WRAPPER) Wrapper<T> queryWrapper);
    // 根据 Wrapper 条件，查询总记录数
    Integer selectCount(@Param(Constants.WRAPPER) Wrapper<T> queryWrapper);
    ```
    

    
