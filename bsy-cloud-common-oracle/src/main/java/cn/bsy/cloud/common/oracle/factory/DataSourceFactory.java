package cn.bsy.cloud.common.oracle.factory;

import cn.bsy.cloud.common.oracle.config.DruidProperties;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.config.GlobalConfig.DbConfig;
import com.baomidou.mybatisplus.extension.injector.LogicSqlInjector;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import lombok.AllArgsConstructor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import java.io.IOException;

/**
 * @Description: Mp3.x 配置
 * @ClassName: DataSourceFactory
 * @author: gaoheng
 * @date: 2022年11月18日 下午5:39:30
 */
@Configuration
@AllArgsConstructor
@ConditionalOnProperty(prefix = "spring.datasource", name = {"url","username","password"}, matchIfMissing = false)
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class, DruidDataSourceAutoConfigure.class, MybatisPlusAutoConfiguration.class,DruidDataSourceAutoConfigure.class})
public class DataSourceFactory {

    private final DruidProperties druidProperties;

    /**
     * 分页插件
     */
    @Bean("paginationInterceptor")
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        paginationInterceptor.setDialectType("oracle");
        return paginationInterceptor;
    }

    /**
     * sql执行效率插件
     */
    @Bean("performanceInterceptor")
    public PerformanceInterceptor performanceInterceptor() {
        PerformanceInterceptor performanceInterceptor = new PerformanceInterceptor();
        /** 格式化 */
        performanceInterceptor.setFormat(true);
        performanceInterceptor.setWriteInLog(true);
        return performanceInterceptor;
    }

    /**
     * 数据源配置
     */
    @Bean("singleDatasource")
    public DruidDataSource singleDatasource() {
        DruidDataSource dataSource = new DruidDataSource();
        return druidProperties.config(dataSource);
    }

    /**
     * 创建全局配置
     */
    @Bean
    public GlobalConfig mpGlobalConfig() {
        /** 全局配置文件 */
        GlobalConfig globalConfig = new GlobalConfig();
        DbConfig dbConfig = new DbConfig();
        /** 默认为自增 */
        dbConfig.setIdType(IdType.UUID);
        /** 手动指定db 的类型 */
        dbConfig.setDbType(DbType.ORACLE);
        globalConfig.setDbConfig(dbConfig);
        /** 逻辑删除注入器 */
        LogicSqlInjector injector = new LogicSqlInjector();
        globalConfig.setSqlInjector(injector);
        return globalConfig;
    }

    @Bean(name = "singleSqlSessionFactory")
    @DependsOn("singleDatasource")
    public MybatisSqlSessionFactoryBean singleSqlSessionFactory(DruidDataSource singleDatasource,
                                                                GlobalConfig globalConfig, PaginationInterceptor paginationInterceptor,
                                                                PerformanceInterceptor performanceInterceptor) throws IOException {
        return getSessionFactoryBean(singleDatasource, globalConfig, paginationInterceptor, performanceInterceptor);
    }

    @Bean
    public static MapperScannerConfigurer singleMapperScannerConfigurer() {
        MapperScannerConfigurer configurer = new MapperScannerConfigurer();
        configurer.setBasePackage("cn.**.mapper");
        configurer.setSqlSessionFactoryBeanName("singleSqlSessionFactory");
        return configurer;
    }

    private MybatisSqlSessionFactoryBean getSessionFactoryBean(DruidDataSource singleDatasource,
                                                               GlobalConfig globalConfig, PaginationInterceptor paginationInterceptor,
                                                               PerformanceInterceptor performanceInterceptor) throws IOException {
        MybatisSqlSessionFactoryBean sqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
        /** 配置数据源 */
        sqlSessionFactoryBean.setDataSource(singleDatasource);
        /** 全局配置 */
        sqlSessionFactoryBean.setGlobalConfig(globalConfig);
        /** 指明mapper.xml位置 */
        sqlSessionFactoryBean.setMapperLocations(
                new PathMatchingResourcePatternResolver().getResources("classpath*:/mapper/**/**Mapper.xml"));
        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.init(globalConfig);
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        sqlSessionFactoryBean.setConfiguration(configuration);
        sqlSessionFactoryBean.setPlugins(new Interceptor[]{paginationInterceptor, performanceInterceptor});
        return sqlSessionFactoryBean;
    }

    /**
     * 事物管理器
     */
    @Bean
    @DependsOn("singleDatasource")
    public DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(singleDatasource());
    }
}