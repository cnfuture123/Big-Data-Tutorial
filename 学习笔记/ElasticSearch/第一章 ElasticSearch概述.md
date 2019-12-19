# ElasticSearch概述

  - Elaticsearch，简称为es，是一个开源的高扩展的分布式全文检索引擎。
  - 它可以近乎实时的存储、检索数据。
  - 扩展性很好，可以扩展到上百台服务器，处理PB级别的数据。
  - es也使用Java开发并使用Lucene作为其核心来实现所有索引和搜索的功能。
  - 它的目的是通过简单的RESTful API来隐藏Lucene的复杂性，从而让全文搜索变得简单。
  
## ElasticSearch的使用案例

  - 2013年初，GitHub抛弃了Solr，采取ElasticSearch 来做PB级的搜索。
  - 维基百科：启动以ElasticSearch为基础的核心搜索架构。
  - 百度：目前覆盖百度内部20多个业务线（包括casio、云分析、网盟、预测、文库、直达号、钱包、风控等）。
  - 新浪：使用ES分析处理32亿条实时日志。
  - 阿里：使用ES构建挖财自己的日志采集和分析体系。
  
## ElasticSearch对比Solr

  - Solr利用Zookeeper进行分布式管理，而Elasticsearch自身带有分布式协调管理功能。
  - Solr支持更多格式的数据，而Elasticsearch仅支持json文件格式。
  - Solr官方提供的功能更多，而Elasticsearch本身更注重于核心功能，高级功能多由第三方插件提供。
  - Solr在传统的搜索应用中表现好于Elasticsearch，但在处理实时搜索应用时效率明显低于Elasticsearch。
  
