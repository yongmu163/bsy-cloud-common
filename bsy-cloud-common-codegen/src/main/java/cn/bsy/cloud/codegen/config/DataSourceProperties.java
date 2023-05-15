package cn.bsy.cloud.codegen.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Description: 數據源配置類
 * @Author gaoh
 * @Date 2023年05月13日 下午 4:26
 **/
@Data
@Slf4j
@Component
@ConfigurationProperties(prefix = "spring.datasource")
public class DataSourceProperties {
    /**
     * 数据库连接
     */
    String url;
    /**
     * 用户名
     */
    String userName;
    /**
     * 密码
     */
    String password;
    /**
     * 数据库名称
     */
    String schemaName;
}


