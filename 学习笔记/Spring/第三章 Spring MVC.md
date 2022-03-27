# Spring MVC

## 概述

  - Spring MVC是Spring提供给Web应用的框架设计。将Web层职责进行解耦，基于请求-响应模型
  - MVC是Model、View和Controller的缩写，分别代表Web应用程序中的3种职责：
    - 模型：一个或多个JavaBean对象，用于存储数据（实体模型，由JavaBean类创建）和处理业务逻辑（业务模型，由一般的Java类创建）
    - 视图：一个或多个JSP页面，向控制器提交数据和为模型提供数据显示，JSP页面主要使用HTML标记和JavaBean标记来显示数据
    - 控制器：根据视图提出的请求判断将请求和数据交给哪个模型处理，将处理后的结果交给对应的视图更新显示
  - Spring MVC作用：
    - 天生与Spring框架集成（支持IOC, AOP）
    - 支持Restful风格
    - 支持灵活的URL到页面控制器的映射
    - 支持灵活的数据验证、格式化和数据绑定机制

## Spring MVC工作流程（重要）

  - Spring MVC框架主要组件：
    - DispatcherServlet：前端控制器，Spring MVC所有的请求都经过DispatcherServlet来统一分发
    - Controller：处理器/页面控制器，处理用户请求，将返回ModelAndView对象给DispatcherServlet前端控制器，ModelAndView中包含了模型（Model）和视图（View）。
    - HandlerMapping：处理器映射，将请求映射到具体的处理器
    - View Resolver：视图解析器，负责查找View对象，从而将相应结果渲染给客户
    - HandlerExceptionResolver：异常处理器
    - 说明：从宏观考虑，DispatcherServlet是整个Web应用的控制器；从微观考虑，Controller是单个Http请求处理过程中的控制器，而ModelAndView是Http请求过程中返回的模型（Model）和视图（View）。
  - 工作流程示例：
    
    ![image](https://user-images.githubusercontent.com/46510621/111857066-ff233180-8969-11eb-833c-58beab090529.png)

## 创建Spring MVC应用

  - 导入依赖：
    
    ![image](https://user-images.githubusercontent.com/46510621/111857567-55de3a80-896d-11eb-9bce-7f7a148483f2.png)

  - 在web.xml文件中配置DispatcherServlet
  
    ![image](https://user-images.githubusercontent.com/46510621/111857633-cbe2a180-896d-11eb-91de-8129a4db5fc5.png)
    
  - 创建Web应用首页：
    - 在应用的WebContent目录下有个应用首页index.jsp
    - index.jsp的代码示例：
      
      ![image](https://user-images.githubusercontent.com/46510621/111857713-5fb46d80-896e-11eb-9651-a1faf30efad8.png)

  - 创建Controller类：
    - 在src目录下创建controller包，并在该包中创建RegisterController和LoginController两个传统风格的控制器类（实现了Controller接口），分别处理首页中“注册”和“登录”超链接的请求。
    - RegisterController代码示例：

      ![image](https://user-images.githubusercontent.com/46510621/111857755-a99d5380-896e-11eb-9445-9aea6185121a.png)

    - LoginController代码示例：
    
      ![image](https://user-images.githubusercontent.com/46510621/111857777-c89be580-896e-11eb-9c98-4ecb067f000d.png)

  - 创建Spring MVC配置文件并配置Controller映射信息：
    - 在WEB-INF目录下创建名为springmvc-servlet.xml的配置文件
    - 代码示例：
    
      ![image](https://user-images.githubusercontent.com/46510621/111857818-14e72580-896f-11eb-8b43-740ee61ccf51.png)

  - 创建应用的其他页面：
    - RegisterController控制器处理成功后跳转到/WEB-INF/jsp下的register.jsp视图，LoginController控制器处理成功后跳转到/WEB-INF/jsp下的login.jsp视图，因此在应用的/WEB-INF/jsp目录下应有 register.jsp和login.jsp页面
  - 发布并运行Spring MVC应用：
    - 第一次运行Spring MVC应用时需要将应用发布到Tomcat。例如在运行springMVCDemo01应用时可以选中应用名称springMVCDemo01并右击，然后选择Run As→Run on Server命令
    - 通过地址“http://localhost:8080/springMVCDemo01”首先访问 index.jsp页面
    
## @Controller和@RequestMapping注解详解

### @Controller注解

  - 基于注解的控制器具有以下两个优点：
    - 在基于注解的控制器类中可以编写多个处理方法，进而可以处理多个请求（动作），可以将相关的操作编写在同一个控制器类中，从而减少控制器类的数量，方便以后的维护
    - 基于注解的控制器不需要在配置文件中部署映射，仅需要使用@RequestMapping注解一个方法进行请求处理
  - 使用@Controller注解声明某类的实例是一个控制器，示例：

    ![image](https://user-images.githubusercontent.com/46510621/111858013-82478600-8970-11eb-86e9-7d587c196635.png)

  - 在Spring MVC中使用扫描机制找到应用中所有基于注解的控制器类，需要在配置文件中声明spring-context，并使用<context：component-scan/> 元素指定控制器类的基本包（请确保所有控制器类都在基本包及其子包下）。

    ![image](https://user-images.githubusercontent.com/46510621/111858031-b9b63280-8970-11eb-9f41-ce81cd6c0f37.png)

### @RequestMapping注解

  - @RequestMapping注解作用是将请求与处理方法一一对应
  - 方法级别注解，标记在方法上：
    
    ![image](https://user-images.githubusercontent.com/46510621/111858089-25000480-8971-11eb-8c60-cacce2ac1db2.png)

    - 注解的value属性将请求URI映射到方法，value属性是RequestMapping注解的默认属性
    - 用户可以使用如下URL访问login方法，在访问login方法之前需要事先在/WEB-INF/jsp/目录下创建login.jsp
      ```
      http://localhost:8080/springMVCDemo02/index/login
      ```
  - 类级别注解，标记在类上：
  
    ![image](https://user-images.githubusercontent.com/46510621/111861153-25a39580-8987-11eb-8874-116c476ba897.png)

    - 在类级别注解的情况下，控制器类中的所有方法都将映射为类级别的请求
    - 为了方便维护程序，建议开发者采用类级别注解，将相关处理放在同一个控制器类中。例如，对商品的增、删、改、查处理方法都可以放在ItemOperate控制类中
  - 请求处理方法:
    - 常用的参数类型：
      - Servlet API类型：
        
        ![image](https://user-images.githubusercontent.com/46510621/111861261-cb570480-8987-11eb-8406-9a08c4d905c8.png)

      - org.springframework.ui.Model类型，该类型是一个包含Map的Spring框架类型：

        ![image](https://user-images.githubusercontent.com/46510621/111861280-ede91d80-8987-11eb-9f51-b735cbee48df.png)
    
    - 常见的返回类型:
      - ModelAndView、Model、View
      - String及其他任意的Java类型
    
## 获取参数

  - 通过实体Bean接收请求参数：
    - 适用于get和post提交请求方式
    - Bean的属性名称必须与请求参数名称相同
    - 示例：
      - 创建POJO实体类：

        ![image](https://user-images.githubusercontent.com/46510621/111861437-394ffb80-8989-11eb-90c1-8a17973fe1ae.png)

      - 创建控制器类:

        ![image](https://user-images.githubusercontent.com/46510621/111861601-81235280-898a-11eb-95f6-8b47688a1989.png)

  - 通过处理方法的形参接收请求参数:
    - 把表单参数写在控制器类相应方法的形参中，即形参名称与请求参数名称完全相同
    - 适用于get和post提交请求方式
  - 通过HttpServletRequest接收请求参数:
    - 适用于get和post提交请求方式
    - 示例：
      
      ![image](https://user-images.githubusercontent.com/46510621/111861647-f1ca6f00-898a-11eb-9853-d645386a2309.png)

  - 通过@PathVariable接收URL中的请求参数：
    - 通过@PathVariable可以将URL中占位符参数绑定到控制器处理方法的入参中
    - 示例：
    
      ![image](https://user-images.githubusercontent.com/46510621/111861696-39e99180-898b-11eb-97be-58f31cc354bf.png)

  - 通过@RequestParam接收请求参数：
    - 将请求参数传递给请求方法
    - 通过@RequestParam接收请求参数与“通过处理方法的形参接收请求参数”的区别：当请求参数与接收参数名不一致时，“通过处理方法的形参接收请求参数”不会报404 错误，而“通过@RequestParam接收请参数”会报404错误。
    - 示例：
    
      ![image](https://user-images.githubusercontent.com/46510621/111861815-1ecb5180-898c-11eb-9c93-359f9290b19f.png)

## 转发与重定向(forward/redirect)（重要）

  - 两者区别：
    - 转发是将用户对当前处理的请求转发给另一个视图或处理请求，以前的request中存放的信息不会失效
    - 重定向是将用户从当前处理请求定向到另一个视图，以前的请求（request）中存放的信息全部失效
  - 转发过程：
    - 客户浏览器发送HTTP请求，Web服务器接受此请求，调用内部的一个方法在容器内部完成请求处理和转发动作，将目标资源发送给客户
    - 转发的路径必须是同一个Web容器下的URL，其不能转向到其他的Web路径上，中间传递的是自己的容器内的request
    - 在客户浏览器的地址栏中显示的仍然是其第一次访问的路径，也就是说客户是感觉不到服务器做了转发的
  - 重定向过程:
    - 客户浏览器发送HTTP请求，Web服务器接受后发送302状态码及对应新的location给客户浏览器，客户浏览器发现是302响应，则自动再发送一个新的HTTP请求，请求URL是新的location地址，服务器根据此请求寻找资源并发送给客户。
    - 这里location可以重定向到任意URL，在客户浏览器的地址栏中显示的是其重定向的路径
  - 示例：

    ![image](https://user-images.githubusercontent.com/46510621/111862022-70c0a700-898d-11eb-89f3-35bc03f7db19.png)

## @ModelAttribute注解的使用

  - 实现功能：
    - 绑定请求参数到实体对象（表单的命令对象）:
    
      ![image](https://user-images.githubusercontent.com/46510621/111862122-25f35f00-898e-11eb-880f-fcb44a293c36.png)

      - 将请求参数的输入封装到user对象中
      - 创建UserForm实例
    - 注解一个非请求处理方法:
      - @ModelAttribute注解的方法将在每次调用该控制器类的请求处理方法前被调用，这种特性可以用来控制登录权限

        ![image](https://user-images.githubusercontent.com/46510621/111862164-7e2a6100-898e-11eb-9e00-78b633fd4c82.png)

## 拦截器（重要）

  - 作用：用于拦截用户的请求并做相应的处理，通常应用在权限验证、记录请求信息的日志、判断用户是否登录等功能上。
  - 定义一个拦截器可以通过两种方式：
    - 通过实现HandlerInterceptor接口或继承HandlerInterceptor接口的实现类
    - 通过实现WebRequestInterceptor接口或继承WebRequestInterceptor接口的实现类
  - 示例：

    ![image](https://user-images.githubusercontent.com/46510621/111863047-0b23e900-8994-11eb-890c-db046e3cdd04.png)

    - preHandle方法：该方法在控制器的处理请求方法前执行，其返回值表示是否中断后续操作，返回true表示继续向下执行，返回false表示中断后续操作
    - postHandle方法：该方法在控制器的处理请求方法调用之后、解析视图之前执行，可以通过此方法对请求域中的模型和视图做进一步的修改
    - afterCompletion方法：该方法在控制器的处理请求方法执行完成后执行，即视图渲染结束后执行，可以通过此方法实现一些资源清理、记录日志信息等工作
  - 拦截器的配置：
    
    ![image](https://user-images.githubusercontent.com/46510621/111863121-7d94c900-8994-11eb-8ca4-355e9ce54d06.png)

  - 拦截器的执行流程：
    - 单个拦截器的执行流程：程序将首先执行拦截器类中的preHandle方法，如果该方法返回true，程序将继续执行控制器中处理请求的方法，否则中断执行。如果preHandle方法返回true，并且控制器中处理请求方法执行后、返回视图前将执行postHandle方法，返回视图后才执行afterCompletion方法。
  - 多个拦截器的执行流程：
    - 在Web应用中通常需要有多个拦截器同时工作，这时它们的preHandle方法将按照配置文件中拦截器的配置顺序执行，而它们的postHandle方法和afterCompletion方法则按照配置顺序的反序执行

## Hibernate-Validator数据验证

  - 配置属性文件与验证器：
    
    ![image](https://user-images.githubusercontent.com/46510621/111864238-b46ddd80-899a-11eb-9567-cf34fbac3872.png)

  - 标注类型：
    - 空检查：
      
      ![image](https://user-images.githubusercontent.com/46510621/111864266-d7988d00-899a-11eb-8c67-2898d7aa29c6.png)

    - boolean检查：
      
      ![image](https://user-images.githubusercontent.com/46510621/111864279-e8490300-899a-11eb-892f-116394f6064e.png)

    - 长度检查：
      
      ![image](https://user-images.githubusercontent.com/46510621/111864291-f860e280-899a-11eb-9112-19e2c0a97727.png)

    - 日期检查：
    
      ![image](https://user-images.githubusercontent.com/46510621/111864302-0878c200-899b-11eb-8c52-80736e39cd3e.png)

    - 数值检查：

      ![image](https://user-images.githubusercontent.com/46510621/111864312-19c1ce80-899b-11eb-9883-b5e0adfbe82f.png)

