# Java设计模式

## SOLID设计原则

  - SOLID原则包括：单一职责原则、开闭原则、里氏替换原则、接口隔离原则和依赖倒转原则

### 单一职责原则

  - 单一职责原则：是以一种面向对象的设计原则，该原则指出软件模块应该只有一个被修改的理由。通俗的说，即一个类只负责一项职责。
    - 问题背景：类T负责两个不同的职责：职责P1，职责P2。当由于职责P1需求发生改变而需要修改类T时，有可能会导致原本运行正常的职责P2功能发生故障。每个更改的职责/理由都会增加新的依赖关系，使代码不那么健壮，更难以修改。
    - 解决方案：遵循单一职责原则。分别建立两个类T1、T2，使T1完成职责P1功能，T2完成职责P2功能。这样，当修改类T1时，不会使职责P2发生故障风险；同理，当修改T2时，也不会使职责P1发生故障风险。
  - 职责扩散：因为某种原因，职责P被分化为粒度更细的职责P1和P2。
  - 单一职责的优点：
    - 降低类的复杂度，一个类只负责一项职责
    - 提高类的可读性，提高系统的可维护性
    - 降低需求变更引起的风险，当修改一个功能时，可以显著降低对其他功能的影响

### 开闭原则

  - 开闭原则：模块、类和函数应该对扩展开放，对修改关闭
    - 问题背景：在软件的生命周期内，因为需求变化、升级和维护等原因需要对软件原有代码进行修改时，可能会引入错误，不仅要测试正在改变的功能，还要测试它负责的整个功能。这涉及许多额外的资源，也会带来额外的风险。
    - 解决方案：当软件需要变化时，尽量通过扩展软件实体的行为来实现变化，而不是通过修改已有的代码来实现变化。
  - 开闭原则抽象表达：用抽象构建框架，用实现扩展细节

### 里氏替换原则

  - 里氏替换原则：所有引用基类的地方必须能透明地使用其子类的对象。通俗理解：只要有父类出现的地方，都可以用子类来替代。
    - 问题背景：类A实现功能P1。现需要将功能P1进行扩展，扩展后的功能为P，其中P由原有功能P1与新功能P2组成。新功能P2由类A的子类B来完成，则子类B在完成新功能P2的同时，有可能会导致原有功能P1发生故障。
    - 解决方案：当使用继承时，遵循里氏替换原则。类B继承类A时，除添加新的方法完成新增功能P2外，尽量不要重写父类A的方法，也尽量不要重载父类A的方法。
  - 通俗理解：子类可以扩展父类的功能，但不能改变父类原有的功能。约束如下：
    - 子类可以实现父类的抽象方法，但不能覆盖父类的非抽象方法。
    - 子类中可以增加自己特有的方法。
    - 当子类的方法重载父类的方法时，方法的前置条件（即方法的形参）要比父类方法的输入参数更宽松。
    - 当子类的方法实现父类的抽象方法时，方法的后置条件（即方法的返回值）要比父类更严格。

### 接口隔离原则

  - 接口隔离原则：客户端不应该依赖于它所不需要的接口。类之间的依赖应该建立在最小的接口上面。何为最小的接口，即能够满足项目需求的相似功能作为一个接口，这样设计主要就是为了“高内聚”。
    - 问题背景：类A通过接口I依赖类B，类C通过接口I依赖类D，如果接口I对于类A和类C来说不是最小接口，则类B和类D必须去实现他们不需要的方法。
    - 解决方案：采用接口隔离原则，将接口I拆分为独立的几个接口，类A和类C分别与他们需要的接口建立依赖关系。
  - 接口隔离原则的含义：
    - 建立单一接口，尽量细化接口，接口中的方法尽量少
    - 接口是对外部设定的“契约”，通过分散定义多个接口，可以预防变更的扩散，提高系统的灵活性和可维护性。
  - 接口隔离和单一职责原则的区别：
    - 从原则约束的侧重点来说，接口隔离原则关注接口依赖程度的隔离，更加关注接口的“高内聚”；而单一职责原则更加注重的是接口职责的划分。
    - 接口隔离原则主要约束接口，针对抽象，偏向程序整体框架的构建；单一职责原则更加偏向对业务的约束，针对的是程序中的实现和细节。

