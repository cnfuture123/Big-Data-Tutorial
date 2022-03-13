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
        - 接入层主要完成鉴权、限流、反向代理和负载均衡等功能，一般是使用Nginx + Lua作为接入服务器。
          - Nginx将客户端请求分发给上游的多个Web服务；Nginx向外暴露一个外网IP，Nginx和内部Web服务之间使用内网访问
          - Nginx需要保障负载均衡，并且通过Lua脚本具备动态伸缩、动态增加Web服务节点的能力
          - Nginx需要保障系统的高可用，任何一台Web服务节点挂了，Nginx可以将流量迁移到其他Web服务节点上
          - Nginx也应用了Reactor模式，执行过程主要包括一个Master和n个Worker进程，所有进程都是单线程的。Nginx使用了多路复用和事件通知，Master进程用于接收外界的信号，并给Worker进程发送信号，同时监控Worker进程的工作状态。Worker进程是外部请求的处理者，每个Worker请求相互独立且平等的竞争来自客户端的请求
          - 高可用：使用Nginx + KeepAlived组合模式：
            - 使用至少两台Nginx组成一个集群，分别部署KeepAlived，设置成相同的虚拟IP供下游访问
            - 当一台Nginx挂了，KeepAlived能够探测到，并会将流量自动迁移到另一台Nginx上，整个过程对下游调用方透明
            
    - 千万级QPS的Web应用框架图：

      <img width="743" alt="image" src="https://user-images.githubusercontent.com/46510621/156917992-b670530e-c95c-4f51-971c-2a6b36f3a3e4.png">

      - 对于千万级QPS的Web应用，除了服务层的独立Tomcat或Spring Cloud微服务节点需要不断的横向扩展外，还需要进行以下两大增强：
        - 引入LVS(Linux Virtual Server)负载均衡层，进行请求分发和接入层的负载均衡
          - 该层通过LVS + KeepAlived组合模式达到高可用和负载均衡的目的
            - 使用至少两台LVS组成一个集群，分别部署KeepAlived，设置成相同的虚拟IP供下游访问。KeepAlived对LVS负载调度器实现健康监控、热备切换
            - 在LVS系统上可以配置多个接入层Nginx服务器集群，由LVS完成高速的请求分发和接入层的负载均衡
          - LVS的转发分为NAT模式和DR模式：
            - NAT(Network Address Translation)模式: 是一种外网和内网地址映射和网络地址转发的技术
            - DR模式为直接路由，请求由LVS接收，处理后由真实服务器直接返回给用户。一个请求过来时，LVS只需要将网络帧的MAC地址修改为某一台真实服务器(RS)的MAC，该包就会转发到相应的RS处理；当RS返回响应时，只要直接向客户端IP返回即可
          - 常使用直接路由方式进行负载均衡，数据在分发过程中不修改IP地址，只修改MAC地址
        - 引入DNS服务器的负载均衡，可以在域名下面添加多个IP，由DNS服务器进行多个IP之间的负载均衡，也可以按照就近原则为用户返回最近的服务器IP地址
        

## 高并发IO的底层

  - IO读写的基本原理：
    - 概述：
      - 操作系统将内存划分为两部分：一部分是内核空间(Kernel-Space)，另一部分是用户空间(User-Space)。在Linux系统中，内核模块运行在内核空间，对应的进程处于内核态；用户程序运行在用户空间，对应的进程处于用户态
      - 用户态进程必须通过系统调用向内核发出指令，完成调用系统资源的操作
    - IO读写流程：
      - 上层应用通过操作系统的read系统调用把数据从内核缓冲区复制到应用程序的进程缓冲区，通过操作系统的write系统调用把数据应用程序的进程缓冲区复制到操作系统的内核缓冲区。应用程序的IO操作实际上不是物理设备的读写，而是缓存的复制
    - Java客户端和服务端完成一次socket请求与响应的数据交换流程：
      - 客户端发送请求：Java客户端程序通过write系统调用将数据复制到内核缓冲区，Linux将内核缓冲区的请求数据通过客户端机器的网卡发送出去。在服务端，请求数据从接收网卡读到服务器机器的内核缓冲区
      - 服务端获取请求：Java服务端程序通过read系统调用从Linux内核缓冲区读取数据，再送入Java进程缓冲区
      - 服务端业务处理：Java服务端在自己的用户空间中完成客户端请求对应的业务处理
      - 服务端返回数据：Java服务端完成处理后，构建好的响应数据从用户缓冲区写入内核缓冲区，操作系统负责将内核缓冲区的数据发送出去
      - 发送给客户端：服务端Linux系统将内核缓冲区的数据写入网卡，网卡通过底层的通信协议将数据发送给目标客户端
  - 四种主要的IO模型：
    - 同步阻塞IO：
      - 同步阻塞IO指的是指的是用户空间主动发起，需要等待内核IO操作完成后才返回到用户空间的IO操作。在IO操作过程中，发起IO请求的用户进程处于阻塞状态
      - 阻塞IO的优缺点：
        - 优点：应用程序开发简单，在阻塞等待数据期间，用户线程挂起，基本不会占用CPU资源
        - 缺点：一般情况每个连接配备一个独立的线程，一个线程维护一个连接的IO操作。在高并发的应用场景下，阻塞IO需要大量的线程来维护大量的网络连接，内存、线程切换开销非常大z
    - 同步非阻塞IO：
      - 同步非阻塞IO指的是用户进程主动发起，不需要等待内核IO操作完成就能立即返回用户空间的IO操作。在IO操作过程中，发起IO请求的用户进程处于非阻塞状态
      - 应用程序的线程需要不断地进行IO系统调用，轮询数据是否已经准备好，如果没有准备好就继续轮询，直到完成IO系统调用为止
    - IO多路复用：
      - 在Linux中，select/epoll系统调用可以用于监视多个文件描述符，一旦某个描述符就绪（一般是内核缓冲区可读/可写），内核就能够将文件描述符的就绪状态返回给用户进程，用户空间根据文件描述符的就绪状态进行相应的IO系统调用
      - 采用IO多路复用模型可以避免同步非阻塞IO轮询等待的问题，通过select/epoll系统调用，单个应用程序的线程可以不断的轮询成百上千的socket连接的就绪状态并返回
      - Java的NIO组件在Linux系统上使用epoll系统调用实现的，使用的就是IO多路复用模型
    - 异步IO：
      - 用户线程通过系统调用向内核注册某个IO操作，内核在整个IO操作（包括数据准备、数据复制）完成后通知用户程序，用户执行后续的业务操作。在整个内核的数据处理过程，用户程序都不需要阻塞

