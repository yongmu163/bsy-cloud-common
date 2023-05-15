package cn.bsy.cloud.common.core.service.tree.impl;


import cn.bsy.cloud.common.core.service.tree.TreeNodeConfigService;
import cn.hutool.core.lang.tree.TreeNodeConfig;

/**
 * @author gaoh
 * @desc 树形数据配置接口实现类
 * @date 2022年01月22日 下午 11:07
 */
public class TreeNodeConfigServiceImpl implements TreeNodeConfigService {
    /**
     * 获取树型数据配置
     * @param idKeyName
     * @param parentIdKeyName
     * @param deep
     * @param childrenKeyName
     * @return
     */
    @Override
    public TreeNodeConfig getTreeNodeConfig(String idKeyName, String parentIdKeyName, Integer deep, String childrenKeyName) {
        TreeNodeConfig config = new TreeNodeConfig();
        // 默认为id可以不设置
        config.setIdKey(idKeyName);
        // 默认为parentId可以不设置
        config.setParentIdKey(parentIdKeyName);
        // 最大递归深度
        config.setDeep(deep);
        // 默认为children不用设置
        config.setChildrenKey(childrenKeyName);
        return config;
    }

    @Override
    public TreeNodeConfig getTreeNodeConfig() {
        return this.getTreeNodeConfig("id","parentId",4,"children");
    }
}