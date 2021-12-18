# Java锁

## 基本概念

  - 线程安全：
    - 当多个线程并发访问某个Java对象时，无论系统如何调度这些线程，无论这些线程如何交替操作，这个对象都能表现出一致的、正确的行为，那么对这个对象的操作是线程安全的
    - 自增运算符不是线程安全的，它是一个复合操作，至少包括3个JVM指令：内存取值，寄存器加1，存值到内存。两个或两个以上的原子操作合在一起进行操作就不具备原子性
  - 临界区资源：
    - 在并发情况下，临界区资源是受保护的对象
    - 临界区代码段是每个线程中访问临界资源的那段代码，多个线程必须互斥地对临界区资源进行访问
    - 可以使用synchronized关键字同步代码块，对临界区代码段进行排他性保护

## synchronized关键字

  - 每个Java对象都隐含有一把锁，称为Java内置锁（或对象锁）。使用synchronized(syncObject)调用获取syncObject的内置锁
  - synchronized方法是一种粗粒度的并发控制，某一时刻只能有一个线程执行该synchronized方法；而synchronized代码块是一种细粒度的并发控制，处于synchronized块之外的其他代码是可以被多个线程并发访问的。
  - synchronized方法实际等同于一个synchronized代码块，这个代码块包含同步方法中的所有语句，然后在synchronized代码块的括号中传入this关键字，使用this对象锁作为进入临界区的同步锁
  - 使用synchronized修饰static方法时，synchronized的同步锁不是普通的Object对象的监视锁，而是类对应的Class对象的监视锁

## 生产者-消费者问题

  - 生产者-消费者问题也称有限缓冲问题，关键是：
    - 保证生产者不会在缓冲区满时加入数据，消费者不会在缓冲区空时消费数据
    - 保证生产者加入数据，消费者消费数据过程中，不会产生错误的数据和行为

