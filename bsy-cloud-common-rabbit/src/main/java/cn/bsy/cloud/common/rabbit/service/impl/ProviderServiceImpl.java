package cn.bsy.cloud.common.rabbit.service.impl;

import cn.bsy.cloud.common.rabbit.service.IProviderService;
import cn.bsy.cloud.common.rabbit.service.IRabbitChannelService;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @Description: 消息发送实现类
 * @Author gaoh
 * @Date 2023年05月01日 下午 7:55
 **/
@Slf4j
@Service
@AllArgsConstructor
public class ProviderServiceImpl implements IProviderService {


    IRabbitChannelService rabbitChannelService;

    @Override
    public void publishSimpleMessage(Channel channel, String queueName, Object obj) {
        try {
            if (ObjectUtil.isNull(obj)) {
                return;
            }
            // 将对象反序列化
            byte[] bytes = JSON.toJSONString(obj).getBytes();
            /**
             * 参数1：队列持久化 true持久化 false 不进行持久化
             * 参数2；队列不独占； true 独占 false 不独占
             * 参数3：消息完成后不自动删除 true 自动删除 false 不自动删除
             */
            channel.queueDeclare(queueName, true, false, false, null);
            // MessageProperties.PERSISTENT_TEXT_PLAIN 代表消息需要持久化
            channel.basicPublish("", queueName, MessageProperties.PERSISTENT_TEXT_PLAIN, bytes);
        } catch (IOException e) {
            log.error("消息发送失败，{}", e);
        }
    }

    @Override
    public void publishSimpleMessage(String queueName, Object obj) {
        this.publishSimpleMessage(rabbitChannelService.getRabbitMqChannel(), queueName, obj);
    }

    @Override
    public void publishFanoutMessage(Channel channel, String exchange, Object obj) {
        if (ObjectUtil.isNull(obj)) {
            return;
        }
        // 将对象反序列化
        byte[] bytes = JSON.toJSONString(obj).getBytes();
        try {
            channel.exchangeDeclare(exchange, "fanout");
            // MessageProperties.PERSISTENT_TEXT_PLAIN 代表消息需要持久化
            channel.basicPublish(exchange, "", MessageProperties.PERSISTENT_TEXT_PLAIN, bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void publishFanoutMessage(String exchangeName, Object obj) {
        this.publishFanoutMessage(rabbitChannelService.getRabbitMqChannel(), exchangeName, obj);
    }

    @Override
    public void publishGatewayFanoutMessage(String exchangeName, Object obj) {
        // 信道名称就是交换机名称
        Channel channel = rabbitChannelService.getRabbitMqChannel(exchangeName);
        this.publishFanoutMessage(channel, exchangeName, obj);
    }

    @Override
    public void publishRoutingMessage(Channel channel, String exchange, String routingKey, Object obj) {
        providerRoutingMessage(channel, exchange, "direct", routingKey, obj);

    }

    @Override
    public void publishRoutingMessage(String exchangeName, String routingKey, Object obj) {
        this.publishRoutingMessage(rabbitChannelService.getRabbitMqChannel(), exchangeName, routingKey, obj);
    }


    @Override
    public void publishTopicMessage(Channel channel, String exchangeName, String routingKey, Object obj) {
        providerRoutingMessage(channel, exchangeName, "topic", routingKey, obj);
    }

    @Override
    public void publishTopicMessage(String exchangeName, String routingKey, Object obj) {
        this.providerRoutingMessage(rabbitChannelService.getRabbitMqChannel(), exchangeName, "topic", routingKey, obj);
    }


    private void providerRoutingMessage(Channel channel, String exchange, String routingTypr, String routingKey, Object obj) {
        try {
            if (ObjectUtil.isNull(obj)) {
                return;
            }
            // 将对象反序列化
            byte[] bytes = JSON.toJSONString(obj).getBytes();
            channel.exchangeDeclare(exchange, routingTypr);
            channel.basicPublish(exchange, routingKey, MessageProperties.PERSISTENT_TEXT_PLAIN, bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
