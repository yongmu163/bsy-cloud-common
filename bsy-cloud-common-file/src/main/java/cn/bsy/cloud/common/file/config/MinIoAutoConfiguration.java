package cn.bsy.cloud.common.file.config;

import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @Description: minIO配置类
 * @Author gaoh
 * @Date 2022年07月20日 下午 4:44
 **/
@AllArgsConstructor
@EnableConfigurationProperties({MinIoClientProperties.class})
public class MinIoAutoConfiguration {
}