### 依赖倒置原则

  - 高级模块不应该依赖低级模块，两者都应该依赖抽象。抽象不应该依赖于细节，细节应该依赖于抽象。依赖倒置原则的核心就是面向接口编程
    - 问题背景：类A直接依赖类B，假如要将类A改为依赖类C，则必须通过修改类A的代码来达成。这种场景下，类A一般是高层模块，负责复杂的业务逻辑；类B和类C是低层模块，负责基本的原子操作；假如修改类A，会给程序带来不必要的风险。
    - 解决方案：将类A修改为依赖接口I，类B和类C各自实现接口I，类A通过接口I间接与类B或者类C发生联系，则会大大降低修改类A的几率。
  - 依赖倒置原则基于这样一个事实：相对于细节的多变性，抽象的东西要稳定的多。以抽象为基础搭建起来的架构比以细节为基础搭建起来的架构要稳定的多。在java中，抽象指的是接口或者抽象类，细节就是具体的实现类，使用接口或者抽象类的目的是制定好规范和契约，而不去涉及任何具体的操作，把展现细节的任务交给他们的实现类去完成。

### 迪米特原则

  - 一个对象应该对其他对象保持最少的了解
    - 问题背景：类与类之间的关系越密切，耦合度越大，当一个类发生改变时，对另一个类的影响也越大
    - 解决方案：尽量降低类与类之间的耦合

## 创建型模式

### 单例模式（重要）

  - 定义：保证一个对象只能创建一个实例，提供了对实例的全局访问方法。
  - 单例模式的要素：
    - 私有的构造方法
    - 指向自己实例的私有静态引用
    - 以自己实例为返回值的静态的公有的方法
  - 饿汉式和懒汉式：
    - 饿汉式单例在单例类被加载时候，就实例化一个对象交给自己的引用
      - 示例：
        ```
        public class Singleton {
          private static Singleton singleton = new Singleton();
          private Singleton(){};
          public static Singleton getInstance() {
            return singleton;
          }
        }
        ```
    - 懒汉式在调用取得实例方法的时候才会实例化对象
      - 示例：
        ```
        public class Singleton {
          private static Singleton singleton;
          private Singleton(){};
          public static Singleton getInstance() {
            if (singleton == null) {
              singleton = new Singleton();
            }
            return singleton;
          }
        }        
        ```
  - 静态内部类实现单例：
    - 静态内部类初始化过程是懒加载的过程，在程序没有访问静态内部类中的成员时，程序是不会进行该类初始化，直到我们调用的时候，才会进行初始化，才会存在JVM的堆中
    - 示例：
      ```
      public class Singleton {    
         private Singleton() {}    
         private static class Holder {       
            static Singleton instance = new Singleton();    
         }
         public static Singleton getInstance() {  
          return Holder.instance;    
         }    
      }  
      ```
    - 使用示例：
      ```
      public class Experiment {
        private String expId;
        private Map<String, ExperimentGroup> groups;
        
        @Data
        public static class ExperimentGroup {
            private String groupId;
            private int start;
            private int end;
            private List<String> whiteList;
            private ExpConfig config;
        }
        
        public static class ExpConfig extends HashMap<String, Map<String, List<Strategy>>> {
        }
        
        public static class Strategy extends HashMap<String, Object> {}
      }
      ```
  - 单例模式优点：
    - 在内存中只有一个对象，节省内存空间
    - 避免频繁的创建销毁对象，可以提高性能
    - 避免对共享资源的多重占用
  - 单例模式使用场景：
    - 需要频繁实例化然后销毁的对象
    - 创建对象时耗时过多或者耗资源过多，但又经常用到的对象
    - 频繁访问数据库或文件的对象
    - 要求只有一个对象的场景
        
### 工厂模式

#### 简单工厂模式

  - 定义：用于实现逻辑的封装，并通过公共的接口提供对象的实例化服务，在添加新的类时只需要做少量的修改。该模式通过向工厂传递类型来指定要创建的对象
  - 示例：
    ```
    public class VehicleFactory {
      public enum VehicleType {
        Bike, Car, Truck;
      }
      public static Vehicle create(VehicleType type) {
        if (type.equals(VehicleType.Bike)) {
          return new Bike();
        }
        if (type.equals(VehicleType.Car)) {
          return new Car();
        }
        if (type.equals(VehicleType.Truck)) {
          return new Truck();
        }
      } 
    }
    ```
  - 使用场景：
    - 需要创建的对象较少。
    - 客户端不关心对象的创建过程。

