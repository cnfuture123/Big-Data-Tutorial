# JDK1.8新特性

## Lambda表达式

  - 概述：
    - Lambda是一个匿名函数，可以把Lambda表达式理解为一段可以传递的代码
    - 使用更简洁、更灵活
    - 使用"->"操作符，将Lambda表达式分为两个部分：
      - 左侧：指定Lambda表达式需要的参数列表
      - 右侧：指定Lambda体，Lambda表达式实现的功能
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


      
