package cn.bsy.cloud.common.mvc.config;


import cn.bsy.cloud.common.mvc.filter.XssFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author gaoh
 * @desc 过滤器配置类
 * @date 2022年01月11日 下午 6:42
 */
@Configuration
public class FilterConfig {
    @Bean
    public XssFilter getXssFilter() {
        XssFilter xssFilter = new XssFilter();
        return xssFilter;
    }
}