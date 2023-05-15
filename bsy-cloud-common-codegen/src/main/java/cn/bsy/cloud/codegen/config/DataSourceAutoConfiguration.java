package cn.bsy.cloud.codegen.config;

import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @Description: 自动配置类
 * @Author gaoh
 * @Date 2023年05月13日 下午 4:53
 **/
@AllArgsConstructor
@EnableConfigurationProperties({DataSourceProperties.class})
public class DataSourceAutoConfiguration {
}
