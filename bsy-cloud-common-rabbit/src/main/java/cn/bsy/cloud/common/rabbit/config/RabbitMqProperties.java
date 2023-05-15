package cn.bsy.cloud.common.rabbit.config;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Description: rabbitmq配置类
 * @Author gaoh
 * @Date 2023年05月01日 下午 4:46
 **/
@Data
@Slf4j
@Component
@ConfigurationProperties(prefix = "rabbitmq")
public class RabbitMqProperties {

    /**
     * rabbitmq地址
     */
    private String host;
    /**
     * rabbitmq端口号
     */
    private Integer port;
    /**
     * 虚拟主机
     */
    private String virtualHost;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
}
