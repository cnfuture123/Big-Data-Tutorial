## 高并发的必备技能

  - Netty:
    - 概述：
      - Netty是JBOSS提供的一个Java开源框架，是基于NIO的客户端/服务端编程框架，NIO(Non-Blocking IO)是指非阻塞IO
      - 提供异步的、事件驱动的网络应用程序框架和工具
    - Netty优点：
      - API使用简单，开发门槛低
      - 功能强大，预置多种编解码功能，支持多种主流协议
      - 定制能力强，可以通过ChannelHandle对通信框架进行灵活扩展
      - 性能高
      - 社区活跃
  - Redis：
    - 主要应用场景：
      - 缓存：数据查询、短连接、新闻内容
      - 分布式会话
      - 任务队列：秒杀、抢购
      - 应用排行榜
      - 数据过期处理
  - ZooKeeper:
    - ZooKeeper是重要的分布式协调工具
  - 高性能HTTP通信技术：
    - 十万级QPS的Web应用框架图：
      
      <img width="771" alt="image" src="https://user-images.githubusercontent.com/46510621/156917658-510d2e66-dc81-4c8e-b5a2-3a6fd0f44180.png">

      - 对于十万级流量的系统应用，其架构一般可以分为三层：服务层、接入层、客户端层
        - 服务层一般执行的是Java应用程序，可以细分为传统的单体应用和Spring Cloud分布式应用
        - 接入层主要完成鉴权、限流、反向代理和负载均衡等功能，一般是使用Nginx + Lua作为接入服务器。为了保证高可用，会搭建冗余的接入服务器，然后使用KeepAlived中间件进行高可用监控管理，并且虚拟出外部IP，供外部访问
    - 千万级QPS的Web应用框架图：

      <img width="743" alt="image" src="https://user-images.githubusercontent.com/46510621/156917992-b670530e-c95c-4f51-971c-2a6b36f3a3e4.png">

      - 对于千万级QPS的Web应用，除了服务层的独立Tomcat或Spring Cloud微服务节点需要不断的横向扩展外，还需要进行以下两大增强：
        - 引入LVS负载均衡层，进行请求分发和接入层的负载均衡
        - 引入DNS服务器的负载均衡，可以在域名下面添加多个IP，由DNS服务器进行多个IP之间的负载均衡
        
      
    
    
