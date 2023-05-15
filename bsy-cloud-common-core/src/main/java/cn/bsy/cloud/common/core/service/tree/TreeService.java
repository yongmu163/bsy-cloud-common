package cn.bsy.cloud.common.core.service.tree;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;

import java.util.List;

/**
 * @author gaoh
 * @desc 树形数据生成接口
 * @date 2022年01月22日 下午 10:38
 */
public interface TreeService<T,V> {
    /**
     * 生成树形列表
     * @param list
     * @param config
     * @param parentId
     * @return
     */
    List<Tree<V>> buildTreeList(List<T> list, TreeNodeConfig config, V parentId);


    /**
     * 生成树形列表重载
     * @param list
     * @return
     */
    List<AuthResource> buildTreeList(List<AuthResource> list);
}
