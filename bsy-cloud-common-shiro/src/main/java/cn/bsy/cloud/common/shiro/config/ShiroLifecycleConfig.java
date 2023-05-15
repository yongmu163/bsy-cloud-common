package cn.bsy.cloud.common.shiro.config;

import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author gaoh
 * @desc Shiro生命周期处理器
 * @date 2022年06月28日 下午 3:37
 */
@Configuration
public class ShiroLifecycleConfig {

    /**
     * Shiro生命周期处理器
     *
     * @return
     */
    @Bean(name = "lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

}