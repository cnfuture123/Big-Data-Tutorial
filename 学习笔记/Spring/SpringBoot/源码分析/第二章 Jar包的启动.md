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

        - JarLauncher执行流程：
          
          <img width="1246" alt="image" src="https://user-images.githubusercontent.com/46510621/163146808-f08cb490-34d1-42b3-ab3d-31384c6f842b.png">
          
        - JarLauncher代码：
          ```
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
          - registerUrlProtocolHandler: 注册URL（jar）协议的处理器，主要是使用自定义的URLStreamHandler处理器处理jar包
            ```
            public static void registerUrlProtocolHandler() {
                Handler.captureJarContextUrl();
                String handlers = System.getProperty("java.protocol.handler.pkgs", "");
                System.setProperty("java.protocol.handler.pkgs",
                        "".equals(handlers) ? "org.springframework.boot.loader" : (handlers + "|" + "org.springframework.boot.loader"));
                resetCachedUrlHandlers();
            }
            ```
            - 获取系统变量中的java.protocol.handler.pkgs配置的URLStreamHandler路径
            - 将Spring Boot自定义的URL协议处理器路径（org.springframework.boot.loader）添加至系统变量中
            - 重置已缓存的URLStreamHandler处理器，避免重复创建
              ```
              private static void resetCachedUrlHandlers() {
                  try {
                      URL.setURLStreamHandlerFactory(null);
                  } catch (Error error) {}
              }
              ```
          - getClassPathArchivesIterator: 子类ExecutableArchiveLauncher获取当前jar所有archive条目信息
            ```
            protected Iterator<Archive> getClassPathArchivesIterator() throws Exception {
                Archive.EntryFilter searchFilter = this::isSearchCandidate;
                Iterator<Archive> archives = this.archive.getNestedArchives(searchFilter, entry ->
                        (isNestedArchive(entry) && !isEntryIndexed(entry)));
                if (isPostProcessingClassPathArchives())
                    archives = applyClassPathArchivePostProcessing(archives);
                return archives;
            }
            ```
            - archive的初始化: 这个archive是在创建JarLauncher实例化对象的时候初始化的
              ```
              protected final Archive createArchive() throws Exception {
                  ProtectionDomain protectionDomain = getClass().getProtectionDomain();
                  CodeSource codeSource = protectionDomain.getCodeSource();
                  URI location = (codeSource != null) ? codeSource.getLocation().toURI() : null;
                  String path = (location != null) ? location.getSchemeSpecificPart() : null;
                  if (path == null)
                      throw new IllegalStateException("Unable to determine code source archive");
                  File root = new File(path);
                  if (!root.exists())
                      throw new IllegalStateException("Unable to determine code source archive from " + root);
                  return root.isDirectory() ? (Archive)new ExplodedArchive(root) : (Archive)new JarFileArchive(root);
              }
              ```
            - isSearchCandidate: 判断当前jar包中的哪些Archive对象是可以被搜索的，可搜索的条件是在BOOT-INF/目录下
              ```
              protected boolean isSearchCandidate(Archive.Entry entry) {
                  return entry.getName().startsWith("BOOT-INF/");
              }
              ```
            - getNestedArchives: 从archive（当前 jar 包）解析出所有Archive条目信息，返回JarFileArchive对象
          - createClassLoader：
            ```
            protected ClassLoader createClassLoader(Iterator<Archive> archives) throws Exception {
                List<URL> urls = new ArrayList<>(50);
                while (archives.hasNext())
                    urls.add(((Archive)archives.next()).getUrl());
                return createClassLoader(urls.<URL>toArray(new URL[0]));
            }

            protected ClassLoader createClassLoader(URL[] urls) throws Exception {
                return new LaunchedURLClassLoader(isExploded(), getArchive(), urls, getClass().getClassLoader());
            }
            ```
            - 获取所有JarFileArchive对应的URL
            - 创建Spring Boot自定义的ClassLoader类加载器：LaunchedURLClassLoader
          - getMainClass: 子类ExecutableArchiveLauncher获取主类
            ```
            protected String getMainClass() throws Exception {
                Manifest manifest = this.archive.getManifest();
                String mainClass = null;
                if (manifest != null)
                    mainClass = manifest.getMainAttributes().getValue("Start-Class");
                if (mainClass == null)
                    throw new IllegalStateException("No 'Start-Class' manifest entry specified in " + this);
                return mainClass;
            }
            ```
            - 获取jar包的Manifest对象，也就是META-INF/MANIFEST.MF文件中的属性
            - 获取启动类也就是Start-Class配置
          - launch: 
            ```
            protected void launch(String[] args, String launchClass, ClassLoader classLoader) throws Exception {
                Thread.currentThread().setContextClassLoader(classLoader);
                createMainMethodRunner(launchClass, args, classLoader).run();
            }
            ```
            - 设置当前线程的ClassLoader为刚创建的类加载器
            - createMainMethodRunner: 创建一个MainMethodRunner对象（main方法执行器）
              ```
              protected MainMethodRunner createMainMethodRunner(String mainClass, String[] args, ClassLoader classLoader) {
                  return new MainMethodRunner(mainClass, args);
              }
              ```
            - MainMethodRunner.run(): 反射的方式执行主类的main()，启动Spring Boot应用
              ```
              public void run() throws Exception {
                  Class<?> mainClass = Class.forName(this.mainClassName, false, Thread.currentThread().getContextClassLoader());
                  Method mainMethod = mainClass.getDeclaredMethod("main", new Class[] { String[].class });
                  mainMethod.setAccessible(true);
                  mainMethod.invoke((Object)null, new Object[] { this.args });
              }
              ```

## 参考

  - https://www.cnblogs.com/lifullmoon/p/14953064.html
