package cn.bsy.cloud.common.feign.config;


import cn.bsy.cloud.common.feign.interceptor.FeignRequestInterceptor;
import feign.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * @author gaoh
 * @desc OpenFeign 配置类
 * @date 2022年02月09日 下午 1:29
 */

@Configuration
@ConditionalOnClass(Feign.class)
public class FeignConfig {
    public static final int CONNECT_TIMEOUT_MILLS = 5000;
    public static final int READ_TIMEOUT_MILLS = 5000;

    /**
     * <h2>开启 OpenFeign 日志</h2>
     */
    @Bean
    public Logger.Level feignLogger() {
        //  需要注意, 日志级别需要修改成 debug
        return Logger.Level.FULL;
    }

    /**
     * OpenFeign 开启重试
     * period = 100 发起当前请求的时间间隔, 单位是 ms
     * maxPeriod = 1000 发起当前请求的最大时间间隔, 单位是 ms
     * maxAttempts = 5 最多请求次数
     */
    @Bean
    public Retryer feignRetryer() {
        return new Retryer.Default(
                100,
                SECONDS.toMillis(1),
                5
        );
    }

    /**
     * 对请求的连接和响应时间进行限制
     *
     * @return
     */
    @Bean
    public Request.Options options() {
        return new Request.Options(
                CONNECT_TIMEOUT_MILLS, TimeUnit.MICROSECONDS,
                READ_TIMEOUT_MILLS, TimeUnit.MILLISECONDS,
                true
        );
    }

    /**
     * feign请求拦截器
     * @return
     */
    @Bean
    public RequestInterceptor requestInterceptor(){
        return new FeignRequestInterceptor();
    }
}