# 组件介绍
认证令牌配置组件包
# 组件使用介绍

## 1、pom文件引入
```yaml
<!--公共组件包-认证令牌配置组件包-->
<dependency>
    <groupId>cn.bsy.cloud</groupId>
    <artifactId>bsy-cloud-common-jwt</artifactId>
</dependency>
```
2、jwt组件使用
```java
public class UserServiceImpl {
    /**
     * 认证令牌操作服务
     */
    IJwtService jwtService;
    /**
     * 用于生成认证令牌的密钥对服务
     */
    IGenerateJwtKeyService generateJwtKeyOnRedisService;
    /**
     * 令牌缓存操作服务
     */
    IJwtCacheService jwtCacheService;

    /**
     * 生成用户认证token
     * @return
     */
    private JwtTokenDTO genUserTypeToken() {
        LoginInfoDTO loginInfo = new LoginInfoDTO("userId", "custId", "tenantId");
        JwtTokenDTO jwtToken = jwtService.generateToken(loginInfo, generateJwtKeyOnRedisService,
                new JwtObjectInitServiceImpl(TokenTypeEnum.USER_TYPE, TokenExpireDayType.TOKEN_EXPIRE_DAY_TYPE_1D_5D_DEFAULT));
        // 将token存入缓存
        jwtCacheService.setTokenToCache(jwtToken);
        return jwtToken;
    }
}
```