#### 工厂方法模式（重要）

  - 定义：定义一个用于创建对象的接口，让子类决定实例化哪一个类，工厂方法使一个类的实例化延迟到其子类。
  - 示例：
    ```
    public interface Reader {
      void read();
    }
    public class JpgReader implements Reader {
      @Override
      public void read() {
          System.out.print("read jpg");
      }
    }
    public class PngReader implements Reader {
      @Override
      public void read() {
          System.out.print("read png");
      }
    }
    
    public interface ReaderFactory {
      Reader getReader();
    }
    public class JpgReaderFactory implements ReaderFactory {
      @Override
      public Reader getReader() {
          return new JpgReader();
      }
    }
    public class PngReaderFactory implements ReaderFactory {
      @Override
      public Reader getReader() {
          return new PngReader();
      }
    }
    ReaderFactory factory=new PngReaderFactory();
    Reader reader = factory.getReader();
    reader.read();
    ```
  - 工厂方法模式有四个要素：
    - 工厂接口：工厂方法模式的核心，与调用者直接交互用来提供产品。
    - 工厂实现：工厂实现决定如何实例化产品，是实现扩展的途径，需要有多少种产品，就需要有多少个具体的工厂实现
    - 产品接口：定义产品的规范，所有的产品实现都必须遵循产品接口定义的规范
    - 产品实现：实现产品接口的具体类，决定了产品在客户端中的具体行为
  - 使用场景：
    - 作为一种创建类模式，在任何需要生成复杂对象的地方，都可以使用工厂方法模式。
    - 由于工厂模式是依靠抽象架构的，它把实例化产品的任务交由实现类完成，扩展性比较好。当需要系统有比较好的扩展性时，可以考虑工厂模式，不同的产品用不同的实现工厂来组装。

#### 抽象工厂模式

  - 定义：为创建一组相关或相互依赖的对象提供一个接口，而且无需指定他们的具体类。
  - 工厂方法和抽象工厂的区别：
    - 工厂方法类中只有一个抽象方法，在不同的具体工厂类中分别实现抽象产品的实例化；而抽象工厂类中，每个抽象产品都有一个实例化方法。
  - 抽象工厂模式主要类：
    - AbstractFactory（抽象工厂类）：抽象类，用于声明创建不同类型产品的方法。它针对不同的抽象产品类都有对应的创建方法。
    - ConcreteFactory（具体工厂类）：具体类，用于实现抽象工厂基类中声明的方法。针对每个系列的产品都有一个对应的具体工厂类。
    - AbstracProduct（抽象产品类）：对象所需的基本接口或类。一簇相关的产品类由来自不同层级的相似产品类组成。ProductA1和ProductB1来自第一个类簇，由ConcreteFactory1实例化。ProductA2和ProductB2来自第二个类簇，由ConcreteFactory2实例化。
  - 抽象工厂模式优点：
    - 抽象工厂模式除了具有工厂方法模式的优点外，最主要的优点就是可以在类的内部对产品族进行约束。所谓的产品族，一般或多或少的都存在一定的关联，抽象工厂模式就可以在类内部对产品族的关联关系进行定义和描述，而不必专门引入一个新的类来进行管理。
  - 抽象工厂模式缺点：
    - 产品族的扩展将是一件十分费力的事情，假如产品族中需要增加一个新的产品，则几乎所有的工厂类都需要进行修改。
  - 使用场景：
    - 当需要创建的对象是一系列相互关联或相互依赖的产品族时，便可以使用抽象工厂模式。

### 建造者模式

  - 定义：将一个复杂对象的构建与它的表示分离，使得同样的构建过程可以创建不同的表示。
  - 建造者模式中包含以下类：
    - Product（产品类）：需要为其构建对象的类，是具有不同表现形式的复杂或复合对象。
    - Builder（抽象建造者类）：用于声明构建产品类的组成部分的抽象类或接口。它的作用是仅公开构建产品类的功能，隐藏产品类的其他功能。
    - ConcreteBuilder（具体建造者类）：用于实现抽象建造者类接口中声明的方法。除此之外，它还通过getResult方法返回构建好的产品类。
    - Director（导演类）：负责调用适当的建造者来组建产品。
  - 建造者模式的优点：
    - 首先，建造者模式的封装性很好。使用建造者模式可以有效的封装变化，一般产品类和建造者类是比较稳定的，将主要的业务逻辑封装在导演类中。
    - 其次，建造者模式很容易进行扩展。如果有新的需求，通过实现一个新的建造者类就可以完成

