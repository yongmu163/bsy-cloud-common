package cn.bsy.cloud.common.mysql.config;

import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @Description:自动配置类
 * @ClassName:DataSourceAutoConfiguration
 * @Date:2022年6月22日 上午11:47:55
 * @Author:gaoheng
 */
@AllArgsConstructor
@EnableConfigurationProperties({DruidProperties.class})
public class DataSourceAutoConfiguration {
}
