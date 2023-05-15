package cn.bsy.cloud.common.core.service.tree;

import cn.hutool.core.lang.tree.TreeNodeConfig;

/**
 * @author gaoh
 * @desc 树形数据配置接口
 * @date 2022年01月22日 下午 11:03
 */
public interface TreeNodeConfigService {
    /**
     * 生成树形数据配置信息
     * @param idKeyName
     * @param parentIdKeyName
     * @param deep
     * @param childrenKeyName
     * @return
     */
    TreeNodeConfig getTreeNodeConfig(String idKeyName, String parentIdKeyName,
                                     Integer deep, String childrenKeyName);

    /**
     * 默认属性数据配置信息
     * @return
     */
    TreeNodeConfig getTreeNodeConfig();

}