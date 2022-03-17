## 动态SQL

  - if
    - 使用动态 SQL 最常见情景是根据条件包含 where 子句的一部分。
    - 示例：

      ![image](https://user-images.githubusercontent.com/46510621/112331809-37bd6500-8cf4-11eb-9baf-cba338354b63.png)

  - choose、when、otherwise
    - MyBatis提供了choose元素，它有点像Java中的switch语句，从多个条件中选择一个使用。
    - 示例：
      
      ![image](https://user-images.githubusercontent.com/46510621/112332731-f4afc180-8cf4-11eb-8ba3-25fdfbab8f5a.png)

  - trim、where、set
    - where 元素只会在子元素返回任何内容的情况下才插入 “WHERE” 子句。而且，若子句的开头为 “AND” 或 “OR”，where 元素也会将它们去除。
      - 示例：

        ![image](https://user-images.githubusercontent.com/46510621/112333646-c088d080-8cf5-11eb-8d7e-121f81a41688.png)
    
    - 用于动态更新语句的类似解决方案叫做 set。set 元素可以用于动态包含需要更新的列，忽略其它不更新的列。
      - 示例：

        ![image](https://user-images.githubusercontent.com/46510621/112333961-034aa880-8cf6-11eb-8466-26fd17b4bede.png)
  
  - foreach
    - 动态 SQL 的另一个常见使用场景是对集合进行遍历（尤其是在构建 IN 条件语句的时候）。
    - foreach允许你指定一个集合，声明可以在元素体内使用的集合项（item）和索引（index）变量。也可以指定开头与结尾的字符串以及集合项迭代之间的分隔符。
    - 你可以将任何可迭代对象（如 List、Set 等）、Map 对象或者数组对象作为集合参数传递给 foreach。当使用可迭代对象或者数组时，index 是当前迭代的序号，item 的值是本次迭代获取到的元素。当使用 Map 对象（或者 Map.Entry 对象的集合）时，index 是键，item 是值。
    - 示例：

      ![image](https://user-images.githubusercontent.com/46510621/112334555-7a803c80-8cf6-11eb-850f-78587ec38bee.png)

  - script
    - 要在带注解的映射器接口类中使用动态 SQL，可以使用 script 元素。
    - 示例：

      ![image](https://user-images.githubusercontent.com/46510621/112334861-b915f700-8cf6-11eb-9c5b-e6b743fcdb01.png)

  - bind
    - bind 元素允许你在 OGNL 表达式以外创建一个变量，并将其绑定到当前的上下文。
    - 示例：

      ![image](https://user-images.githubusercontent.com/46510621/112335104-ea8ec280-8cf6-11eb-83f9-066d6a934074.png)
