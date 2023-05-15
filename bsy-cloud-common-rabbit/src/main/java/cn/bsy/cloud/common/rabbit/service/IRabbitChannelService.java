package cn.bsy.cloud.common.rabbit.service;

import com.rabbitmq.client.Channel;

/**
 * @Description: rabbit通道操作接口
 * @Author gaoh
 * @Date 2023/5/1 0001 下午 5:58
 **/
public interface IRabbitChannelService {
    /**
     * 获取匿名mq通道
     *
     * @return
     */
    Channel getRabbitMqChannel();

    /**
     * 获取实名mq通道
     *
     * @param channelName
     * @return
     */
    Channel getRabbitMqChannel(String channelName);

    /**
     * 获取channel名称
     * @param channel
     * @return
     */
    String getRabbitMqChannelName(Channel channel);

    /**
     * 关闭
     * @param channel
     */
    void closeChannel(Channel channel);
}
