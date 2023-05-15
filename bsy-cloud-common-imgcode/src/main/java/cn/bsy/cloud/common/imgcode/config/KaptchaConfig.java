package cn.bsy.cloud.common.imgcode.config;


import cn.bsy.cloud.common.imgcode.service.BaseImageCodeProcessorService;
import cn.bsy.cloud.common.imgcode.service.ImageCodeGeneratorService;
import cn.bsy.cloud.common.imgcode.service.ImageCodeStyleService;
import cn.bsy.cloud.common.imgcode.service.impl.ImageCodeGeneratorServiceImpl;
import cn.bsy.cloud.common.imgcode.service.impl.ImageCodeProcessorServiceImpl;
import cn.bsy.cloud.common.imgcode.service.impl.ImageCodeStyleServiceImpl;
import cn.bsy.cloud.common.redis.utils.RedisOperator;
import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

/**
 * @author gaoh
 * @desc 图片验证码配置类
 * @date 2022年01月27日 上午 11:06
 */
@Configuration
@AllArgsConstructor
public class KaptchaConfig {
    private RedisOperator redisOperator;

    /**
     * 注入
     *
     * @return
     */
    @Bean(name = "imageCodeStyleService")
    @ConditionalOnMissingBean(name = "imageCodeStyleService")
    public ImageCodeStyleService getImageCodeStyleConfig() {
        return new ImageCodeStyleServiceImpl();
    }

    /**
     * 注入图片验证码生成工具
     *
     * @param imageCodeStyleService
     * @return
     */
    @Bean
    @DependsOn({"imageCodeStyleService"})
    public DefaultKaptcha producer(ImageCodeStyleService imageCodeStyleService) {
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        defaultKaptcha.setConfig(imageCodeStyleService.getConfig());
        return defaultKaptcha;
    }

    /**
     * @param producer
     * @return
     */
    @Bean(name = "imageCodeGeneratorService")
    @ConditionalOnMissingBean(name = "imageCodeGeneratorService")
    public ImageCodeGeneratorService getImageCodeGeneratorService(Producer producer) {
        return new ImageCodeGeneratorServiceImpl(producer);
    }

    /**
     * 验证码图片配置类的默认实现，默认实现bean,该bean在单体项目中可自定义实现
     *
     * @return
     */
    @Bean(name = "imageCodeProcessorService")
    @ConditionalOnMissingBean(BaseImageCodeProcessorService.class)
    public BaseImageCodeProcessorService getBaseImageCodeProcessorService() {
        return new ImageCodeProcessorServiceImpl(redisOperator);
    }
}