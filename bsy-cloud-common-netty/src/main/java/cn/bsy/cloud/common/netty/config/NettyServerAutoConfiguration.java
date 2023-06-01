package cn.bsy.cloud.common.netty.config;

import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @Description: 自动配置类
 * @Author gaoh
 * @Date 2023年05月31日 下午 4:35
 **/
@AllArgsConstructor
@EnableConfigurationProperties({NettyServerProperties.class})
public class NettyServerAutoConfiguration {
}
