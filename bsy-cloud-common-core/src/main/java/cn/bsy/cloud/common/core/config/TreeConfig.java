package cn.bsy.cloud.common.core.config;


import cn.bsy.cloud.common.core.service.tree.TreeNodeConfigService;
import cn.bsy.cloud.common.core.service.tree.TreeService;
import cn.bsy.cloud.common.core.service.tree.impl.TreeNodeConfigServiceImpl;
import cn.bsy.cloud.common.core.service.tree.impl.TreeServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author gaoh
 * @desc 树形数据配置类
 * @date 2022年01月22日 下午 11:38
 */
@Configuration
public class TreeConfig {
    /**
     * 注入树形数据生成实现
     * @return
     */
    @Bean(value = "treeService")
    @ConditionalOnMissingBean(name = "treeService")
    TreeService getTreeService() {
        TreeService treeService = new TreeServiceImpl();
        return treeService;
    }

    /**
     * 注入树形数据配置实现
     * @return
     */
    @Bean(value = "treeNodeConfigService")
    @ConditionalOnMissingBean(name = "treeNodeConfigService")
    TreeNodeConfigService getTreeNodeConfigService() {
        TreeNodeConfigService treeNodeConfigService = new TreeNodeConfigServiceImpl();
        return treeNodeConfigService;
    }
}