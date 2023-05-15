package cn.bsy.cloud.common.oracle.config;

import cn.hutool.core.collection.CollectionUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;

import java.util.*;

/**
 * @Description: 通过配置类的方式将springBoot自动注入关闭
 * @Author gaoh
 * @Date 2023年05月14日 下午 5:02
 **/
@Configuration
public class ExcludeOracleAutoConfiguration implements EnvironmentPostProcessor, Ordered {
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        String key = "spring.autoconfigure.exclude";
        Binder binder = Binder.get(environment);
        List<String> exList = new ArrayList<>();
        // 先获取到原配置文件的信息。参考AutoConfigurationImportSelector#getExcludeAutoConfigurationsProperty
        List<String> stringList = (List)binder.bind(key, String[].class).map(Arrays::asList).orElse(Collections.emptyList());
        exList.addAll(stringList);
        // 增加需要排除的类
        exList.add("org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration");
        exList.add("org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration");
        exList.add("com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration");
        exList.add("com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure");
        MutablePropertySources m = environment.getPropertySources();
        Properties p = new Properties();
        p.put(key, CollectionUtil.join(exList,","));
        // 保存新的配置文件
        m.addFirst(new PropertiesPropertySource("commonDataProperties", p));
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