### 原型模式

  - 定义：用原型实例指定创建对象的种类，并通过拷贝这些原型创建新的对象。
  - 原型模式中包含以下类：
    - Prototype（抽象原型类）：
      - 实现Cloneable接口，在运行时通知虚拟机可以安全地在实现了此接口的类上使用clone方法。
      - 重写Object类中的clone方法，作用是返回对象的一个拷贝。
    - ConcretePrototype（具体原型类）：用于实现或扩展clone()方法的类。clone()方法必须要实现，因为它返回了类型的新实例。
  - 原型模式的优点：
    - 使用原型模式创建对象比直接new一个对象在性能上要好的多，因为Object类的clone方法是一个本地方法，它直接操作内存中的二进制流
    - 简化对象的创建，使得创建对象就像我们在编辑文档时的复制粘贴一样简单
  - 使用场景：
    - 需要重复地创建相似对象时可以考虑使用原型模式
  - 浅拷贝和深拷贝：
    - 如果在拷贝这个对象的时候，只对基本数据类型进行了拷贝，而对引用数据类型只是进行了引用的传递，而没有真实的创建一个新的对象，则认为是浅拷贝。
    - 反之，在对引用数据类型进行拷贝的时候，创建了一个新的对象，并且复制其内的成员变量，则认为是深拷贝。

## 行为型模式

### 责任链模式

  - 定义：使多个对象都有机会处理请求，从而避免了请求的发送者和接收者之间的耦合关系。将这些对象连成一条链，并沿着这条链传递该请求，直到有对象处理它为止。
  - 包含以下类：
    - Client（客户端）：它的职责是实例化一个处理器的链，然后在第一个对象中调用handleRequest方法。
    - Handler（处理器）：这是一个抽象类，提供给所有实际处理器进行继承。它拥有一个handleRequest方法，用来接收需要处理的请求。
    - ConcreteHandler（具体处理器）：这是一个实现了handleRequest方法的具体类。每一个具体处理器都维持一个引用，指向链中下一个具体处理器，需要检查它自身是否能处理这个请求，不能就将请求传递给链中的下一个具体处理器。
  - 使用场景：
    - 事件处理器：大部分图形用户界面框架使用责任链模式来处理事件。
    - 日志处理器：每一个处理器都要么记录一个基于其状态的特殊请求，要么将请求传送给下一个处理器。

### 命令模式

  - 定义：将一个请求封装成一个对象，从而让你使用不同的请求把客户端参数化，对请求排队或者记录请求日志，可以提供命令的撤销和恢复功能。简单说就是将请求封装成对象，将动作请求者和动作执行者解耦。
  - 包含以下类：
    - Command（命令类）：这是表示命令封装的抽象类。它声明了执行的抽象方法，该方法应该由所有具体命令实现。
    - ConcreteCommand（具体命令类）：这是命令类的实际实现。它必须执行命令并处理与每个具体命令相关的参数。它将命令委托给接收者。
    - Receiver（接收者）：这是负责执行与命令关联的操作的类。
    - Invoker（调用者）：这是触发命令的类。通常是外部事件，例如用户操作。
    - Client（客户端）：这是实例化具体命令对象及其接收者的实际类。
  - 命令模式优缺点：
    - 优点：
      - 封装性很好：每个命令都被封装起来，对于客户端来说，需要什么功能就去调用相应的命令，而无需知道命令具体是怎么执行的。
      - 扩展性很好：当增加新命令的时候，对命令类的编写一般不是从零开始的，有大量的接收者类和命令类可供调用，代码的复用性很好。
    - 缺点：命令很多时需要对应封装很多命令类
  - 使用场景：
    - 对于大多数请求-响应模式的功能，比较适合使用命令模式
    - 命令模式用于多线程应用程序。命令对象可以在后台以单独的线程执行。java.lang.Runnable是一个命令接口。

