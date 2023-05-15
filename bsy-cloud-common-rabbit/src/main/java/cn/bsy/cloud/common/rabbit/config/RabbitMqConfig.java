package cn.bsy.cloud.common.rabbit.config;

import cn.bsy.cloud.common.rabbit.service.IConsumerService;
import cn.bsy.cloud.common.rabbit.service.IProviderService;
import cn.bsy.cloud.common.rabbit.service.IRabbitChannelService;
import cn.bsy.cloud.common.rabbit.service.impl.ConsumerServiceImpl;
import cn.bsy.cloud.common.rabbit.service.impl.ProviderServiceImpl;
import cn.bsy.cloud.common.rabbit.service.impl.RabbitChannelServiceImpl;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

/**
 * @Description: rabbitMq连接配置类
 * @Author gaoh
 * @Date 2023年05月01日 下午 5:26
 **/
@Slf4j
@Configuration
@AllArgsConstructor
@ConditionalOnProperty(prefix = "rabbitmq", name = {"host", "port", "virtualHost", "username", "password"}, matchIfMissing = false)
public class RabbitMqConfig {
    private final RabbitMqProperties rabbitMqProperties;

    @Bean(name = "rabbitMqConnection")
    @ConditionalOnMissingBean(Connection.class)
    public Connection getRabbitMqConnection() {
        // 实例化连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(rabbitMqProperties.getHost());
        connectionFactory.setPort(rabbitMqProperties.getPort());
        connectionFactory.setVirtualHost(rabbitMqProperties.getVirtualHost());
        connectionFactory.setUsername(rabbitMqProperties.getUsername());
        connectionFactory.setPassword(rabbitMqProperties.getPassword());
        try {
            Connection connection = connectionFactory.newConnection();
            return connection;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * 获取rabbitmq信道 接口
     *
     * @param rabbitMqConnection
     * @return
     */
    @Bean(name = "rabbitChannelService")
    @DependsOn("rabbitMqConnection")
    public IRabbitChannelService getRabbitChannelService(Connection rabbitMqConnection) {
        return new RabbitChannelServiceImpl(rabbitMqConnection);
    }

    /**
     * 发送消息接口实现类
     *
     * @return
     */
    @Bean(name = "providerService")
    @DependsOn("rabbitChannelService")
    @ConditionalOnBean({IRabbitChannelService.class})
    public IProviderService getProviderService(IRabbitChannelService rabbitChannelService) {
        return new ProviderServiceImpl(rabbitChannelService);
    }

    /**
     * 注入消息消费接口
     * @return
     */
    @Bean(name = "consumerService")
    @DependsOn("rabbitChannelService")
    @ConditionalOnBean({IRabbitChannelService.class})
    public IConsumerService getConsumerService(IRabbitChannelService rabbitChannelService) {
        return new ConsumerServiceImpl(rabbitChannelService);
    }
}
