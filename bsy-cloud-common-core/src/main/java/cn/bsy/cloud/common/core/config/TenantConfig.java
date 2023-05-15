package cn.bsy.cloud.common.core.config;

/**
 * @author gaoh
 * @desc 租户配置类
 * @date 2022年01月21日 下午 8:17
 */

/**
 * @Configuration
 * @PropertySource(value = "classpath:tenant.properties" , ignoreResourceNotFound = true,encoding = "utf-8")
 * @author  gaoh
 */
public class TenantConfig {
    /*@Autowired
    private Environment environment;
    @Bean
    TenantProperties getSystemProperties() {
        TenantProperties tenantProperties = new TenantProperties();
        tenantProperties.setCode(environment.getProperty("system.code"));
        tenantProperties.setName(environment.getProperty("system.name"));
        return tenantProperties;
    }*/
}