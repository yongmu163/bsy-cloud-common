package cn.bsy.cloud.common.core.config;


import cn.bsy.cloud.common.core.service.rest.RestTemplateService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author gaoh
 * @desc http请求bean自动注入
 * @date 2022年01月30日 下午 6:02
 */
@Configuration
public class RestConfig {
    /**
     * 注入RestTemplate
     * @return
     */
    @Bean
    @LoadBalanced
    @ConditionalOnMissingBean(name = "restTemplate")
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * 注入rest请求工具服务
     *
     * @param restTemplate
     * @return
     */
    @Bean("restTemplateService")
    RestTemplateService getRestTemplateService(RestTemplate restTemplate) {
        return new RestTemplateService(restTemplate);
    }
}