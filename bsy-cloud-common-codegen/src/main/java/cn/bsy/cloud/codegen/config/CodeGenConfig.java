package cn.bsy.cloud.codegen.config;

import cn.bsy.cloud.codegen.constant.DbDriverNameEnum;
import cn.bsy.cloud.codegen.service.ICodeGenreatorService;
import cn.bsy.cloud.codegen.service.impl.CodeGenreatorServiceImpl;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.util.List;

/**
 * @Description: 代码生成器配置类
 * @Author gaoh
 * @Date 2023年05月13日 下午 4:58
 **/
@Configuration
@AllArgsConstructor
@ConditionalOnProperty(prefix = "spring.datasource", name = {"url", "username", "password"}, matchIfMissing = false)
public class CodeGenConfig {

    private DataSourceProperties dataSourceProperties;

    /**
     * 自动注入数据源
     *
     * @return
     */
    @Bean("dataSourceConfig")
    @ConditionalOnMissingBean(DataSourceConfig.class)
    public DataSourceConfig getCodeGenDataSourceConfig() {
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        String url = dataSourceProperties.getUrl();
        String[] urlArray = StrUtil.splitToArray(url, ':');
        dataSourceConfig.setDbType(DbType.getDbType(urlArray[1]));
        // 数据源为 orcale 数据库
        if (dataSourceConfig.getDbType() == DbType.ORACLE) {
            dataSourceConfig.setDriverName(DbDriverNameEnum.ORACLE_DRIVER.getDriverName());
            // 将oracle的用户名作为数据库实例
            dataSourceConfig.setSchemaName(dataSourceProperties.getUserName());
        } else {
            // 数据源为 mysql 数据库
            dataSourceConfig.setDriverName(DbDriverNameEnum.MYSQL_DRIVER.getDriverName());
            String schemaNameStr = urlArray[urlArray.length - 1];
            // 从jdbc链接中，截取mysql数据库库名
            String schemaName = StrUtil.sub(schemaNameStr, schemaNameStr.indexOf("/") + 1, schemaNameStr.indexOf("?"));
            dataSourceConfig.setSchemaName(schemaName);
        }
        dataSourceConfig.setUrl(url);
        dataSourceConfig.setUsername(dataSourceProperties.getUserName());
        dataSourceConfig.setPassword(dataSourceProperties.getPassword());
        ConfigBuilder config = new ConfigBuilder(null, dataSourceConfig, null, null, null);
        List<TableInfo> tableInfoList = config.getTableInfoList();
        return dataSourceConfig;
    }

    /**
     * 自动注入代码生成对象
     *
     * @param dataSourceConfig
     * @return
     */
    @Bean("codeGenreatorService")
    @DependsOn("dataSourceConfig")
    @ConditionalOnMissingBean(ICodeGenreatorService.class)
    public ICodeGenreatorService getCodeGenreatorService(DataSourceConfig dataSourceConfig) {
        return new CodeGenreatorServiceImpl(dataSourceConfig);
    }
}