### 解释器模式

  - 定义：定义语法的表示以及该语法的对应解释。
  - 包含以下类：
    - Context（环境）：Context用于封装解释器的全局信息，所有具体的解释器均需访问Context。
    - AbstractExpression（抽象表达式）：一个抽象类或接口，声明执行的解释方法，由所有具体的解释器实现。
    - TerminalExpression（终结符表达式）：一种解释器类，实现与语法的终结符相关的操作。
    - NonTerminalExpression（非终结符表达式）：这是实现语法的不同规则或符号的类。
  - 解释器模式的优缺点：
    - 优点：解释器是一个简单的语法分析工具，它最显著的优点就是扩展性，修改语法规则只需要修改相应的非终结符就可以了，若扩展语法，只需要增加非终结符类就可以了。
    - 缺点：解释器模式会引起类的膨胀，每个语法都需要产生一个非终结符表达式，语法规则比较复杂时，就可能产生大量的类文件
  - 使用场景：
    - 简单的语法规则
    - Java在java.util.Parser中实现了解释器模式，它用于解释正则表达式。

### 迭代器模式

  - 定义：提供一种方法访问一个容器对象中各个元素，而又不暴露该对象的内部细节
  - 包含以下类：
    - Aggregate（抽象容器）：一般是一个接口，提供一个iterator()方法
    - ConcreteAggregate（具体容器）：就是抽象容器的具体实现类，比如List接口的有序列表实现ArrayList，List接口的链表实现LinkList
    - Iterator（抽象迭代器）：抽象迭代器是迭代器抽象类，它定义遍历容器对象的操作以及返回对象的操作。
    - ConcreteIterator（具体迭代器）：这是处理特定具体容器类的具体迭代器。实现迭代器接口中定义的方法，完成集合的迭代。
  - 使用场景：
    - 大多数语言在实现容器的时候都给提供了迭代器

### 观察者模式（重要）

  - 定义：定义对象间一种一对多的依赖关系，使得当每一个对象改变状态，则所有依赖于它的对象都会得到通知并自动更新。
  - 包含以下类：
    - Subject（主题）：主题通常是由类实现的可观察的接口。应通知的观察者使用attach方法注册。当它们不再需要被告知变更时，使用detach方法取消注册。
    - ConcreteSubject（具体主题）：具体主题是一个实现主题接口的类。它处理观察者列表并更新它们的变化。
    - Observer（观察者）：观察者是一个由对象实现的接口，应该根据主题中的更改来进行更新。每个观察者都应该实现update方法，该方法通知它们新的状态变化。
  
### 中介者模式

  - 定义：用一个中介者对象封装一系列的对象交互，中介者使各对象不需要显示地相互作用，从而使耦合松散，而且可以独立地改变它们之间的交互。
  - 包含以下类：
    - Mediator（抽象中介者）：抽象中介者定义了参与者的交互方式。
    - ConcreteMediator（具体中介者）：它实现了中介者声明的操作。
    - Colleague（抽象同事角色）：这是一个抽象类或接口，用于定义需要调解的参与者如何进行交互。
    - ConcreteColleague（具体同事角色）：这是实现Colleague接口的具体类。
  - 中介者模式的优点：
    - 适当地使用中介者模式可以避免同事类之间的过度耦合，使得各同事类之间可以相对独立地使用
    - 使用中介者模式可以将对象间一对多的关联转变为一对一的关联，使对象间的关系易于理解和维护
    - 使用中介者模式可以将对象的行为和协作进行抽象，能够比较灵活的处理对象间的相互作用
  - 使用场景：
    - 当有许多实体以类似的方式进行交互并且这些实体应该解耦时，就应该使用中介者模式。
    - 在Java库中，中介者模式用于实现java.util.Timer。timer（计时器）类可用于调度线程以固定间隔运行一次或重复多次运行。

### 备忘录模式

  - 定义：保存对象的内部状态而不破坏其封装，并在以后阶段恢复其状态。
  - 包含以下类：
    - Originator（发起者）：发起者是我们需要记住状态的对象，以便在某个时刻恢复它。
    - Memento（备忘录）：这是负责存储发起者内部状态的类。
    - Caretaker（管理者）：对备忘录进行管理，保存和提供备忘录。
  - 使用场景：
    - 只要需要执行回滚操作，就会使用备忘录模式。它可用于各种原子事务，如果其中一个操作失败，则必须将对象恢复到初始状态。
    - 例如：JDBC的事务操作，文本编辑器的Ctrl+Z恢复等。

