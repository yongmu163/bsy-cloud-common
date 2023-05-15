package cn.bsy.cloud.common.rabbit.service.impl;

import cn.bsy.cloud.common.rabbit.service.AbstractConsumer;
import cn.bsy.cloud.common.rabbit.service.IConsumerService;
import cn.bsy.cloud.common.rabbit.service.IRabbitChannelService;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.rabbitmq.client.Channel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * @Description: 消息消费接口实现类
 * @Author gaoh
 * @Date 2023年05月01日 下午 8:26
 **/
@Slf4j
@Service
@AllArgsConstructor
public class ConsumerServiceImpl implements IConsumerService {

    private IRabbitChannelService rabbitChannelService;

    @Override
    public void consumSimpleMessage(Channel channel, AbstractConsumer consumer, String queueName) {
        try {
            channel.queueDeclare(queueName, true, false, false, null);
            channel.basicConsume(queueName, true, consumer);
        } catch (IOException e) {
            log.error("接收普通消息失败");
        }
    }

    @Override
    public void consumSimpleMessage(AbstractConsumer consumer, String queueName) {
        this.consumSimpleMessage(rabbitChannelService.getRabbitMqChannel(), consumer, queueName);
    }

    @Override
    public void consumFanoutMessage(Channel channel, AbstractConsumer consumer, String exchange, String queueName) {
        try {
            channel.exchangeDeclare(exchange, "fanout");
            if (StrUtil.isBlank(queueName)) {
                // 创建临时队列
                queueName = channel.queueDeclare().getQueue();
            }
            // 将交换机与临时队列绑定
            channel.queueBind(queueName, exchange, "");
            channel.basicConsume(queueName, true, consumer);
        } catch (Exception e) {
            log.error("接收广播消息失败");
        }

    }

    @Override
    public void consumFanoutMessage(AbstractConsumer consumer, String exchangeName, String queueName) {
        this.consumFanoutMessage(rabbitChannelService.getRabbitMqChannel(), consumer, exchangeName, exchangeName);
    }

    @Override
    public void consumFanoutMessage(AbstractConsumer consumer, String exchangeName) {
        this.consumFanoutMessage(rabbitChannelService.getRabbitMqChannel(), consumer, exchangeName, exchangeName);
    }

    @Override
    public void consumGatewayFanoutMessage(Channel channel, AbstractConsumer consumer, String exchangeName) {
        try {
            channel.exchangeDeclare(exchangeName, "fanout");
            // 创建队列,名称就是交换机名称
            String queueName = exchangeName;
            channel.queueDeclare(queueName, true, false, false, null);
            // 将交换机与临时队列绑定
            channel.queueBind(queueName, exchangeName, "");
            channel.basicConsume(queueName, true, consumer);
        } catch (Exception e) {
            log.error("接收广播消息失败");
        }
    }

    @Override
    public void consumRoutingMessage(Channel channel, AbstractConsumer consumer, String exchange, List<String> routingKeyList) {
        consumeRoutingMessage(channel, consumer, exchange, null,"direct", routingKeyList);
    }

    @Override
    public void consumRoutingMessage(AbstractConsumer consumer, String exchangeName, List<String> routingKeyList) {
        this.consumeRoutingMessage(rabbitChannelService.getRabbitMqChannel(), consumer, exchangeName,null, "direct", routingKeyList);
    }


    @Override
    public void consumTopicMessage(Channel channel, AbstractConsumer consumer, String exchangeName, String queueName, List<String> routingKeyList) {
        consumeRoutingMessage(channel, consumer, exchangeName, queueName, "topic", routingKeyList);
    }

    @Override
    public void consumTopicMessage(AbstractConsumer consumer, String exchangeName, String queueName, List<String> routingKeyList) {
        this.consumeRoutingMessage(rabbitChannelService.getRabbitMqChannel(), consumer, exchangeName, queueName,"topic", routingKeyList);
    }

    @Override
    public void consumTopicMessage(AbstractConsumer consumer, String exchangeName, List<String> routingKeyList) {
        this.consumeRoutingMessage(rabbitChannelService.getRabbitMqChannel(), consumer, exchangeName, exchangeName,"topic", routingKeyList);
    }


    private void consumeRoutingMessage(Channel channel, AbstractConsumer consumer, String exchange, String queueName, String routingType, List<String> routingKeyList) {
        try {
            channel.exchangeDeclare(exchange, routingType);
            if (StrUtil.isBlank(queueName)) {
                // 创建临时队列
                queueName = channel.queueDeclare().getQueue();
            }
            if (CollUtil.isEmpty(routingKeyList)) {
                return;
            }
            for (String routingKey : routingKeyList) {
                // 将交换机与临时队列绑定
                channel.queueBind(queueName, exchange, routingKey);
            }
            channel.basicConsume(queueName, true, consumer);
        } catch (IOException e) {
            log.error("接收路由消息失败");
        }
    }
}
