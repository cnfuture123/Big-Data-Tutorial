## XML映射文件

  - MyBatis的真正强大在于它的语句映射，这是它的魔力所在。映射器的XML文件相对简单,如果拿它跟具有相同功能的 JDBC 代码进行对比，你会立即发现省掉了将近 95% 的代码。
    
    ![image](https://user-images.githubusercontent.com/46510621/111897762-b4cead00-8a5c-11eb-9959-1bbd939fa3b3.png)

  - select元素：
    - 示例：

      ![image](https://user-images.githubusercontent.com/46510621/111897900-9917d680-8a5d-11eb-9dd1-a8a5a0e66d1e.png)

    - select元素的部分属性：

      ![image](https://user-images.githubusercontent.com/46510621/111898021-4a1e7100-8a5e-11eb-85c9-6fe5c2caff2c.png)

  - insert, update和delete元素：
    - 示例：

      ![image](https://user-images.githubusercontent.com/46510621/111898283-00cf2100-8a60-11eb-80c9-e08bbcb270f7.png)

    - 元素属性：

      ![image](https://user-images.githubusercontent.com/46510621/111898181-7686bd00-8a5f-11eb-92e0-c502fd3039e7.png)

  - 生成主键：
    - 如果你的数据库支持自动生成主键的字段（比如MySQL和SQL Server），那么你可以设置useGeneratedKeys=”true”，然后再把keyProperty设置为目标属性
    
      ![image](https://user-images.githubusercontent.com/46510621/111898425-f7928400-8a60-11eb-893e-b7461fa7fb7f.png)

  - sql元素：
    - 用来定义可重用的SQL代码片段，以便在其它语句中使用。 参数可以静态地（在加载的时候）确定下来，并且可以在不同的include元素中定义不同的参数值
    - 示例：
      ```
      <sql id="userColumns"> ${alias}.id,${alias}.username,${alias}.password </sql>
      ```
      
      - 这个SQL片段可以在其它语句中使用：
      
      ![image](https://user-images.githubusercontent.com/46510621/111898595-0e85a600-8a62-11eb-9dbf-eaed0a0b5923.png)
      
      