## Java NIO

  - 概述：
    - Java New IO类库，简称为Java NIO
    - Java NIO类库包含三个核心组件：
      - Channel(通道)
        - 在NIO中，一个网络连接使用一个通道表示，所有IO操作都是通过连接通道完成
        - 一个通道类似于OIO中两个流的结合体，既可以从通道读取数据，也可以向通道写入数据
      - Buffer(缓冲区)
        - 应用程序与通道的交互是进行数据的读取和写入。通道的读取是将数据从通道读取到缓冲区；通道的写入是将数据从缓冲区写入通道中
      - Selector(选择器)
        - 选择器可以理解为一个IO事件的监听和查询器，通过选择器，一个线程可以查询多个通道的IO事件的就绪状态
        - IO多路复用编程的第一步是把通道注册到选择器中，第二步是通过选择器提供的事件查询(select)方法来查询这些注册的通道是否有已经就绪的IO事件
    - NIO和OIO(Old IO)区别：
      - OIO是面向流的，NIO是面向缓冲区的
      - OIO的操作是阻塞的，NIO是非阻塞的
      - OIO没有选择器的概念，NIO有
  
### NIO Buffer

  - NIO Buffer本质上是一个内存块，既可以写入数据，也可以读取数据
  - 为了记录读写的状态和位置，Buffer类额外提供了一些重要的属性：capacity(容量)，position(读写位置)和limit(读写的限制)
    - capacity: 可以容纳的最大数据量，在缓冲区创建时设置并且不能改变
    - limit: 读写的限制，缓冲区中当前的数据量
    - position: 读写位置，缓冲区下一个要被读或写元素的索引
    - mark: 调用mark()设置mark=position，再调用reset()让position恢复到mark标记的位置，即position=mark
  - NIO Buffer的重要方法：
    - flip(): 将写模式翻转成读模式
    - clear(): 将缓冲区转换为写模式，作用是：
      - 将position清零
      - limit设置为capacity最大容量值
    - rewind(): 已经读完的数据，可以调用rewind()再读一遍
  - 使用Buffer类的基本步骤：
    - 使用创建子类实例对象的allocate()创建一个Buffer类的实例对象
    - 调用put()将数据写入缓冲区
    - 写入完成后，在开始读取数据前调用Buffer.flip()，将缓冲区转换为读模式
    - 调用get()可以从缓冲区读取数据
    - 读取完成后，调用Buffer.clear()或Buffer.compact()，将缓冲区转换为写模式，可以继续写入

### NIO Channel
    
  - FileChannel: 
    - 概述：
      - 文件通道，既可以从文件中读取数据，也可以将数据写入文件中
      - FileChannel为阻塞模式
    - 读取FileChannel：
      - channel.read(buf)读取通道的数据时，对于通道是读模式，对于缓冲区是写入数据，这是ByteBuffer缓冲区处于写模式
    - 写入FileChannel：
      - write(buf)作用是从ByteBuffer缓冲区中读取数据，然后写入通道，返回值是写入成功的字节数
    - 关闭通道：
      - channel.close()
    - 强制刷新到磁盘：
      - channel.force(true)
  - SocketChannel:
    - 概述：
      - 网络连接的通道，一个是SocketChannel，负责连接的数据传输；另一个是ServerSocketChannel，负责连接的监听
      - ServerSocketChannel仅应用于服务端，SocketChannel同时处于服务端和客户端。对于一个连接，两端都有一个负责传输的SocketChannel
    - 模式设置：
      - socketChannel.configureBlocking(false)设置为非阻塞模式
      - socketChannel.configureBlocking(true)设置为阻塞模式
  - DatagramChannel:
    - 概述：
      - 处理UDP的数据传输

