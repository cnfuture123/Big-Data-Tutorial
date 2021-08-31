# Lombok

## 简介

  - Project Lombok是一个Java类库，可以集成到编译器和构建工具。
  - 它目标是通过一些简单的注解，来替代冗余的，重复出现的代码
  
## 安装

  - Lombok作为一个单独的jar去使用，并包含了开发API，可以用于集成IDE
  - Maven引入依赖：
    ```
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <version>0.9.2</version>
    </dependency>
    ```
    
## Lombok注解

  - @Getter, @Setter:
    - 为字段生成getter, setter方法
  - @NonNull:
    - 表明需要对属性进行空值检查
  - @ToString:
    - 生成toString方法的实现
    - 可以使用exclude参数排除特定的属性
  - @EqualsAndHashCode:
    - 类级别的注解，生成equals和hashCode方法
  - @Data:
    - 类级别的注解，集成了以上4种注解的功能
  - @Cleanup:
    - 确保分配的资源被释放

## 参考

  - https://objectcomputing.com/resources/publications/sett/january-2010-reducing-boilerplate-code-with-project-lombok
