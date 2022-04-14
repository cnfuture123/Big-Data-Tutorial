## SpringApplication介绍

  - org.springframework.boot.SpringApplication：Spring应用启动器，提供启动Spring应用的功能

## SpringApplication代码解析

  - 属性说明：
    ```
    // Spring应用上下文（非Web场景）
    public static final String DEFAULT_CONTEXT_CLASS = "org.springframework.context.annotation.AnnotationConfigApplicationContext";
    // Spring应用上下文（Web场景）
    public static final String DEFAULT_SERVLET_WEB_CONTEXT_CLASS = "org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext";
    // Spring应用上下文（Reactive场景）
    public static final String DEFAULT_REACTIVE_WEB_CONTEXT_CLASS = "org.springframework.boot.web.reactive.context.AnnotationConfigReactiveWebServerApplicationContext";
    
    public static final String BANNER_LOCATION_PROPERTY_VALUE = "banner.txt";
    public static final String BANNER_LOCATION_PROPERTY = "spring.banner.location";
    private static final String SYSTEM_PROPERTY_JAVA_AWT_HEADLESS = "java.awt.headless";
    private static final Log logger = LogFactory.getLog(SpringApplication.class);
    
    // 主Bean（通常为我们的启动类，优先注册）
    private Set<Class<?>> primarySources;
    // 来源Bean（优先注册）
    private Set<String> sources;
    // 启动类
    private Class<?> mainApplicationClass;
    // Banner打印模式
    private Mode bannerMode;
    // 是否打印应用启动耗时日志
    private boolean logStartupInfo;
    // 是否接收命令行中的参数
    private boolean addCommandLineProperties;
    // 是否设置ConversionService类型转换器
    private boolean addConversionService;
    // Banner对象（用于输出横幅）
    private Banner banner;
    // 资源加载对象
    private ResourceLoader resourceLoader;
    // Bean名称生成器
    private BeanNameGenerator beanNameGenerator;
    // Spring应用的环境对象
    private ConfigurableEnvironment environment;
    // Spring应用上下文的Class对象
    private Class<? extends ConfigurableApplicationContext> applicationContextClass;
    // Web应用的类型（Servlet、Reactive）
    private WebApplicationType webApplicationType;
    private boolean headless;
    // 是否注册钩子函数，用于JVM关闭时关闭Spring应用上下文
    private boolean registerShutdownHook;
    // 保存ApplicationContextInitializer对象（主要是对Spring应用上下文做一些初始化工作）
    private List<ApplicationContextInitializer<?>> initializers;
    // 保存ApplicationListener监听器（支持在整个Spring Boot的多个时间点进行扩展）
    private List<ApplicationListener<?>> listeners;
    // 默认的配置项
    private Map<String, Object> defaultProperties;
    // 额外的profile
    private Set<String> additionalProfiles;
    // 是否允许覆盖BeanDefinition
    private boolean allowBeanDefinitionOverriding;
    // 是否为自定义的Environment对象
    private boolean isCustomEnvironment;
    // 是否支持延迟初始化（需要通过 {@link LazyInitializationExcludeFilter} 过滤）
    private boolean lazyInitialization;
    ```
    
## SpringApplication实例化流程

  - 流程分析图：
  
    <img width="1103" alt="image" src="https://user-images.githubusercontent.com/46510621/163306850-6840b8eb-48d9-4e04-a9d9-64708c9aca42.png">
    
  - SpringApplication构造器源码分析：
    ```
    public SpringApplication(ResourceLoader resourceLoader, Class<?>... primarySources) {
        this.sources = new LinkedHashSet();
        this.bannerMode = Mode.CONSOLE;
        this.logStartupInfo = true;
        this.addCommandLineProperties = true;
        this.addConversionService = true;
        this.headless = true;
        this.registerShutdownHook = true;
        this.additionalProfiles = new HashSet();
        this.isCustomEnvironment = false;
        this.lazyInitialization = false;
        // 设置资源加载器resourceLoader，默认为null，可以通过SpringApplicationBuilder设置
        this.resourceLoader = resourceLoader;
        Assert.notNull(primarySources, "PrimarySources must not be null");
        // 设置primarySources为主要的Class类对象，通常是我们的启动类
        this.primarySources = new LinkedHashSet(Arrays.asList(primarySources));
        // 确定Web应用的类型，可选值：NONE，SERVLET，REACTIVE
        this.webApplicationType = WebApplicationType.deduceFromClasspath();
        // 初始化所有ApplicationContextInitializer类型的对象，并保存至initializers集合中
        this.setInitializers(this.getSpringFactoriesInstances(ApplicationContextInitializer.class));
        // 初始化所有ApplicationListener类型的对象，并保存至listeners集合中
        this.setListeners(this.getSpringFactoriesInstances(ApplicationListener.class));
        // 获取当前被调用的main方法所属的Class类对象
        this.mainApplicationClass = this.deduceMainApplicationClass();
    }
    ```
    - 确定Web应用的类型:
      ```
      this.webApplicationType = WebApplicationType.deduceFromClasspath();
      
      static WebApplicationType deduceFromClasspath() {
          if (ClassUtils.isPresent("org.springframework.web.reactive.DispatcherHandler", (ClassLoader)null) &&                  !ClassUtils.isPresent("org.springframework.web.servlet.DispatcherServlet", (ClassLoader)null) && !ClassUtils.isPresent("org.glassfish.jersey.servlet.ServletContainer", (ClassLoader)null)) {
              return REACTIVE;
          } else {
              String[] var0 = SERVLET_INDICATOR_CLASSES;
              int var1 = var0.length;

              for(int var2 = 0; var2 < var1; ++var2) {
                  String className = var0[var2];
                  if (!ClassUtils.isPresent(className, (ClassLoader)null)) {
                      return NONE;
                  }
              }

              return SERVLET;
          }
      }
      ```

  
    
    
    
    
