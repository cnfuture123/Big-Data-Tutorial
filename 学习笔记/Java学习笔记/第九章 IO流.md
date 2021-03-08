# IO流

## File类的使用

  - java.io.File类：文件和文件目录路径的抽象表示形式
  - File能新建、删除、重命名文件和目录，但File不能访问文件内容本身，需要使用输入/输出流来访问
  - 构造器：
    - public File(String pathname): 以pathname为路径创建File对象，可以是绝对路径或相对路径，相对路径默认当前路径是user.dir
  - 路径分隔符：
    - windows和DOS默认使用"\"
    - UNIX和URL使用"/"
  - 常用方法：
    - getAbsolutePath()：获取绝对路径
    - getName()：获取名称
    - getParent()：获取上层文件目录路径
    - listFiles()：获取指定目录下的所有文件
    - isDirectory()：判断是否是文件目录
    - isFile()：判断是否是文件
    - exists()：判断是否存在
    - createNewFile()：创建文件
    - mkdir()：创建文件目录
    - delete()：删除文件或文件夹

## IO流原理

  - 概述：
    - IO是Input/Output缩写，处理设备之间的数据传输
    - Java对于数据的输入/输出操作以流的方式进行
    - 输入input：读取外部数据（磁盘、光盘）到程序内存中
    - 输出output：将程序内存数据输出到磁盘、光盘等存储设备
  - 流的分类：
    - 按操作单位分为：字节流（8 bit），字符流（16 bit）
    - 按数据流的流向分为：输入流，输出流
    - 按流的角色分为：节点流，处理流
  - 节点流和处理流：
    - 节点流：直接从数据源或目的地读写数据
    - 处理流：不直接连接到数据源或目的地，而是连接在已存在的流之上
  - InputStream和Reader：
    - InputStream和Reader是所有输入流的基类
    - InputStream(典型实现：FileInputStream)：
      - int read()：从输入流中读取数据的下一个字节
      - int read(byte[] b)：从输入流中将最多b.length字节数据读入一个byte[]中
      - int read(byte[] b, int off, int len)：将输入流中最多len字节的数据读入byte[]
      - close()：关闭输入流，释放资源
    - Reader（典型实现：FileReader）:
      - int read()：读取单个字符
      - int read(char[] c)：将字符读入数组
      - int read(char[] c, int off, int len)：将字符读入数组c中，从off开始存储，最多len个字符
      - close()：关闭输入流，释放资源
  - OutputStream和Writer
    - 类似的方法：
      - write()
      - write(byte[] b / char[] c)
      - write(byte[] b / char[] c, int off, int len)
      - flush()：刷新输出流的缓冲数据，写入磁盘
      - close()  
    
## 节点流（或文件流）

  - 读取文件：
    ```
    1.建立一个流对象，将已存在的文件数据加载进流
      FileReader fr = new FileReader(new File("1.txt"));
    2.创建临时存放数据的数组
      char[] ch = new char[1024];
    3.调用流对象的读取方法将数据读入到数组中
      fr.read(ch)
    4.关闭资源：
      fr.close()
    ```
  - 写入文件：
    ```
    1.创建流对象，建立数据存放文件
      FileWriter fw = new FileWriter(new File("2.txt"));
    2.调用流对象的写入方法，将数据写入流
      fw.write("1233333333333444");
    3.关闭资源
      fw.close()
    ```
  - 注意点：
    - 写入文件时，如果使用构造器FileOutputStream(file)，则目录下同名文件会被覆盖；如果使用FileOutputStream(file, true)，则在文件末尾追加
    - 读取文件时，如果文件不存在会报异常
    
## 缓冲流

  - 概述：
    - 为了提高数据读写速度，Java API提供缓冲功能的流类，使用时会创建一个内部缓冲区数组，默认使用8kb的缓冲区
    - 缓冲流要套接在相应的节点流上，按数据操作单位可分为：
      - BufferedInputStream, BufferedOutputStream
      - BufferedReader, BufferedWriter
  - 特点：
    - 读取数据时，数据按块读入缓冲区，之后的读操作直接访问缓冲区。写数据时也是先写到缓冲区，直到缓冲区写满再刷写到文件中
    - 关闭流的顺序与打开流的顺序相反
  - 示例：
    ```
    BufferedReader br = null;
    BufferedWriter bw = null;
    try {
      br = new BufferedReader(new FileReader(1.txt));
      bw = new BufferedWriter(new FileWriter(2.txt));
      String str;
      while(str = br.readLine() != null) {
        bw.write(str);
        bw.newLine();
      }
      bw.flush();
    } catch (IOException e) {
      e.printStachTrace();
    } finally {
      if (bw != null) {
        bw.close();
      }
      if (br != null) {
        br.close();
      }
    }
    ```

## 转换流

  - 概述：
    - 转换流提供在字节流和字符流之间的转换
    - Java API提供两种转换流：
      - InputStreamReader：将InputStream转换为Reader，需要和InputStream套接
        - public InputStreamReader(InputStream in)
        - public InputStreamReader(InputStream in, String charsetName)
      - OutputStreamWriter：将Writer转换为OutputStream，需要和OutputStream套接
        - public OutputStreamWriter(OutputStream out)
        - public OutputStreamWriter(OutputStream out, String charsetName)

## 标准输入、输出流

  - 概述：
    - System.in和System.out分别代表系统标准的输入和输出设备
    - System.in的类型是InputStream
    - System.out的类型是PrintStream，它是OutStream的子类

## 对象流

  - 概述：
    - ObjectInputStream和ObjectOutputStream：用于存储和读取基本类型数据或对象的处理流，可以把Java的对象写入到数据源中，也能将对象从数据源中还原
      - 序列化：用ObjectOutputStream保存基本类型数据或对象的机制
      - 反序列化：用ObjectInputStream读取基本类型数据或对象的机制
      - 不能序列化static和transient修饰的成员变量
  - 对象的序列化：
    - 对象序列化机制允许把内存中的Java对象转换成平台无关的二进制流，进而把二进制流持久化地保存在磁盘上，或通过网络将二进制流传输到另一个网络节点
    - 为了让某个类是可序列化的，需要实现两个接口之一：
      - Serializable
      - Externalizable
    - 实现Serializable接口的类都有一个表示序列化版本标识符的静态变量：private static final long serialVersionUID
    - Java的序列化机制是通过在运行时判断类的serialVersionUID来验证版本一致性的。在进行反序列化时，JVM会把传来的字节流中的serialVersionUID与本地相应实体类的serialVersionUID进行比较，如果相同就认为是一致的，可以进行反序列化，否则就会抛出异常InvalidCastException
    

