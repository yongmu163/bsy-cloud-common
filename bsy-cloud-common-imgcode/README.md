# 组件介绍
图片验证码组件包
# 组件使用介绍

## 1、pom文件引入
```yaml
<!-- 公共组件包-图片验证码组件包 -->
<dependency>
    <groupId>cn.bsy.cloud</groupId>
    <artifactId>bsy-cloud-common-imgcode</artifactId>
</dependency>
```
2、imageCode组件使用
```java
public class ValidateCodeController {
    /**
     * 验证码服务
     */
    private final BaseImageCodeProcessorService imageCodeProcessorService;
    /**
     * 验证码生成器
     */
    private final ImageCodeGeneratorService imageCodeGeneratorService;

    /**
     * 图片验证码
     * @param uniqueCode 前端应用生成的随机数
     * @throws IOException
     */
    @GetMapping("/code/img/{uniqueCode}")
    public void createValidateCode(@PathVariable("uniqueCode") String uniqueCode) throws IOException {
        ServletWebRequest servlet = new ServletWebRequest(WebUtils.getRequest(), WebUtils.getResponse());
        imageCodeProcessorService.create(servlet, uniqueCode, imageCodeGeneratorService);
    }
}
```