package cn.bsy.cloud.common.jwt.config;


import cn.bsy.cloud.common.jwt.service.IGenerateJwtKeyService;
import cn.bsy.cloud.common.jwt.service.IJwtCacheService;
import cn.bsy.cloud.common.jwt.service.IJwtService;
import cn.bsy.cloud.common.jwt.service.impl.*;
import cn.bsy.cloud.common.redis.utils.RedisOperator;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

/**
 * @author gaoh
 * @desc 通用配置组件包配置类
 * @date 2022年01月12日 下午 5:07
 */
@Configuration
@AllArgsConstructor
public class CommonConfig {
    /**
     * redis缓存
     */
    RedisOperator redisOperator;

    /**
     * 注入密钥对生成服务，存入缓存中
     *
     * @return
     */
    @Bean(name = "generateJwtKeyService")
    @ConditionalOnMissingBean(name = "generateJwtKeyService")
    public IGenerateJwtKeyService generateJwtKeyService() {
        return new GenerateJwtKeyServiceImpl();
    }

    /**
     * 注入jwt服务
     *
     * @return
     */
    @Bean(name = "jwtService")
    @ConditionalOnMissingBean(name = "jwtService")
    public IJwtService getJwtService() {
        return new JwtServiceImpl();
    }

    /**
     * 注入jwt缓存操作服务
     *
     * @return
     */
    @Bean(name = "jwtCacheService")
    @ConditionalOnMissingBean(name = "jwtCacheService")
    public IJwtCacheService getJwtCacheService() {
        return new JwtCacheServiceImpl(redisOperator);
    }
}