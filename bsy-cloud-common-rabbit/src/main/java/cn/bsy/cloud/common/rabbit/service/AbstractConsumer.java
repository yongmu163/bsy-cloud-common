package cn.bsy.cloud.common.rabbit.service;

import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.ShutdownSignalException;

import java.io.IOException;

/**
 * @Description: 消费者抽象类
 * @Author gaoh
 * @Date 2023年05月01日 下午 10:12
 **/
public abstract class AbstractConsumer implements Consumer {
    @Override
    public void handleConsumeOk(String s) {

    }

    @Override
    public void handleCancelOk(String s) {

    }

    @Override
    public void handleCancel(String s) throws IOException {

    }

    @Override
    public void handleShutdownSignal(String s, ShutdownSignalException e) {

    }

    @Override
    public void handleRecoverOk(String s) {

    }
}
