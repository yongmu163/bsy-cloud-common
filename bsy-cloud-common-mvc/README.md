# 组件介绍
common-mvc组件主要是针对web项目的通用配置组件包。包含功能如下：

1、统一响应处理，即response返回结果统一处理为如下格式：

```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```
2、全局异常捕获处理

3、跨域配置

4、http请求抵御跨站脚本攻击

5、Model与DTO互相转换

6、分页显示对象

# 组件使用介绍
引入common-mvc组件后，即可使用，无需特殊处理。
## 1、pom引入
```yaml
<!--公共组件包-web项目通用配置组件包-->
<dependency>
    <groupId>cn.bsy.cloud</groupId>
    <artifactId>bsy-cloud-common-mvc</artifactId>
</dependency>
```


