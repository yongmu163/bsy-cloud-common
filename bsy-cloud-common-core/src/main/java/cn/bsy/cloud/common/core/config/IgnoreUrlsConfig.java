package cn.bsy.cloud.common.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author gaoh
 * @desc 从注册中心读取忽略认证的URL配置
 * @date 2022年02月14日 下午 4:12
 *
 * nacos:
 *   ignore-auth
 */
@Data
@Component
@ConfigurationProperties(prefix = "nacos.ignore-auth")
public class IgnoreUrlsConfig {
    private List<String> urls;
}