### 状态模式

  - 定义：允许一个对象在其内部状态改变的时候改变其行为。这个对象看上去就像是改变了它的类一样。
  - 包含的类：
    - 环境(Context)：定义客户端所感兴趣的接口，持有状态的对象。
    - 抽象状态(State)：用以封装环境（Context）对象的一个特定的状态所对应的行为。
    - 具体状态(ConcreteState)：每一个具体状态类都实现了环境（Context）的一个状态所对应的行为。

### 策略模式（重要）

  - 定义：定义一组算法，将每个算法都封装起来，并且使他们之间可以互换。
  - 包含以下类：
    - Strategy（抽象策略）：特定策略的抽象。
    - ConcreteStrategy（具体策略）：实现抽象策略的类。通常由一组封装了算法的类来担任，这些类之间可以根据需要自由替换。
    - Context（环境）：运行特定策略的类。对策略进行二次封装，目的是避免高层模块对策略的直接调用。
  - 策略模式的优缺点：
    - 优点：
      - 策略类之间可以自由切换
      - 易于扩展，增加一个新的策略很容易
    - 缺点：
      - 维护各个策略类会给开发带来额外开销
  - 使用场景：
    - 几个类的主要逻辑相同，只在部分逻辑的算法和行为上稍有区别的情况
    - 有几种相似的行为，或者说算法，客户端需要动态地决定使用哪一种，那么可以使用策略模式，将这些算法封装起来供客户端调用

### 模板方法模式（重要）

  - 定义：定义一个操作中算法的框架，而将一些步骤延迟到子类中，使得子类可以不改变算法的结构即可重定义该算法中的某些特定步骤。
  - 实现方式：
    - 模板方法模式实现的最好方式是使用抽象类。
  - 模版方法的优点：
    - 容易扩展：通过增加实现类一般可以很容易实现功能的扩展，符合开闭原则。
    - 便于维护：抽象类中的模版方法是不易产生改变的部分，假如不使用模版方法，任由这些相同的代码散乱的分布在不同的类中，维护起来是非常不方便的。
  - 使用场景：
    - 在多个子类拥有相同的方法，并且这些方法逻辑相同时，可以考虑使用模版方法模式。
    - 在程序的主框架相同，细节不同的场合下，也比较适合使用这种模式。
    - 项目中召回策略的扩展使用该模式

### 访问者模式

  - 定义：将操作与其操作的对象结构分开，允许添加新操作而不更改结构类。
  - 包含以下类：
    - Visitor（抽象访问者）：抽象类或者接口，声明访问者可以访问哪些元素
    - ConcreteVisitor（具体访问者）：访问者的实现。
    - Element（抽象元素）：表示对象结构的基类。结构中的所有类都是它派生的，它们必须实现accept（visitor：Visitor）方法。
    - ConcreteElement（具体元素类）：实现抽象元素类所声明的accept方法，通常都是visitor.visit(this)
  - 访问者模式的优点：
    - 符合单一职责原则：因为被封装的操作通常来说都是易变的，所以当发生变化时，就可以在不改变元素类本身的前提下，实现对变化部分的扩展。
    - 扩展性良好：元素类可以通过接受不同的访问者来实现对不同操作的扩展。
  - 使用场景：
    - 假如一个对象中存在着一些与本对象不相干（或者关系较弱）的操作，为了避免这些操作污染这个对象，则可以使用访问者模式来把这些操作封装到访问者中去。

## 结构型模式

### 适配器模式（重要）

  - 定义：将一个类的接口转换成客户期望的另一个接口，适配器让原本接口不兼容的类可以相互合作。
  - 包含角色：目标类（Target）、被适配类（Adaptee）、适配器（Adapter）
    - 说明：客户需要Target，现实只有Adaptee，可以用一个实现Target协议的适配器通过类继承或者对象组合类获得Adaptee。
  - 适配器模式优缺点：
    - 优点：
      - 单一职责原则：将接口或数据转换代码从程序主要业务逻辑中分离
      - 开闭原则：客户端代码通过客户端接口与适配器进行交互， 你就能在不修改现有客户端代码的情况下在程序中添加新类型的适配器
    - 缺点：代码整体复杂度增加
  - 使用场景：
    - 当你希望使用某个类，但是其接口与其他代码不兼容时，可以使用适配器。
    
