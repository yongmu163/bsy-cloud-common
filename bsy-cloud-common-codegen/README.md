# 组件介绍
代码生成器组件包，用于快速生成后端java代码。
# 使用介绍

## 1、pom文件依赖
```java
<!-- 公共组件包-代码生成器组件 -->
<dependency>
	<groupId>cn.bsy.cloud</groupId>
	<artifactId>bsy-cloud-common-codegen</artifactId>
</dependency>
```
## 2、使用说明

引入maven依赖包后，启动当前项目，在浏览器中输入下方地址即可：

```java
http://IP地址:端口/项目前缀/code/gen?packageName=cn.bsy.cloud&author=admin&moduleName=web&tableName=app_user
```