### NIO Selector

  - 概述：
    - 选择器的使命是完成IO的多路复用，主要工作是通道的注册、监听、事件查询。一个通道代表一条连接通路，通过选择器可以同时监控多个通道的IO状况
    - 在NIO编程中，一般是一个单线程处理一个选择器，通过选择器，一个单线程可以处理成百上千的的通道
    - 调用通道的Channel.register(Selector sel, int ops)可以将通道实例注册到选择器中
  - 可供选择器监控的通道IO事件类型包括：这里的IO事件不是对通道的的IO操作，而是通道处于某个IO操作的就绪状态
    - 可读：SelectionKey.OP_READ
    - 可写：SelectionKey.OP_WRITE
    - 连接：SelectionKey.OP_CONNECT
    - 接收：SelectionKey.OP_ACCEPT
  - SelectableChannel: 一个通道如果能被选择，必须继承SelectableChannel类
  - SelectionKey就是那些被选择器选中的IO事件
  - 选择器使用流程：
    - 获取选择器实例
      ```
      Selector selector = Selector.open();
      ```
    - 将通道注册到选择器实例
      ```
      serverSocketChannel.bind(new InetSocketAddress(11111));
      serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
      ```
    - 选出感兴趣的IO就绪事件（选择键集合）：通过Selector的select()选出已经注册的、就绪的IO事件，并且保存到SelectionKey集合中

## Reactor模式

  - 概述：
    - Reactor模式由Reactor线程、Handlers处理器两个角色组成：
      - Reactor线程职责：负责查询IO事件，当检测到一个IO事件时将其发送给相应的Handler处理器去处理
      - Handlers处理器职责：非阻塞的执行业务处理逻辑，与IO事件绑定，负责IO事件的处理，完成连接建立、通道的读取、处理业务逻辑、负责将结果写到通道等

## Netty核心原理

  - 概述：
    - Netty是一个Java NIO客户端/服务端框架，是为快速开发可维护的高性能、高可用的网络服务器和客户端程序而提供的异步事件驱动基础框架和工具
    - Netty的出站可以理解为从Handler传递到Channel的操作，入站为从Channel传递到Handler的操作
  - Netty的通道类型：
    - NioSocketChannel: 异步非阻塞TCP Socket传输通道
    - NioServerSocketChannel: 异步非阻塞TCP Socket服务端监听通道
    - NioDatagramChannel: 异步非阻塞UDP传输通道
    - OioSocketChannel: 同步阻塞TCP Socket传输通道
    - OioServerSocketChannel: 同步阻塞TCP Socket服务端监听通道
    - OioDatagramChannel: 同步阻塞UDP传输通道
  - Netty的Reactor模式：
    - 在Reactor模式中，一个反应器会由一个事件处理线程负责事件查询和分发。该线程不断进行轮询，通过Selector选择器不断查询注册过的IO事件（选择键）。如果查询到IO事件，就分发给Handler业务处理器
    - Netty中的反应器组件有多个实现类，与其通道类型相匹配。对于NioSocketChannel, 反应器类型为NioEventLoop。一个EventLoop反应器和NettyChannel通道是一对多的关系
  - Netty的Handler:




## HTTP原理与Web框架

### HTTP协议

  - 概述：
    - HTTP(Hyper Text Transfer Protocol)超文本传输协议：是一个基于请求与响应、无状态的应用层的协议，所有的WWW文件都必须遵守这个标准
    - HTTP主要特点：
      - 支持客户端/服务端模式
      - 通信速度快
      - 灵活：HTTP允许传输任意类型的数据对象，数据的类型由Content-Type标记
      - 无连接：每次连接只处理一个请求，服务器处理完客户的请求并收到客户的应答后断开连接
    - 通过HTTP协议请求的的资源有统一的资源标识符(URI)，在Java编程中，更多使用URI的一个子类URL(统一资源定位符)
  - HTTP请求报文：
    - HTTP请求由三部分组成：请求行、请求头、请求体
    - HTTP Method: Restful接口常用到GET、POST、PUT、DELETE
  - HTTP中GET和POST区别：
    - 请求数据的位置不同：
      - GET请求的数据附在URL之后，以?分隔，多个参数之间用&连接
      - POST请求提交的数据将被放置在请求体中
    - 传输数据的大小不同：
      - GET请求对URL长度有限制，而传输数据会受到URL长度限制
      - POST请求理论上传输数据不受限
    - 传输数据的安全性不同：
      - POST的安全性比GET高，通过GET提交数据，用户名和密码将明文出现在URL上
  - Netty服务端处理分包问题的策略：
    - 定长分包：接收端按照固定长度进行数据包分割，发送端按照固定长度发送数据包
    - 长度域分包：比如使用LengthFieldBasedFrameDecoder长度域解码器在接收端分包，在发送端先发送4个字节表示消息的长度，紧接着发送消息的内容
    - 分割符分割：比如使用LineBasedFrameDecoder解码器通过换行符分包
  





    