### 外观模式

  - 定义：为子系统中一组不同的接口提供统一的接口
  - 外观模式优缺点：
    - 优点：可以让自己的代码独立于复杂子系统。
    - 缺点：外观可能成为与程序中所有类都耦合的上帝对象。
  - 使用场景：
    - 如果你需要一个指向复杂子系统的直接接口，且该接口的功能有限，则可以使用外观模式。
    - 如果需要将子系统组织为多层结构， 可以使用外观。

### 桥接模式

  - 定义：将抽象部分与它的实现部分分离，使它可以独立的变更
  - 桥接模式的优缺点：
    - 优点：
      - 可以创建与平台无关的类和程序
      - 客户端代码仅与高层抽象部分进行互动， 不会接触到平台的详细信息
      - 单一职责原则：抽象部分专注于处理高层逻辑， 实现部分处理平台细节
    - 缺点：对高内聚的类使用该模式可能会让代码更加复杂
  - 使用场景：
    - 如果你想要拆分或重组一个具有多重功能的庞杂类（例如能与多个数据库服务器进行交互的类），可以使用桥接模式。

### 装饰器模式（重要）

  - 定义：在不改变原类文件和使用继承的情况下，动态地扩展一个对象的功能。它是通过创建一个包装对象，也就是装饰来包裹真实的对象。
  - 装饰模式的特点：
    - 装饰对象和真实对象有相同的接口。这样客户端对象就能以和真实对象相同的方式和装饰对象交互。
    - 装饰对象包含一个真实对象的引用（reference）
    - 装饰对象接受所有来自客户端的请求。它把这些请求转发给真实的对象。
    - 装饰对象可以在转发这些请求以前或以后增加一些附加功能。
  - 装饰模式优点：
    - 需创建新子类即可扩展对象的行为
    - 可以在运行时添加或删除对象的功能
    - 可以用多个装饰封装对象来组合几种行为
  - 使用场景：
    - 在无需修改代码的情况下即可使用对象，且希望在运行时为对象新增额外的行为，可以使用装饰模式。

### 代理模式（重要）

  - 定义：为某对象提供一种代理以控制对该对象的访问。即客户端通过代理间接地访问该对象，从而限制、增强或修改该对象的一些特性。
  - 代理模式优缺点：
    - 优点：
      - 可以在客户端毫无察觉的情况下控制服务对象
      - 即使服务对象还未准备好或不存在， 代理也可以正常工作
    - 缺点：
      - 代码可能会变得复杂， 因为需要新建许多类
      - 服务响应可能会延迟
  - 使用场景：
    - 延迟初始化（虚拟代理）。如果你有一个偶尔使用的重量级服务对象，一直保持该对象运行会消耗系统资源时，可使用代理模式。
    - 访问控制（保护代理）。如果你只希望特定客户端使用服务对象，这里的对象可以是操作系统中非常重要的部分，而客户端则是各种已启动的程序（包括恶意程序），此时可使用代理模式。
    - 本地执行远程服务（远程代理）。适用于服务对象位于远程服务器上的情形。
 
### 组合模式

  - 定义：将对象组合成树形结构以表示“部分-整体”的层次结构，组合模式使得用户对单个对象和组合对象的使用具有一致性。
  - 包含以下类：
    - Component：是组合中的对象声明接口，用于访问和管理Component子部件
    - Leaf：在组合中表示叶子结点对象，叶子结点没有子结点。
    - Composite：定义有枝节点行为，用来存储子部件，在Component接口中实现与子部件有关操作
  - 组合模式的优缺点：
    - 优点：
      - 可以利用多态和递归机制更方便地使用复杂树结构
      - 开闭原则：无需更改现有代码，你就可以在应用中添加新元素，使其成为对象树的一部分
    - 缺点：对于功能差异较大的类，提供公共接口或许会有困难
  - 使用场景：
    - 如果你需要实现树状对象结构，可以使用组合模式。
    - 如果你希望客户端代码以相同方式处理简单和复杂元素，可以使用该模式。

### 享元模式

  - 定义：运用共享技术来有效地支持大量细粒度对象的复用，实现对象的共享，即共享池，当系统中对象多的时候可以减少内存的开销
  - 享元模式优缺点：
    - 优点：如果程序中有很多相似对象，那么你将可以节省大量内存。
    - 缺点：
      - 可能需要牺牲执行速度来换取内存， 因为每次调用享元方法时都需要重新计算部分情景数据
      - 代码会变得更加复杂
  - 使用场景：
    - 数据库连接池
    


