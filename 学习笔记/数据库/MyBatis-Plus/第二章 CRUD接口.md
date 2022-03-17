## Service CRUD接口

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
    
## Mapper CRUD接口

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
    
## 条件构造器

  - AbstractWrapper:
    - QueryWrapper(LambdaQueryWrapper) 和 UpdateWrapper(LambdaUpdateWrapper) 的父类
    - 用于生成sql的where条件, entity属性也用于生成sql的where条件
    - entity生成的where条件与使用各个api生成的where条件没有任何关联行为
  - QueryWrapper:
    - 继承自AbstractWrapper,自身的内部属性entity也用于生成where条件
    - LambdaQueryWrapper可以通过new QueryWrapper().lambda()方法获取
  - UpdateWrapper:
    - 继承自AbstractWrapper, 自身的内部属性entity也用于生成where条件
    - LambdaUpdateWrapper, 可以通过new UpdateWrapper().lambda()方法获取
    
## 逻辑删除

  - 说明：
    - 只对自动注入的sql起效
      - 插入: 不作限制
      - 查找: 追加where条件过滤掉已删除数据,且使用 wrapper.entity 生成的where条件会忽略该字段
      - 更新: 追加where条件防止更新到已删除数据,且使用 wrapper.entity 生成的where条件会忽略该字段
      - 删除: 转变为更新
    - 逻辑删除是为了方便数据恢复和保护数据本身价值等的一种方案，但实际就是删除
    - 如果你需要频繁查出来看就不应使用逻辑删除，而是以一个状态去表示
  - 使用方法：
    - 实体类字段上加上@TableLogic注解
      ```
      @TableLogic
      private Integer deleted
      ```

## 字段类型处理器

  - 类型处理器：
    - 用于JavaType与JdbcType之间的转换
    - 用于PreparedStatement设置参数值和从ResultSet或CallableStatement中取出一个值
      ```
      @TableField(typeHandler = JacksonTypeHandler.class)
      // @TableField(typeHandler = FastjsonTypeHandler.class)
      private OtherInfo otherInfo;
      ```
      
      
