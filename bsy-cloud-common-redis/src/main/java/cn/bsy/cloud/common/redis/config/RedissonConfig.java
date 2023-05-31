package cn.bsy.cloud.common.redis.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: redission配置类
 * @Author gaoh
 * @Date 2023年05月10日 下午 4:15
 **/
@Configuration
@ConditionalOnProperty(prefix = "spring.redis", name = {"host"}, matchIfMissing = false)
public class RedissonConfig {
    @Value("${spring.redis.host}")
    String hostName;
    @Value("${spring.redis.port}")
    int port;
    @Value("${spring.redis.password}")
    String password;
    @Value("${spring.redis.database}")
    int index;

    @Bean(destroyMethod = "shutdown")
    RedissonClient redisson() {
        Config config = new Config();
        SingleServerConfig singleServerConfig = config.useSingleServer();
        String address = new StringBuilder("redis://").append(hostName).append(":").append(port).toString();
        singleServerConfig.setAddress(address);
        // 设置 数据库编号
        singleServerConfig.setDatabase(index);
        // 密码
        singleServerConfig.setPassword(password);
        // 发布和订阅连接的最小空闲连接数
        singleServerConfig.setSubscriptionConnectionMinimumIdleSize(1);
        // 发布和订阅连接池大小
        singleServerConfig.setSubscriptionConnectionPoolSize(50);
        // 最小空闲连接数
        singleServerConfig.setConnectionMinimumIdleSize(32);
        // 连接池大小
        singleServerConfig.setConnectionPoolSize(64);
        // 连接空闲超时，单位：毫秒
        singleServerConfig.setIdleConnectionTimeout(10000);
        // 连接超时，单位：毫秒
        singleServerConfig.setConnectTimeout(10000);
        // 命令等待超时，单位：毫秒
        singleServerConfig.setTimeout(3000);
        // 命令失败重试次数
        singleServerConfig.setRetryAttempts(3);
        // 命令重试发送时间间隔，单位：毫秒
        singleServerConfig.setRetryInterval(1500);
        // 单个连接最大订阅数量
        singleServerConfig.setSubscriptionsPerConnection(5);

        singleServerConfig.setPingConnectionInterval(1000);
        // 设置Key和Value以字符串进行读取
        config.setCodec(new StringCodec());
        return Redisson.create(config);
    }
}
