package cn.bsy.cloud.common.rabbit.config;

import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @Description: mq配置类
 * @Author gaoh
 * @Date 2022年05月01日 下午 4:44
 **/
@AllArgsConstructor
@EnableConfigurationProperties({RabbitMqProperties.class})
public class RabbitMqConfiguration {
}
