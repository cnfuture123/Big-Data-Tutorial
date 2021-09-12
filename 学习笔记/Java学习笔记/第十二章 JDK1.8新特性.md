# JDK1.8新特性

## Lambda表达式

  - 概述：
    - Lambda是一个匿名函数，可以把Lambda表达式理解为一段可以传递的代码
    - 使用更简洁、更灵活
    - 使用"->"操作符，将Lambda表达式分为两个部分：
      ```
      (argument-list) -> {body}  
      ```
      - 左侧：指定Lambda表达式需要的参数列表
      - 右侧：指定Lambda体，Lambda表达式实现的功能
      - 
  - 语法：
    - 无参、无返回值：
      ```
      Runnable r1 = () -> {System.out.println("Hello Lambda");};
      ```
    - 传一个参数，但无返回值：
      ```
      Consumer<String> con = (String str) -> {System.out.println(str);};
      ```
    - 数据类型省略，编译器通过类型推断：
      ```
      Consumer<String> con = (str) -> {System.out.println(str);};
      ```
    - 传两个或以上参数，多条执行语句，并且有返回值：
      ```
      Comparator<Integer> com = (x, y) -> {
        System.out.println(Hello Lambda);
        return Integer.compare(x, y);
      };
      ```
  - Collection中的新方法：
    - forEach():
      - 方法签名：
        ```
        void forEach(Consumer<? super E> action)
        ```
      - 示例：
        ```
        ArrayList<String> list = new ArrayList<>(Arrays.asList("I", "love", "you", "too"));
        list.forEach(str -> {
          if (str.length > 3) System.out.println(str);         
        });
        ```
      
## 函数式接口

  - 概述：
    - 只包含一个抽象方法的接口，称为函数式接口
    - 在接口上使用@FunctionalInterface注解声明这个接口是一个函数式接口
    - Lambda表达式就是一个函数式接口的实例
  - Java四大核心函数式接口：
    - Consumer<T>：消费型接口，对类型为T的对象应用操作。包含方法：void accept(T t)
    - Supplier<T>：供给型接口：返回类型为T的对象。包含方法：T get()
    - Function<T, R>：函数型接口：对类型为T的对象应用操作，并返回结果是R类型的对象。包含方法：R apply(T t)
    - Predicate<T>：断定型接口：确定类型为T的对象是否满足约束条件，并返回boolean值。包含方法：boolean test(T t)
  - 自定义函数接口：编写一个只有一个抽象方法的接口即可
    - 自定义接口：
      ```
      @FunctionalInterface
      public interface ConsumerInterface<T> {
        void accept(T t);
      }
      ```
    - 调用接口：
      ```
      class MyStream<T> {
        private List<T> list;
        public void loopList(ConsumerInterface<T> consumer) {
          for (T t : list) {
            consumer.accept(t);
          }
        }
      }
      ```

## 方法引用和构造器引用

  - 方法引用：
    - 方法引用可以看作Lambda表达式深层次的表达，通过方法的名字指向一个方法，是Lambda表达式的一个语法糖
    - 格式：使用操作符"::"将类（或对象）与方法名分隔
    - 常用方式：
      - 对象::实例方法名
      - 类::静态方法名
      - 类::实例方法名
    - 示例：
      ```
      Consumer<String> con = System.out::println;
      ```
  - 构造器引用：
    - 格式：ClassName::new
    - 示例：
      ```
      Function<Integer, MyClass> fun = MyClass::new;
      ```
      
## Stream API

  - Stream概述：
    - Stream是Java 8中处理集合的关键抽象概念，可以执行复杂的查找、过滤和映射等操作。
    - 使用Stream API对集合数据进行操作，类似于使用SQL执行数据库查询。
    - Stream本质是数据流，用于操作数据源（集合、数组等）所生成的元素序列
  - Stream特点：
    - 集合讲的是数据，Stream讲的是计算
    - Stream本身不会存储元素
    - Stream不会改变源对象，操作后会返回一个持有结果的新Stream
    - Stream是延迟操作的，等到需要结果时才执行
  - Stream操作步骤：
    - 创建Stream：通过数据源（集合、数组等）获取流
      - 通过集合：
        ```
        - default Stream<E> stream()：返回一个顺序流
        - default Stream<E> parallelStream()：返回一个并行流
        ```
      - 通过数组：
        ```
        - static<T> Stream<T> stream(T[] array)
        - static IntStream stream(int[] array)
        - static DoubleStream stream(double[] array)
        ```
      - 通过Stream的of()：
        ```
        - public static<T> Stream<T> of(T... values)
        ```
      - 通过Stream静态方法创建无限流：
        ```
        - 迭代方式：public static<T> Stream<T> iterate(final T seed, final UnaryOperator<T> f)
        - 生成方式：public static<T> Stream<T> generate(Supplier<T> s)
        ```
    - 中间操作：中间操作链，对数据源的数据进行操作
      - 筛选和切片：
        ```
        - filter(Predicate p)：过滤元素
        - distinct()：去重
        - limit(long maxSize)：限制元素个数
        - skip(long n)：返回跳过前n个元素的流
        ```
      - 映射：
        ```
        - map(Function f)：接收一个函数作为参数，该函数被应用到每个元素上，映射为一个新的元素
        - flatMap(Function f)：接收一个函数作为参数，将流中的每个值换成另一个流，然后将所有流连成一个流
        ```
      - 排序：
        ```
        - sorted()：按自然顺序排序的新流
        - sorted(Comparator com)：按比较器顺序排序的新流
        ```
    - 终止操作：执行中间操作链，并产生结果
      - 匹配和查找：
        ```
        - allMatch(Predicate p)：检查是否匹配所有元素
        - anyMatch(Predicate p)：检查是否至少匹配一个元素
        - findFirst()：返回第一个元素
        - findAny()：返回当前流中的任意元素
        ```
      - 统计：
        ```
        - count()：返回元素总数
        - forEach(Consumer c)：迭代
        ```
      - 归约：
        ```
        - reduce(T iden, BinaryOperator b)：可以将流中元素结合，得到一个值，返回T
        - reduce(BinaryOperator b)：可以将流中元素结合，得到一个值，返回Optional<T>
        ```
      - 收集：
        ```
        - collect(Collector c)：流中的元素做汇总
        ```
        
## Optional类

  - 概述：
    - Optional<T>类是一个容器类，可以保存类型T的值代表这个值存在，或者保存null表示这个值不存在。
  - Optional类的方法：
    - 创建Optional类对象的方法：
      ```
      - Optional.of(T t)：创建一个Optional实例，t必须非空
      - Optional.ofNullable(T t)：创建一个Optional实例，t可以为null
      - Optional.empty()：创建一个空的Optional实例
      ```
    - 判断Optional容器是否包含对象：
      ```
      - boolean isPresent()：判断是否包含对象
      - void ifPresent(Consumer<? super T> consumer)：如果有值，就执行Consumer接口的实现代码，并且该值作为参数传给它
      ```
    - 获取Optional容器的对象：
      ```
      - T get()：如果调用对象包含值，返回该值，否则抛出异常
      - T orElse(T other)：如果有值则返回，否则返回指定的other对象
      - T orElseGet(Supplier<? extends T> other)：如果有值则返回，否则返回由Supplier接口实现提供的对象
      ```

