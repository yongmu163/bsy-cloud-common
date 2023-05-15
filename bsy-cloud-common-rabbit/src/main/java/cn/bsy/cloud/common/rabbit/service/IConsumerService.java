package cn.bsy.cloud.common.rabbit.service;

import com.rabbitmq.client.Channel;

import java.util.List;

/**
 * @Description: 消费者接口
 * @Author gaoh
 * @Date 2023/5/1 0001 下午 8:22
 **/
public interface IConsumerService {

    /**
     * 简单模式消费消息
     *
     * @param channel
     * @param consumer
     * @param queueName
     * @return
     */
    void consumSimpleMessage(Channel channel, AbstractConsumer consumer, String queueName);

    /**
     * 简单模式消费消息
     * @param consumer
     * @param queueName
     */
    void consumSimpleMessage(AbstractConsumer consumer, String queueName);

    /**
     * 消费广播消息
     * 当queueName与exchangeName一致时，多消费者同时消费一条消息，不会重复消费
     * queueName为空时，创建临时队列
     * @param channel
     * @param consumer
     * @param exchangeName
     * @param queueName
     */
    void consumFanoutMessage(Channel channel, AbstractConsumer consumer, String exchangeName, String queueName);

    /**
     * 消费广播消息
     * 当queueName与exchangeName一致时，多消费者同时消费一条消息，不会重复消费
     * queueName为空时，创建临时队列
     *
     * @param consumer
     * @param exchangeName
     * @param queueName
     */
    void consumFanoutMessage(AbstractConsumer consumer, String exchangeName, String queueName);

    /**
     * 消费广播消息
     * 默认queueName与exchangeName值相同
     * @param consumer
     * @param exchangeName
     */
    void consumFanoutMessage(AbstractConsumer consumer, String exchangeName);

    /**
     * 网关模式+广播模式 消费消息
     * @param channel
     * @param consumer
     * @param exchangeName 根据交换机名称获取信道和临时队列
     */
    void consumGatewayFanoutMessage(Channel channel,AbstractConsumer consumer, String exchangeName);

    /**
     * 根据路由key获取消息
     * @param channel
     * @param consumer
     * @param exchangeName
     * @param routingKeyList
     */
    void consumRoutingMessage(Channel channel, AbstractConsumer consumer,
                              String exchangeName, List<String> routingKeyList);

    /**
     * 根据路由key获取消息
     * @param consumer
     * @param exchangeName
     * @param routingKeyList
     */
    void consumRoutingMessage(AbstractConsumer consumer,
                              String exchangeName, List<String> routingKeyList);



    /**
     * 根据路由key获取消息,支持通配符
     * 当queueName与exchangeName一致时，多消费者同时消费一条消息，不会重复消费
     * queueName为空时，创建临时队列
     *
     * @param channel
     * @param consumer
     * @param exchangeName
     * @param queueName
     * @param routingKeyList
     */
    void consumTopicMessage(Channel channel, AbstractConsumer consumer,
                              String exchangeName, String queueName, List<String> routingKeyList);

    /**
     * 根据路由key获取消息,支持通配符
     * 当queueName与exchangeName一致时，多消费者同时消费一条消息，不会重复消费
     * queueName为空时，创建临时队列
     *
     * @param consumer
     * @param exchangeName
     * @param queueName
     * @param routingKeyList
     */
    void consumTopicMessage(AbstractConsumer consumer,
                            String exchangeName, String queueName, List<String> routingKeyList);

    /**
     * 根据路由key获取消息,支持通配符
     * 默认queueName值与exchangeName值相同
     * @param consumer
     * @param exchangeName
     * @param routingKeyList
     */
    void consumTopicMessage( AbstractConsumer consumer,
                            String exchangeName, List<String> routingKeyList);
}
