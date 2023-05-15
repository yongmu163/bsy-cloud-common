# 组件介绍
认证鉴权组件包，该组件只适用于单体项目。
# 组件使用介绍

## 1、pom文件依赖
```yaml
<!--公共组件包-认证鉴权组件包(单体项目专用)-->
<dependency>
    <groupId>cn.bsy.cloud</groupId>
    <artifactId>bsy-cloud-common-shiro</artifactId>
</dependency>
```
## 2、shiro使用
```java
 public class TestController {
    /**
     * 测试接口
     * @return
     */
    @GetMapping("/code/test")
    // 权限编码集合。允许多个，以 , 分隔
    //logical 指定了多个权限时检查的逻辑操作。默认为AND
    @RequiresPermissions(value = {"895940742687", "895940742687"}, logical = Logical.OR)
    public Result<String> createTestObject(){
        return Result.ok("hahahhahahhaha");
    }
}
```