package cn.bsy.cloud.common.file.config;

import cn.bsy.cloud.common.file.service.IGenerateWordService;
import cn.bsy.cloud.common.file.service.impl.GenerateWordServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: 书签操作配置类
 * @Author gaoh
 * @Date 2022年10月23日 下午 6:29
 **/
@Configuration
@AllArgsConstructor
@ConditionalOnProperty(prefix = "minio", name = {"endpoint", "accessKey", "secretKey", "port"}, matchIfMissing = false)
public class BookMarkConfig {

    /**
     * 标签配置类
     * @return
     */
    @Bean(name = "generateWordService")
    @ConditionalOnMissingBean(name = "generateWordService")
    public IGenerateWordService generateWordService() {
        return new GenerateWordServiceImpl();
    }
}
