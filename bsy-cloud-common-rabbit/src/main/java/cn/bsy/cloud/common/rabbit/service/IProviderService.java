package cn.bsy.cloud.common.rabbit.service;

import com.rabbitmq.client.Channel;

/**
 * @Description: 消息发送接口
 * @Author gaoh
 * @Date 2023/5/1 0001 下午 7:50
 **/
public interface IProviderService {

    /**
     * 简单模式发送队列，默认 队列持久化；队列不独占；消息完成后不自动删除
     *
     * @param channel
     * @param queueName
     * @param obj
     */
    void publishSimpleMessage(Channel channel, String queueName, Object obj);

    /**
     * 简单模式发送队列，默认 队列持久化；队列不独占；消息完成后不自动删除
     * @param queueName
     * @param obj
     */
    void publishSimpleMessage(String queueName, Object obj);

    /**
     * 广播模式  发送消息，发送消息时不用指定队列，因为消息接收方均为临时创建的队列
     *
     * @param channel
     * @param exchangeName
     * @param obj
     */
    void publishFanoutMessage(Channel channel, String exchangeName, Object obj);


    /**
     * 广播模式  发送消息，发送消息时不用指定队列，因为消息接收方均为临时创建的队列
     * @param exchangeName
     * @param obj
     */
    void publishFanoutMessage(String exchangeName, Object obj);

    /**
     * 网关模式+广播模式  发送消息，发送消息时不用指定队列，因为消息接收方均为临时创建的队列
     *
     * @param exchangeName 交换机名称 同时也是信道名称
     * @param obj
     */
    void publishGatewayFanoutMessage(String exchangeName, Object obj);

    /**
     * 路由模式发送路由消息
     *
     * @param channel
     * @param exchangeName
     * @param routingKey
     * @param obj
     */
    void publishRoutingMessage(Channel channel, String exchangeName, String routingKey, Object obj);

    /**
     * 路由模式发送路由消息
     * @param exchangeName
     * @param routingKey
     * @param obj
     */
    void publishRoutingMessage(String exchangeName, String routingKey, Object obj);

    /**
     * 主题模式
     *
     * @param channel
     * @param exchangeName
     * @param routingKey
     * @param obj
     */
    void publishTopicMessage(Channel channel, String exchangeName, String routingKey, Object obj);

    /**
     * 主题模式
     * @param exchangeName
     * @param routingKey
     * @param obj
     */
    void publishTopicMessage(String exchangeName, String routingKey, Object obj);
}