## Java对象锁结构

  - Java对象结构包括三部分：对象头、对象体和对齐字节
    - 对象头：
      - Mark Word(标记字)：用于存储自身运行时的数据，例如：GC标志位，哈希码，锁状态等
      - Class Pointer(类对象指针)：用于存放方法区Class对象的地址，JVM通过这个指针来确定这个对象是哪个类的实例
      - Array Length(数组长度)：如果对象是Java数组，此字段用于记录数组长度
    - 对象体：包含对象的实例变量
    - 对齐字节：用于保证Java对象所占内存字节数为8的倍数
  - Java内置锁：
    - 4种状态，级别由低到高依次为：无锁、偏向锁、轻量级锁和重量级锁，内置锁可以升级但不能降级
      - 无锁：没有任何线程竞争
      - 偏向锁：
        - 一段同步代码一直被同一个线程所访问，那么该线程自动获取锁，降低获取锁的代价。如果内置锁处于偏向锁状态，当有一个线程来竞争锁时，先用偏向锁，表示内置锁偏爱这个线程，这个线程要执行该锁关联的同步代码时，不需要再做任何检查和切换。偏向锁在竞争不激烈的情况下效率比较高
        - 当有两个线程竞争这个锁对象时，锁会升级为轻量级锁，两个线程公平竞争，哪个线程先占有锁对象，锁对象的Mark Word就指向那个线程的栈帧中的锁记录。企图抢占的线程会通过自旋的形式尝试获取锁，不会阻塞抢锁线程。
        - 自旋原理：
          - 如果持有锁的线程在很短的时间内释放锁资源，等待竞争锁的线程就不需要进行内核态和用户态之间的切换进入阻塞挂起状态，只需要等一等（自旋），等待有锁的线程释放锁后即可获取锁。
          - 但是线程自旋是消耗CPU的，如果一直获取不到锁，那么线程就是一直占用CPU做无用功。JDK 1.6之后引入了适应性自旋锁，自旋的时间不是固定的，而是由前一次在同一个锁上的自旋时间以及锁的拥有者的状态来决定相应地，如果自旋成功了，下次自旋的次数增加，反之次数减少。
          - 如果持有锁的线程执行时间超过自旋等待的最大时间仍没有释放锁，争用线程会停止自旋进入阻塞状态，该锁膨胀为重量级锁
        - 偏向锁的缺点：如果锁对象经常被多个线程竞争，偏向锁就是多余的，并且撤销的过程有一些性能开销
        - 偏向锁加锁过程：新线程需要判断内置锁对象的Mark Word中的线程ID是否是自己的ID，如果是就直接使用这个锁；如果不是就使用CAS交换，新线程将自己的ID交换到内置锁的Mark Word中，如果交换成功就加索成功
      - 轻量级锁：
        - 引入轻量级锁的目的是在多线程竞争不激烈的情况下，通过CAS机制竞争减少重量级锁产生的性能损耗
        - 轻量级锁的执行过程：
          - JVM首先在抢锁线程的栈帧中建立一个锁记录(Lock Record)，用于存储对象目前的Mark Word
          - 抢锁线程使用CAS自旋操作，尝试将内置锁对象的Mark Word中的ptr_to_lock_record(锁记录指针)更新为抢锁线程栈帧中锁记录的地址，如果更新成功，这个线程就拥有了该锁
        - 轻量级锁主要有两种：普通自旋锁，自适应自旋锁
          - 普通自旋锁：当有线程来竞争锁时，抢锁线程原地循环等待，不会被阻塞，直到占用锁的线程释放锁之后，抢锁线程才可以获得锁。默认情况下自旋的次数是10 
          - 自适应自旋锁：等待线程的循环次数不是固定的，自旋次数由上一次在同一个锁的自旋时间以及锁的拥有者的状态决定
        - 在竞争激烈的情况下，轻量级锁会膨胀为基于操作系统互斥锁实现的重量级锁
      - 重量级锁：
        - JVM中每个对象会有一个监视器，监视器和对象一起创建和销毁。监视器的作用是保证同一时间只有一个线程可以访问被保护的临界区代码块
        - 在Hotspot虚拟机中，监视器由ObjectMonitor实现，包含WaitSet、Cxq、EntryList三个队列：
          - Cxq: 竞争队列，所有请求锁的线程首先被放在这个队列中
          - EntryList: Cxq中有资格成为候选资源的线程被移动到EntryList中
          - WaitSet: 某个拥有ObjectMonitor的线程调用Object.wait()方法后将被阻塞，然后被放到这个队列
        - 进程从用户态到内核态切换的三种方式：硬件中断，系统调用，异常
    - 64位Mark Word结构：
      ![image](https://user-images.githubusercontent.com/46510621/145675162-8dd153e8-6a29-4665-9129-f9bba0056cce.png)
    - 锁状态标志位：
      ![image](https://user-images.githubusercontent.com/46510621/145675214-1c800366-dda1-4f89-a3d9-d4506acbda97.png)
  - 大小端问题：
    - 大端模式是指数据的高字节保存在内存的低地址中，而数据的低字节保存在内存的高地址中。所有网络协议都是采用大端模式传输数据
    - 小端模式是指数据的高字节保存在内存的高地址中，而数据的低字节保存在内存的低地址中。小端模式是处理器的主流字节存放方式，JVM采用这种方式存放字节

## CAS

  - 操作系统层面的CAS是一条CPU的原子指令（cmpxchg指令）
  - Unsafe提供的CAS方法包含4个操作数：字段所在的对象、字段内存位置、预期原值和新值。在执行Unsafe的CAS方法时，首先将内存位置的值与预期值比较，如果相匹配则CPU会自动将内存位置的值更新为新值，并返回true，否则不做任何操作并返回false
  - 使用CAS进行无锁编程的步骤是：
    - 获得字段的预期值
    - 计算出需要替换的新值
    - 通过CAS将新值放在字段的内存地址上，如果CAS失败就重复以上两步，直到CAS成功，也称为CAS自旋

## JUC原子类

  - JUC原子类分为4类：基本原子类、数组原子类、原子引用类和字段更新原子类
    - 基本原子类：
      - AtomicInteger: 整型原子类
      - AtomicLong: 长整型原子类
      - AtomicBoolean: 布尔型原子类
      - 通过CAS自旋 + volatile实现线程安全，CAS用于保障变量操作的原子性，volatile用于保证变量的可见性
    - 数组原子类：
      - AtomicIntegerArray: 整型数组原子类
      - AtomicLongArray: 长整型数组原子类
      - AtomicReferenceArray: 引用类型数组原子类
    - 引用原子类：
      - AtomicReference: 引用类型原子类
        - 如果需要同时保障对多个变量操作的原子性，可以把多个变量放在一个对象中进行操作
      - AtomicMarkableReference: 带有更新标记位的原子引用类型
      - AtomicStampedReference: 带有更新版本号的原子引用类型
    - 字段更新原子类：
      - AtomicIntegerFieldUpdater: 原子更新整型字段的更新器
      - AtomicLongFieldUpdater: 原子更新长整型字段的更新器
      - AtomicReferenceFieldUpdater: 原子更新引用类型字段的更新器
  - ABA问题：
    - 乐观锁的实现版本使用版本号解决ABA问题。乐观锁每次在执行数据的修改操作时都会带上一个版本号，版本号和数据的版本号一致就可以执行修改操作并对版本号执行+1操作，否则执行失败
    - AtomicStampedReference在CAS的基础上增加了一个Stamp，使用这个时间戳判断数据是否发生变化，进行实效性检验
    - AtomicMarkableReference只关心是否修改过，其标记属性mark是boolean类型，记录值是否修改过
  - LongAdder: 以空间换时间的方式提升高并发场景下CAS操作的性能
    - LongAdder的核心思想是热点分离，将value值分离成一个数组，当多线程访问时，通过Hash算法将线程映射到数组的一个元素进行操作，而获得最终的value结果时，则将数组的元素求和

## 可见性和有序性

  - 原子性、可见性、有序性是并发编程的三大问题：
    - 可见性：
      - 一个线程对共享变量的修改，另一个线程能够立刻可见，这样的共享变量称为具备内存可见性
      - 要解决多线程的内存可见性问题，所有线程都必须将共享变量刷新到主存，可以使用volatile修饰共享变量
      - volatile关键字可以保证共享变量的主存可见性，可以将共享变量的改动值立即刷新回主存。底层实现通过lock前缀指令，该指令有三个功能：
        - 将当前CPU缓存行的数据立即写回系统主存
        - 引起其他CPU中缓存了该内存地址的数据无效
        - 禁止指令重排
    - 有序性：
      - 有序性是指程序按照代码的先后顺序执行，如果程序执行的顺序与代码的先后顺序不同，并导致了错误的结果，即发生了有序性问题
      - 指令重排序：
        - CPU为了提高程序运行效率，可能会对输入代码进行优化，它不保证程序中各语句的执行顺序同代码中的先后顺序一致，但是它会保证程序最终的执行结果和代码顺序执行的结果是一致的
        - 指令重排序不会影响单个线程的执行，但是会影响多个线程并发执行的正确性
      - 内存屏障：
        - 是一系列CPU指令，作用是保证特定的执行顺序，保障并发执行的有序性。在编译器和CPU都进行指令的重排时，可以通过在指令间插入一个内存屏障指令，禁止在该指令前后执行指令重排序
  - CPU物理缓存结构：
    - 按照数据读取顺序与CPU内核结合的紧密程度，CPU高速缓存有L1、L2和L3高速缓存。每一级高速缓存中存储的数据都是下一级缓存的一部分，越靠近CPU的高速缓存读取越快，容量也越小
    - L1、L2缓存都只能被一个单独的CPU内核使用，L3缓存可以被同一个CPU芯片上的所有CPU内核共享，而主存可以由系统中的所有CPU共享
  - 硬件层的MESI协议：
    - 每个CPU的处理过程为：先将计算需要用到的数据缓存在CPU的高速缓存中，在CPU进行计算时，直接从高速缓存中读取数据并且在计算完成后写回高速缓存中。整个计算完成后，再把高速缓存中的数据同步到主存
    - 为了解决内存的可见性问题，CPU提供了两种解决方法：总线锁和缓存锁
      - 总线锁：
        - 在多CPU系统中，当其中一个CPU要对共享主存操作时，在总线上发出一个LOCK信号，使得其他CPU无法通过总线访问共享主存的数据，总线锁把CPU和主存之间的通信锁住
      - 缓存锁：
        - 缓存一致性机制就是当某CPU对高速缓存中的数据进行操作之后，通知其他CPU放弃存储在它们内部的缓存数据，或者从主存重新读取
        - 每个CPU通过嗅探在总线上传播的数据来检查自己的高速缓存中的值是否过期，当CPU发现自己缓存的数据对应的主存地址被修改时，会将当前数据置为无效状态，当CPU对这个数据进行修改操作时，会重新从主存中把数据读到CPU的高速缓存中

## Java内存模型

  - Java内存模型（JMM）有以下规定：
    - 所有变量存储在主存中
    - 每个线程有自己的工作内存，且对变量的操作都是在工作内存中进行
    - 不同线程之间无法直接访问彼此工作内存中的变量，要想访问只能通过主存来传递
  - JMM解决有序性问题：
    - JMM内存屏障主要有Load和Store两类：
      - Load Barrier(读屏障)：在读指令前插入读屏障，可以让高速缓存中的数据失效，重新从主存加载数据
      - Store Barrier(写屏障)：在写指令之后插入写屏障，让写入缓存中的最新数据写回主存

## JUC显式锁

  - Lock接口是JUC显式锁的一个抽象，主要抽象方法包括：
    - lock(): 阻塞抢锁
    - lockInterruptibly(): 可中断抢锁，当前线程在抢锁过程中可以响应中断信号
    - tryLock(): 尝试抢锁，线程为非阻塞模式，在调用tryLock()方法后立即返回。若抢锁成功则返回true，失败则返回false
    - tryLock(long time, TimeUnit unit): 限时抢锁，达到超时时间返回false
    - unlock(): 释放锁
    - newCondition(): 获取与显式锁绑定的Condition对象，用于等待-通知方式的线程间通信
  - 显式锁相比内置锁的优势：
    - 可中断获取锁：lockInterruptibly()
    - 可非阻塞获取锁：tryLock()
    - 可限时抢锁：tryLock(long time, TimeUnit unit)
  - 可重入锁ReentrantLock
    - ReentrantLock是显式锁的实现类，是一个可重入的独占锁
      - 可重入的含义：支持一个线程对资源的重复加锁，即一个线程可以多次进入同一个锁同步的临界区代码块
      - 独占的含义：同一时刻只能有一个线程获取到锁，其他线程只能等待拥有锁的线程释放锁
  - Condition接口的主要方法：等效于Object的wait, notify
    - await(): 当前线程加入await等待队列
    - signal(): 唤醒一个在await等待队列中的线程
    - signalAll(): 唤醒在await等待队列中的所有线程
    - await(long time, TimeUnit unit): 限时等待
  - 显式锁几种分类：
    - 可重入锁和不可重入锁
      - 可重入锁指的是一个线程可以多次抢占同一个锁
    - 悲观锁和乐观锁
      - 悲观锁存在的问题：
        - 在多线程竞争下，加锁和释放锁导致较多的上下文切换和调度延时，引起性能问题
        - 一个线程持有锁后，其他抢占的线程会挂起
      - 乐观锁操作主要有两个步骤：冲突检测，数据更新。基于CAS原子操作
      - 自旋锁的问题：在竞争激烈的场景下，如果某个线程持有锁的时间太长，会导致其他空自旋的线程耗尽CPU资源。如果大量的线程进行空自旋，可能导致硬件层面的总线风暴
        - 为了保障缓存一致性，不同的内核通过总线通信，所产生的流量被称为缓存一致性流量，因为总线被设计为固定的通信能力，如果缓存一致性流量过大，总线成为瓶颈，即总线风暴
    - 公平锁和非公平锁
      - 公平锁指不同的线程抢占锁的机会平等，抢占的顺序是FIFO
      - 非公平锁指多个线程获取锁的顺序不一定是申请锁的顺序。默认情况ReentrantLock是非公平锁。非公平锁的优点是吞吐量比公平锁大，缺点是导致线程优先级反转或线程饥饿
    - 共享锁和独占锁
      - synchronized内置锁和ReentrantLock都是独占锁
      - JUC中共享锁包括：Semaphore(信号量)，ReadLock(读锁)，CountDownLatch(倒数闩)
        - CountDownLatch: 可以指定一个计数值，在并发环境下由线程进行减一操作，当计数值变为0，被await方法阻塞的线程将会唤醒。通过CountDownLatch可以实现线程间的计数同步
    - 可中断锁和不可中断锁
    - 读写锁：
      - 读锁是共享锁，写锁是独占锁
      - 锁升级是指读锁升级为写锁，锁降级指写锁降级为读锁
      - ReentrantReadWriteLock只支持写锁降级为读锁，不支持读锁升级为写锁
      - StampedLock(印戳锁)是对ReentrantReadWriteLock的一种改进：在没有写只有读的场景下，StampedLock支持不用加读锁而是直接进行读操作，只有在发生过写操作后，再加读锁才能进行读操作
    
## AQS抽象同步器

  - JUC并发包使用队列削峰的方案解决CAS的性能问题，并提供了基于双向队列的削峰基类-AbstractQueuedSynchronizer(抽象同步器类)
  - AQS队列内部维护的是一个FIFO的双向链表，当线程争抢锁失败后会封装成节点加入AQS队列，当获取锁的线程释放锁后会唤醒队列中的一个节点
  - JUC显式锁和AQS的关系：
    - AQS是JUC包的一个同步器，它实现了锁的基本抽象功能，支持独占锁和共享锁两种方式
    - 显式锁如ReentrantLock, ReentrantReadWriteLock, 线程同步工具如Semaphore, 异步回调工具如FutureTask等内部使用AQS作为等待队列
  - AQS是基于模板模式设计的，模板模式的关键是：父类提供框架性的公共逻辑，子类提供个性化的定制逻辑（召回函数）
    - 在模板模式中，由抽象类定义模板方法和钩子方法，模板方法定义一套业务算法框架，算法框架中的某些步骤由钩子方法负责完成。具体的子类需要重写钩子方法
    - 分离变与不变是软件设计的一个原则。模板模式将不变的部分封装在基类的模板方法中，而将变化的部分通过钩子方法进行封装，交给子类提供具体的实现
  - AQS需要重写的钩子方法：
    - tryAcquire(int): 独占锁钩子，尝试获取资源，如果成功则返回true，失败返回false
    - tryRelease(int): 独占锁钩子，尝试释放资源，如果成功则返回true，失败返回false
    - tryAcquireShared(int): 共享锁钩子，尝试获取资源，负数表示失败，0表示成功但没有剩余可用资源，正数表示成功且由剩余资源
    - tryReleaseShared(int): 共享锁钩子，尝试释放资源，如果成功则返回true，失败返回false
    - isHeldExclusively(): 独占锁钩子，判断该线程是否正在独占资源，只有用到condition条件队列时才需要实现它
  - AQS节点的入队和出队
    - 节点入队，分为2种情况：
      - 如果AQS的队列非空，新节点入队的插入位置在队列的尾部，并且通过CAS的方式插入，插入之后AQS的tail指针指向新的尾节点
      - 如果AQS的队列为空，新节点入队时，AQS通过CAS方式将新节点设置为头节点head，并将tail指针指向新节点
    - 节点出队：
      - acquireQueued()不断在前驱节点上自旋，如果前驱节点是头节点并且当前线程使用钩子方法tryAcquire(arg)获得了锁，就移除头节点，将当前节点设置为头节点

## JUC容器类

  - JUC高并发容器是基于非阻塞算法（或无锁编程算法）实现的容器类，无锁编程通过CAS + Volatile组合实现，通过CAS保证操作的原子性，通过Volatile保证变量内存的可见性
  - JUC包中提供了List, Set, Queue, Map各种类型的高并发容器：
    - List:
      - CopyOnWriteArrayList: 
        - 实现了List接口，线程安全的ArrayList，在读多写少的场景性能远高于ArrayList的同步包装容器
        - 写时复制（Copy On Write）核心思想是：如果有多个访问器访问同一个资源，它们会共同获取相同的指针指向相同的资源，只要有一个访问器需要修改该资源，系统就会复制一个副本给该访问器，而其他访问器资源不变，修改的过程对其他访问器是透明的。修改之后再将原来的指针指向新的资源副本，原来的资源被回收
    - Set:
      - CopyOnWriteArraySet:
        - 继承AbstractSet类，对应HashSet，核心操作是基于CopyOnWriteArrayList实现的
      - ConcurrentSkipListSet:
        - 继承AbstractSet类，对应TreeSet，基于ConcurrentSkipListMap实现
    - Map:
      - ConcurrentHashMap:
        - 对应HashMap，但是HashMap是不安全的。HashTable是线程安全的，但效率低。
        - HashMap和HashTable区别：
          - HashTable不允许key和value为null
          - HashTable使用synchronized保证线程安全，操作时对Hash表进行锁定
        - JDK 1.7版本的ConcurrentHashMap锁机制基于粒度更小的分段锁，将key分成一个一个小的Segment存储，然后给每一段数据配一段锁，当一个线程占用锁访问其中一段数据时，其他段的数据也能被其他线程访问，从而实现并发访问
        - JDK 1.8版本中ConcurrentHashMap采用数组 + 链表/红黑树的组合方式，利用CAS + Synchronized保证并发更新的安全
          - 当桶的节点数超过一定阈值（默认64）时，JDK 1.8将链表结构自动转换成红黑树结构，可以理解为将链式桶转换成树状桶
          - 引入红黑树的原因是：链表查询的时间复杂度是O(n)，红黑树查询的时间复杂度为O(log(n))，在节点较多的情况下使用红黑树可以提升性能
          - 核心原理：
            - 通过一个Node<K, V>数组table保存添加到哈希表中的桶，而在同一个Bucket位置通过链表或红黑树的形式来保存
            - 数组table是懒加载的，只有在第一次添加元素时才会初始化。第一次添加元素时默认初始长度是16，通过Hash值跟数组的长度决定放在数组的哪一个Bucket位置，如果出现在同一个位置，就先以链表的形式存储，在同一个位置的个数达到8之后，如果数组的长度小于64就会扩容数组。如果数组的长度大于64就会将该节点的链表转换成红黑树
      - ConcurrentSkipListMap:
        - 对应TreeMap，内部的Skip List(跳表)结构可以代替平衡树，默认是按照Key值升序
    - Queue:
      - ConcurrentLinkedQueue:
        - 基于链表实现的单向队列，按照FIFO对元素进行排序和操作
      - ConcurrentLinkedDeque:
        - 基于链表实现的双向队列，该队列不允许null元素，可以当作栈来使用，并高效支持并发环境
      - BlockingQueue:
        - 阻塞队列和普通队列区别：
          - 阻塞添加：当阻塞队列已满，队列会阻塞添加元素的线程，直到队列元素不满时，才重新唤醒线程执行元素添加操作
          - 阻塞删除：当队列元素为空时，删除队列元素的线程将被阻塞，直到队列不为空时，才重新唤醒删除线程

## 高并发设计模式

  - 线程安全的单例模式：
    - 饿汉式单例在类被加载时直接被初始化
      ```
      private static final Singleton1 single = new Singleton1();
      public static Singleton1 getInstance() return single;
      ```
    - 懒汉式单例在使用的时候才会对单例进行初始化
      ```
      public static Singleton2 getInstance() {
        if (instance == null) {
          instance = new Singleton2();
        }
        return insatnce;
      }
      ```
    - 使用内置锁保护懒汉式单例：
      ```
      public static synchronized Singleton3 getInstance() {
         if (instance == null) {
          instance = new Singleton3();
        }
        return insatnce;
      }
      ```
      - 问题是每次执行getInstance()都需要同步，在竞争激烈的情况下内置锁会升级为重量级锁，开销大，性能差
    - 双重检查锁单例模式：
      ```
      public static synchronized Singleton4 getInstance() {
         if (instance == null) {
          synchronized (Singleton4.class) {
            if (instance == null) {
              instance = new Singleton4();
            }
          }
        }
        return insatnce;
      }
      ```
      - 在多个线程竞争的情况下，可能同时不止一个线程通过了第一次检查，在第一个线程实例化单例对象释放锁之后，其他线程可能获取到锁进入临界区，实际上单例已经实例化。双重检查避免单例对象在多线程场景中反复初始化
  - Master-Worker模式：
    - 核心思想是任务的调度和执行分离。调度任务的角色是Master，执行任务的角色是Worker
    - Netty是基于Reactor模式的具体实现，体现了Master-Worker模式的思想。Netty的EventLoop对应Worker角色，EventLoopGroup轮询组对应Master角色
  - ForkJoin模式：
    - 分而治之就是把一个复杂的算法问题按一定的分解方法分为规模较小的若干部分，然后逐个解决，最后把各部分的解组成整个问题的解
    - ForkJoin模式中只有Worker角色，将大的任务分割成小的任务，一直到任务的规模足够小，可以使用很简单的、直接的方式完成
    - ForkJoin框架包含组件：
      - ForkJoinPool: 执行任务的线程池
      - ForkJoinWorkerThread: 执行任务的工作线程
      - ForkJoinTask: 用于ForkJoinPool的任务抽象类，实现了Future接口
      - RecursiveTask: 带返回结果的递归执行任务，是ForkJoinTask的子类
      - RecursiveAction: 不返回结果的递归执行任务，是ForkJoinTask的子类
    - 工作窃取算法：每个线程有一个双端队列，用于存放需要执行的任务，当自己的队列没有任务时，可以从其他线程的任务队列中获取一个任务继续执行
    - ForkJoin适合调度的任务为CPU密集型任务
  - 生产者-消费者模式：
    - 数据缓冲区的作用是使生产者和消费者解耦
  - Future模式：
    - 核心思想是异步调用，它不是立即返回需要的数据，而是返回一个契约（或异步任务），可以凭借这个契约（或异步任务）获取结果

## 异步回调模式

  - join: 线程A调用线程B的join()，那么线程A进入阻塞状态，直到线程B执行完成
  - FutureTask:
    - 使用FutureTask方式进行异步调用时，涉及的重要组件为FutureTask类和Callable接口
  - Guava的异步回调模式：
    - FutureCallBack是一个新增的接口，用来填写异步任务执行完成后的监听逻辑，有两个回调方法：
      - onSuccess(): 异步任务执行结果作为onSuccess方法的参数被传入
      - onFailure(): 异步任务抛出的异常作为onFailure方法的参数被传入
    - 和FutureTask异步调用的区别：
      - FutureTask是主动调用的模式，调用线程主动获得异步结果，在获取异步结果时处于阻塞状态，直到获取到异步线程的结果
      - Guava是异步回调模式，调用线程不会主动获得异步结果，而是准备好回调函数，并设置好回调钩子，回调函数的执行者是被调用线程，调用线程在执行完业务逻辑之后就结束了
  - CompletableFuture异步回调：
    - JDK 1.8引入，该类实现了Future和CompletionStage两个接口，该类的实例作为一个异步任务，可以在执行完成之后触发一些其他的异步任务，达到异步回调的效果
    - CompletionStage代表某个同步或异步计算的一个阶段，或者一系列异步任务的一个子任务
    - CompletableFuture定义了一组方法用于创建CompletionStage子任务（或阶段性任务），基础的方法包含：
      ```
      runAsync(Runnable runnable): 子任务包装一个Runnable实例，并调用ForkJoinPool.commonPool()线程池来执行
      runAsync(Runnable runnable, Executor executor): 子任务包装一个Runnable实例，并调用指定的executor线程池来执行
      supplyAsync(Supplier<U> supplier): 子任务包装一个Supplier实例，并调用ForkJoinPool.commonPool()线程池来执行
      supplyAsync(Supplier<U> supplier, Executor executor): 子任务包装一个Supplier实例，并调用指定的executor线程池来执行
      ```
    - 异步任务的串行执行：通过CompletionStage接口的thenApply(), thenAccept(), thenRun(), thenCompose()四个方法来实现
    - 异步任务的选择执行：
      - applyToEither(): 两个CompletionStage谁返回结果的速度快，applyToEither()就用这个CompletionStage的结果进行下一步的回调操作
    
   
