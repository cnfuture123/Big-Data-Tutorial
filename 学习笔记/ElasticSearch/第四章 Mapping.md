## Mapping概述

  - Mapping是定义文档及其属性如何存储和索引的过程
  - 每个文档是一群属性的集合，每个属性有各自的数据类型。Mapping包含了这些属性的定义和元数据属性
  - 可以使用动态mapping或显示mapping定义数据
  - 映射限制：
    - 可以使用如下属性限制属性映射的数量（包含手动或动态创建的），防止文档出现映射爆炸
      - index.mapping.total_fields.limit：索引中属性数量的最大值，默认是1000
      - index.mapping.depth.limit：属性的最大深度，由内嵌对象的数量决定，默认是20
      - index.mapping.nested_fields.limit：索引中嵌套映射的最大数量，默认是50
      - index.mapping.nested_objects.limit：单个文档包含嵌套Json对象的最大数量，默认是10000
      - index.mapping.field_name_length.limit：属性名称的最大长度，默认Long.MAX_VALUE，即没有限制

## 动态Mapping

  - 动态映射是自定义的mapping，可以基于匹配条件动态地添加属性
  - 动态属性映射：
    - 当ES检测到文档中出现新的属性，它动态地将这个属性添加到映射中
    - 可以通过dynamic参数为true或runtime，指示ES如何基于接收的文档动态创建属性
      ![image](https://user-images.githubusercontent.com/46510621/132091033-14b6c71a-b562-45e1-8a26-633da74e8934.png)
    - 可以禁用document和object级别的动态映射，设置dynamic参数为false会忽略新的属性，设置为strict会在发现unknown属性时拒绝文档
  - 可以自定义日期和数字类型数据检测的动态属性映射的规则：
    - 日期检测：
      - 如果启用date_detection，string类型的属性会被检测它们的内容是否匹配dynamic_date_formats参数定义的日期类型。如果能匹配，会添加一个相应类型的日期属性
      - 设置date_detection为false可以禁用动态日期检测
      - 通过dynamic_date_formats参数设置被检测的日期格式
        ![image](https://user-images.githubusercontent.com/46510621/132091407-12a87e79-5598-46ce-b5b4-49516f9d6e96.png)
    - 数字检测：
      ![image](https://user-images.githubusercontent.com/46510621/132091674-ed52c201-9d8a-4d9a-9eaf-2c695e6569d2.png)
  - 动态模版：
    - 动态模版可以在默认的动态属性映射规则之外增加更多对映射数据方式的控制，可以通过模版自定义映射方式，基于匹配的条件动态添加属性

## 显示映射

  - 显示映射可以更加精准的控制映射的定义
  - 可以在创建索引或添加属性到已有索引时创建属性映射
    - 创建带显示映射的索引：
      ![image](https://user-images.githubusercontent.com/46510621/132091887-77054d70-200f-42e0-90c7-931bae048575.png)
    - 添加属性到已有索引:
      ![image](https://user-images.githubusercontent.com/46510621/132091901-6329b682-b830-4fda-b86c-d44a52ff5f04.png)
    - 查看索引的映射：
      ![image](https://user-images.githubusercontent.com/46510621/132091929-5fe6a2d5-3148-4284-869d-8fc4db64c036.png)
    - 查看指定属性的映射：
      ![image](https://user-images.githubusercontent.com/46510621/132091947-e7a0aecb-1ea2-4770-88f0-f06d66e25214.png)
      
      
      
