## 常用工具

  - jps: JVM Process Status Tool
    - 它的功能也和ps命令类似: 可以列出正在运行的虚拟机进程，并显示虚拟机执行主类名称以及这些进程的本地虚拟机唯一 ID
    - jps工具主要选项:
      
      <img width="1015" alt="image" src="https://user-images.githubusercontent.com/46510621/159113647-a74f2750-c573-4b32-a3cc-7a45611f3441.png">

  - jstat：JVM Statistics Monitoring Tool
    - 用于监视虚拟机各种运行状态信息的命令行工具。它可以显示本地或者远程虚拟机进程中的类加载、内存、垃圾收集、即时编译等运行时数据
    - jstat工具主要选项：

      <img width="1002" alt="image" src="https://user-images.githubusercontent.com/46510621/159113768-ac488810-547a-44f4-a49b-105d9415f9c2.png">

  - jinfo：
    - 实时查看和调整虚拟机各项参数，jinfo还可以使用-sysprops选项把虚拟机进程的System.getProperties()的内容打印出来
    - 在运行期修改部分参数值的能力(-flag name=value在运行期修改一部分运行期可写的虚拟机参数值)
  - jmap：
    - 用于生成堆转储快照(一般称为heapdump或dump文件)
    - 还可以查询finalize执行队列、Java堆和方法区的详细信息，如空间使用率、当前用的是哪种收集器等
    - jmap工具主要选项：

      <img width="1006" alt="image" src="https://user-images.githubusercontent.com/46510621/159115317-d9bd8f3f-7fec-43db-b0e0-ee77aceb3d4e.png">

  - jhat:
    - 与jmap搭配使用，来分析jmap生成的堆转储快照
  - jstack:
    - 概述：
      - 用于生成虚拟机当前时刻的线程快照(一般称为threaddump或者javacore文件)
      - 线程快照就是当前虚拟机内每一条线程正在执行的方法堆栈的集合，生成线程快照通常是定位线程出现长时间停顿的原因，如线程间死锁、死循环、请求外部资源导致的长时间挂起等
    - 语法格式：
      ```
      jstack <pid>
      ```
    - 输出信息：
      - tid: 线程实例在JVM进程中的id
      - nid: 线程实例在操作系统中的对应底层线程id
      - prio: JVM进程中的线程优先级
      - os_prio: 底层线程的优先级
      - 线程状态
