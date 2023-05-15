# 组件介绍
该组件是对redis的简单封装，能够减少开发者的学习与使用成本；开发者只需引入组件包，即可通过调用方法进行redis的相关操作。
# 组件使用介绍

## 1、pom文件引入依赖
```
<!-- 公共组件包-缓存数据库组件 -->
<dependency>
    <groupId>cn.bsy.cloud</groupId>
    <artifactId>bsy-cloud-common-redis</artifactId>
</dependency>
```
2、redis配置文件
```yaml
spring:      
  redis:
    database: 0
    host: 192.168.0.212
    port: 6379
    password: xxxx
```
3、redis组件使用
```java
public class TenantServiceImpl extends ServiceImpl<TenantMapper, Tenant> implements ITenantService {
    private final TenantMapper tenantMapper;
    /**
     * redis操作服务
     */
    private final RedisOperator redisOperator;
    
    public void init() {
        List<TenantBO> tenantBoList = getTenants();
        if (CollUtil.isNotEmpty(tenantBoList)) {
            for (TenantBO tenantBO : tenantBoList) {
                redisOperator.setObject(RedisDbIndex.REDIS_DB_INDEX_0.getIndex(), tenantBO.getTenantCode(), tenantBO);
                redisOperator.setObject(RedisDbIndex.REDIS_DB_INDEX_0.getIndex(), tenantBO.getTenantId(), tenantBO);
            }
        }
    }

    public TenantBO getTenantByCode(String tenantCode) {
        return (TenantBO) redisOperator.getObject(RedisDbIndex.REDIS_DB_INDEX_0.getIndex(), tenantCode, TenantBO.class);
    }
    
}
```