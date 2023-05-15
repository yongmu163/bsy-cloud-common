package cn.bsy.cloud.common.rabbit.service.impl;


import cn.bsy.cloud.common.rabbit.service.IRabbitChannelService;
import cn.hutool.core.util.ObjectUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: rabbit通道操作接口实现类
 * @Author gaoh
 * @Date 2023年05月01日 下午 6:09
 **/
@Slf4j
public class RabbitChannelServiceImpl implements IRabbitChannelService {
    private Connection rabbitMqConnection;

    private static ConcurrentHashMap<String, Channel> channelMap = new ConcurrentHashMap<>();

    public RabbitChannelServiceImpl(Connection rabbitMqConnection) {
        this.rabbitMqConnection = rabbitMqConnection;
    }

    @Override
    public Channel getRabbitMqChannel() {
        try {
            return rabbitMqConnection.createChannel();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Channel getRabbitMqChannel(String channelName) {
        Channel channel = channelMap.get(channelName);
        if (ObjectUtil.isNull(channel)) {
            channel = this.getRabbitMqChannel();
            channelMap.put(channelName, channel);
        }
        return channel;
    }

    @Override
    public String getRabbitMqChannelName(Channel channel) {
        for (String key : channelMap.keySet()) {
            Channel channelObj = channelMap.get(key);
            if (ObjectUtil.equal(channel, channelObj)) {
                return key;
            }
        }
        return null;
    }

    @Override
    public void closeChannel(Channel channel) {
        if (ObjectUtil.isNotNull(channel)) {
            try {
                channel.close();
                this.rabbitMqConnection.close();
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }
}
