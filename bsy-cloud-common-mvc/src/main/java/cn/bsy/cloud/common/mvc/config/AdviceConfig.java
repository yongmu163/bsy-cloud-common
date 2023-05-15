package cn.bsy.cloud.common.mvc.config;



import cn.bsy.cloud.common.mvc.advice.CommonResponseDataAdvice;
import cn.bsy.cloud.common.mvc.advice.GlobalExceptionAdvice;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author gaoh
 * @desc advice注入
 * @date 2022年01月08日 下午 11:45
 */
@Aspect
@Configuration
public class AdviceConfig {
    @Bean
    @ConditionalOnMissingBean(CommonResponseDataAdvice.class)
    public CommonResponseDataAdvice commonResponseDataAdvice() {
        return new CommonResponseDataAdvice();
    }
    @Bean
    @ConditionalOnMissingBean(GlobalExceptionAdvice.class)
    public GlobalExceptionAdvice globalExceptionAdvice() {
        return new GlobalExceptionAdvice();
    }
    /*@Bean
    public TokenAspect tokenAspect() {
        return new TokenAspect();
    }*/
}