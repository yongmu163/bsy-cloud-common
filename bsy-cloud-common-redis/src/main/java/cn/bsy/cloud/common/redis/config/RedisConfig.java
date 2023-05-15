package cn.bsy.cloud.common.redis.config;


import cn.bsy.cloud.common.redis.utils.RedisOperator;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.util.StringUtils;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author gaoh
 * @desc redis配置
 * @date 2022年01月11日 下午 11:18
 */
@Configuration
@ConditionalOnProperty(prefix = "spring.redis", name = {"host"}, matchIfMissing = false)
public class RedisConfig {
    @Value("${spring.redis.host}")
    String hostName;
    @Value("${spring.redis.port}")
    int port;
    @Value("${spring.redis.password}")
    String password;
    @Value("${spring.redis.database}")
    int index;

    /**
     * 存取字符串数据类型时使用
     *
     * @return
     */
    @Bean(name = "customStringTemplate")
    public StringRedisTemplate stringRedisTemplate() {
        StringRedisTemplate template = new StringRedisTemplate();
        return (StringRedisTemplate) getRedisTemplate(template);
    }

    /**
     * 注入redis操作bean
     * @param customStringTemplate
     * @return
     */
    @Bean
    @DependsOn({"customStringTemplate"})
    public RedisOperator redisOperator(StringRedisTemplate customStringTemplate) {
        return new RedisOperator(customStringTemplate);
    }

    /**
     * 获取redis实例对象
     *
     * @param template
     * @return
     */
    public RedisTemplate getRedisTemplate(RedisTemplate template) {
        template.setConnectionFactory(connectionFactory());
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer
                = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 指定序列化输入的类型
        om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        //序列化 值时使用此序列化方法
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }

    /**
     * 创建redis连接
     *
     * @return
     */
    public RedisConnectionFactory connectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        //设置redis服务器的host或者ip地址
        redisStandaloneConfiguration.setHostName(hostName);
        //设置redis的服务的端口号
        redisStandaloneConfiguration.setPort(port);
        //设置默认使用的数据库
        redisStandaloneConfiguration.setDatabase(0);
        //设置密码
        if (!StringUtils.isEmpty(password)) {
            redisStandaloneConfiguration.setPassword(RedisPassword.of(password));
        }
        //设置默认使用的数据库
        if (index != 0) {
            redisStandaloneConfiguration.setDatabase(0);
        }
        // 开始构造jedis连接工厂
        JedisClientConfiguration.JedisPoolingClientConfigurationBuilder jpcb =
                (JedisClientConfiguration.JedisPoolingClientConfigurationBuilder) JedisClientConfiguration.builder();
        //指定jedisPoolConifig来修改默认的连接池构造器
        jpcb.poolConfig(poolCofig());
        //通过构造器来构造jedis客户端配置
        JedisClientConfiguration jedisClientConfiguration = jpcb.build();
        //单机配置 + 客户端配置 = jedis连接工厂
        JedisConnectionFactory jedisFactory = new JedisConnectionFactory(redisStandaloneConfiguration
                , jedisClientConfiguration);
        jedisFactory.afterPropertiesSet();
        RedisConnectionFactory factory = jedisFactory;
        return factory;
    }

    /**
     * 初始化连接池
     *
     * @return
     */
    public JedisPoolConfig poolCofig() {
        JedisPoolConfig poolCofig = new JedisPoolConfig();
        // 最小空闲连接数
        poolCofig.setMaxIdle(20);
        // 最大连接数
        poolCofig.setMaxTotal(100);
        //当池内没有可用的连接时，最大等待时间
        poolCofig.setMaxWaitMillis(10000);
        poolCofig.setTestOnBorrow(true);
        poolCofig.setTestOnReturn(true);
        return poolCofig;
    }

}