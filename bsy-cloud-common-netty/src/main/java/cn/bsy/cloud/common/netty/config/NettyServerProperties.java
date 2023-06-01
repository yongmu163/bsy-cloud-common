package cn.bsy.cloud.common.netty.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: netty服务端配置信息
 * @Author gaoh
 * @Date 2023年05月31日 下午 3:49
 **/
@Data
@Component
@ConfigurationProperties(prefix = "tcp.server")
public class NettyServerProperties implements Serializable {
    /**
     * 端口号列表
     */
    private List<Integer> ports;
    /**
     * 心跳时间
     */
    private int readerIdelTime;
}
