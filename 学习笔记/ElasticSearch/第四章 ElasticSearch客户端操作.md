# ElasticSearch的客户端操作

  - 三种作为ES客户端的方式：
    - elasticsearch-head插件。
    - 使用ES提供的Restful接口直接访问。
    - 使用ES提供的API进行访问。
    
## 使用Postman工具进行Restful接口访问

  - ![Postman Examples](代码/Postman/ElasticSearch_Postman)
  
## IK分词器

  - IKAnalyzer是一个开源的，基于java语言开发的轻量级的中文分词工具包。
  - IK提供了两个分词算法ik_smart 和 ik_max_word。
    - ik_smart：最少切分，会将文本做最粗粒度的拆分
    - ik_max_word：最细粒度划分，拆分成各种可能的组合
  - 扩展IK分词器字典：
    - 在IK分词器文件的config目录中新建自定义的字典文件，以.dic为后缀，并在文件中加入新增的词
    - 然后打开IKAnalyzer.cfg.xml文件，把自定义的字典添加到IK的字典中
    - 重启ES
  
## 使用Java API访问

  - ![Java Examples](代码/Java)
