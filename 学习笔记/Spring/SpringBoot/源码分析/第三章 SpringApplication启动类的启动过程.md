## SpringApplication介绍

  - org.springframework.boot.SpringApplication：Spring应用启动器，提供启动Spring应用的功能

## SpringApplication代码解析

  - 属性说明：
    ```
    public static final String DEFAULT_CONTEXT_CLASS = "org.springframework.context.annotation.AnnotationConfigApplicationContext";
    public static final String DEFAULT_SERVLET_WEB_CONTEXT_CLASS = "org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext";
    public static final String DEFAULT_REACTIVE_WEB_CONTEXT_CLASS = "org.springframework.boot.web.reactive.context.AnnotationConfigReactiveWebServerApplicationContext";
    public static final String BANNER_LOCATION_PROPERTY_VALUE = "banner.txt";
    public static final String BANNER_LOCATION_PROPERTY = "spring.banner.location";
    private static final String SYSTEM_PROPERTY_JAVA_AWT_HEADLESS = "java.awt.headless";
    private static final Log logger = LogFactory.getLog(SpringApplication.class);
    private Set<Class<?>> primarySources;
    private Set<String> sources;
    private Class<?> mainApplicationClass;
    private Mode bannerMode;
    private boolean logStartupInfo;
    private boolean addCommandLineProperties;
    private boolean addConversionService;
    private Banner banner;
    private ResourceLoader resourceLoader;
    private BeanNameGenerator beanNameGenerator;
    private ConfigurableEnvironment environment;
    private Class<? extends ConfigurableApplicationContext> applicationContextClass;
    private WebApplicationType webApplicationType;
    private boolean headless;
    private boolean registerShutdownHook;
    private List<ApplicationContextInitializer<?>> initializers;
    private List<ApplicationListener<?>> listeners;
    private Map<String, Object> defaultProperties;
    private Set<String> additionalProfiles;
    private boolean allowBeanDefinitionOverriding;
    private boolean isCustomEnvironment;
    private boolean lazyInitialization;
    ```
