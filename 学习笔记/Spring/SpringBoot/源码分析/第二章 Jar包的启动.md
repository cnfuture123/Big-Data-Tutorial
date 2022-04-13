## 概述

  - Spring Boot提供了Maven插件spring-boot-maven-plugin，可以很方便的Spring Boot项目打成jar包或者war包
  - 生成的Jar包结构：
    - 示例：
      
      <img width="581" alt="image" src="https://user-images.githubusercontent.com/46510621/162924154-2585575d-5277-4969-ac83-c7d22b7531c8.png">
      
    - BOOT-INF目录：保存Spring Boot项目编译后的所有文件，其中classes目录下面就是编译后的.class 文件，包括项目中的配置文件等，lib目录下就是我们引入的第三方依赖
    - META-INF目录：通过MANIFEST.MF文件提供jar包的元数据，声明jar的启动类等信息。
      - 示例：
        ```
        Manifest-Version: 1.0
        Spring-Boot-Classpath-Index: BOOT-INF/classpath.idx
        Implementation-Title: admin-server
        Implementation-Version: 1.0.0-SNAPSHOT
        Start-Class: com.paradigm.cess.admin.AdminApplication
        Spring-Boot-Classes: BOOT-INF/classes/
        Spring-Boot-Lib: BOOT-INF/lib/
        Build-Jdk-Spec: 1.8
        Spring-Boot-Version: 2.3.9.RELEASE
        Created-By: Maven Jar Plugin 3.2.0
        Main-Class: org.springframework.boot.loader.JarLauncher
        ```
        - Main-Class配置用于指定启动类，这里设置为spring-boot-loader项目的JarLauncher类，进行Spring Boot应用的启动
        - Start-Class：Spring Boot规定的主启动类，这里通过Spring Boot Maven Plugin插件打包时，会设置为我们定义的Application启动类
      - 为什么不直接将我们的Application启动类设置为Main-Class启动呢？
        - class文件在BOOT-INF/classes/目录下，在Java默认的jar包加载规则下找不到我们的Application启动类，需要通过JarLauncher启动加载
    - org.springframework.boot.loader目录：Spring Boot的spring-boot-loader工具模块，它就是java -jar xxx.jar启动Spring Boot项目的秘密所在，上面的Main-Class指定的就是该工具模块中的一个类
      - JarLauncher: 
        - 类图：
          
          <img width="479" alt="image" src="https://user-images.githubusercontent.com/46510621/162951158-00e4edaf-42ed-4bec-9930-c74a23659951.png">

        - JarLauncher代码：
          ````
          public class JarLauncher extends ExecutableArchiveLauncher {
              public static void main(String[] args) throws Exception {
                  (new JarLauncher()).launch(args);
              }
          }
          ```
          - 通过JarLauncher启动加载应用时会执行main()，实际调用的是父类ExecutableArchiveLauncher的父类Launcher的launch()
        - Launcher.launch()：
          ```
          protected void launch(String[] args) throws Exception {
              if (!isExploded())
                  JarFile.registerUrlProtocolHandler();
              ClassLoader classLoader = createClassLoader(getClassPathArchivesIterator());
              String jarMode = System.getProperty("jarmode");
              String launchClass = (jarMode != null && !jarMode.isEmpty()) ? "org.springframework.boot.loader.jarmode.JarModeLauncher" : getMainClass();
              launch(args, launchClass, classLoader);
          }
          ```
              
      



## 参考

  - https://www.cnblogs.com/lifullmoon/p/14953064.html
