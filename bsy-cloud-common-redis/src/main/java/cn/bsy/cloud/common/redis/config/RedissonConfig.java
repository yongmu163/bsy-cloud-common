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
        singleServerConfig.setPassword(password);
        // 设置Key和Value以字符串进行读取
        config.setCodec(new StringCodec());
        return Redisson.create(config);
    }
}
