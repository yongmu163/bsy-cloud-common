# 组件介绍
common-oracle组件是对引入oracle的一个封装。该组件主要是对分页、数据源、事务、全局配置及sql执行效率插件等进行封装处理，从而减少开发者的学习与使用成本。该组件需与mybatis-plus组件同时使用。
# 组件使用介绍

## 1、pom文件引入
```yaml
<!-- 公共组件包-数据持久层组件 -->
<dependency>
    <groupId>cn.bsy.cloud</groupId>
    <artifactId>bsy-cloud-common-oracle</artifactId>
</dependency>
```
## 2、oracle连接配置
在yaml文件中添加如下配置：
```yaml
spring
  datasource:
    driverClassName: oracle.jdbc.driver.OracleDriver
    url: jdbc:oracle:thin:@192.168.0.213:1521:ORCL
    username: xxxx
    password: xxxx
```
## 3、model类需集成BaseMode类
```java
@Data
@TableName("tb_auth_cust_user")
@EqualsAndHashCode(callSuper = true)
public class AuthCustUser extends BaseModel<AuthCustUser> {
    private static final long serialVersionUID=1L;

        
    /**
     * 客户ID
     */
    @TableField("cust_id")
    private String custId;
        
    /**
     * 用户ID
     */
    @TableField("user_id")
    private String userId;
       
```
