package cn.bsy.cloud.common.file.config;

import cn.bsy.cloud.common.file.service.IGenerateExcelService;
import cn.bsy.cloud.common.file.service.impl.GenerateExcelServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName ExcelConfiguration
 * @Description excel工具服务配置类
 * @Author wh
 * @Date 2022/11/22
 */
@Configuration
@AllArgsConstructor
public class ExcelConfiguration {
    /** excel工具服务 */
    @Bean("iGenerateExcelService")
    @ConditionalOnMissingBean(GenerateExcelServiceImpl.class)
    public IGenerateExcelService iGenerateExcelService()  {
        return new GenerateExcelServiceImpl();
    }
}
