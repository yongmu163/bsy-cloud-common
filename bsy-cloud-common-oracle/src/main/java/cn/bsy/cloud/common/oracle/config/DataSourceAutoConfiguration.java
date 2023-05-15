package cn.bsy.cloud.common.oracle.config;

import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @Description:自动配置类
 * @ClassName:DataSourceAutoConfiguration
 * @Date:2022年11月18日 上午11:47:55
 * @Author:gaoheng
 */
@AllArgsConstructor
@EnableConfigurationProperties({DruidProperties.class})
public class DataSourceAutoConfiguration {
}
