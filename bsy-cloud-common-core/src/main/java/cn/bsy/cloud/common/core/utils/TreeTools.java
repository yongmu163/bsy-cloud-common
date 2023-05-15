package cn.bsy.cloud.common.core.utils;

import cn.bsy.cloud.common.core.model.BaseTree;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Map;

/**
 * @Description: 树型对象工具类
 * @Author gaoh
 * @Date 2022年09月03日 下午 3:11
 **/
@UtilityClass
public class TreeTools {

    /**
     * 将整数类型转为布尔型
     *
     * @param n
     * @return
     */
    public boolean getInteger2Bollean(Integer n) {
        return ((n != null) && n == 1) ? true : false;
    }

    /**
     * 获取树对象配置
     *
     * @param deep     递归深度
     * @param sortName 排序名称
     * @param leafName 叶子节点名称
     * @return
     */
    public TreeNodeConfig getTreeNodeConfig(String parentIdName, Integer deep, String sortName, String leafName) {
        // 配置
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        // 自定义属性名（修改默认名称）
        treeNodeConfig.setWeightKey(sortName);
        treeNodeConfig.setChildrenKey(leafName);
        treeNodeConfig.setParentIdKey(parentIdName);
        // 最大递归深度
        treeNodeConfig.setDeep(deep);
        return treeNodeConfig;
    }

    /**
     * 返回默认数对象配置
     *
     * @return
     */
    public TreeNodeConfig getTreeNodeConfig() {
        // 配置
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        // 自定义属性名（修改默认名称）
        treeNodeConfig.setWeightKey("sort");
        treeNodeConfig.setChildrenKey("children");
        treeNodeConfig.setParentIdKey("pid");
        // 最大递归深度
        treeNodeConfig.setDeep(10);
        return treeNodeConfig;
    }


    /**
     * 构建单个树对象节点
     *
     * @param id       树对象当前主键ID
     * @param parentId 树对象上级主键ID
     * @param name     节点名称
     * @param sort     节点顺序
     * @param extraMap 节点对象扩展内容
     * @return
     */
    public TreeNode<String> buildTreeNode(String id, String parentId, String name, Integer sort, Map<String, Object> extraMap) {
        // 单个树数据构建
        TreeNode<String> treeNode = new TreeNode<String>()
                // 主键
                .setId(String.valueOf(id))
                // 父节点ID
                .setParentId(String.valueOf(parentId))
                // 树节点名称
                .setName(name)
                //权重，排序
                .setWeight(sort)
                // 扩展字段
                .setExtra(extraMap);
        return treeNode;
    }

    /**
     * 构建多级树对象节点列表
     *
     * @param baseTreeList
     * @return
     */
    public List<TreeNode<String>> buildTreeNodeList(List<BaseTree> baseTreeList) {
        // 构建的整个树数据
        List<TreeNode<String>> treeNodeList = CollUtil.newArrayList();
        for (BaseTree baseTree : baseTreeList) {
            treeNodeList.add(buildTreeNode(baseTree.getId(), baseTree.getPid(),
                    baseTree.getName(), baseTree.getSort(),
                    baseTree.getExtraMap()));
        }
        return treeNodeList;
    }

    /**
     * 获取树型对象列表
     *
     * @param treeNodeList 树型对象节点列表
     * @return
     */
    public List<Tree<String>> getTreeList(List<TreeNode<String>> treeNodeList) {
        return getTreeList(treeNodeList, "0", null);
    }

    /**
     * 获取树型对象列表
     *
     * @param treeNodeList   树型对象节点列表
     * @param firstParentId  最上层父节点ID
     * @param treeNodeConfig 树对象配置
     * @return
     */
    public List<Tree<String>> getTreeList(List<TreeNode<String>> treeNodeList, String firstParentId, TreeNodeConfig treeNodeConfig) {
        if (ObjectUtil.isNull(treeNodeConfig)) {
            treeNodeConfig = getTreeNodeConfig();
        }
        List<Tree<String>> treeNodes = TreeUtil.build(treeNodeList, firstParentId, treeNodeConfig,
                (treeNode, tree) -> {
                    // 给树节点赋值（还能set 父 或子节点树）
                    tree.setId(treeNode.getId());
                    tree.setParentId(treeNode.getParentId());
                    tree.setWeight(treeNode.getWeight());
                    tree.setName(treeNode.getName());
                    // 扩展属性值赋值
                    for (Map.Entry<String, Object> entry : treeNode.getExtra().entrySet()) {
                        tree.putExtra(entry.getKey(), ObjectUtil.isNotNull(entry.getValue()) ? entry.getValue() : null);
                    }
                });
        return treeNodes;
    }

}
