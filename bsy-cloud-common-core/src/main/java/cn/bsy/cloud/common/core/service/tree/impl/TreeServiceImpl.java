package cn.bsy.cloud.common.core.service.tree.impl;


import cn.bsy.cloud.common.core.service.tree.AuthResource;
import cn.bsy.cloud.common.core.service.tree.TreeService;
import cn.bsy.cloud.common.core.utils.ObjectUtils;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author gaoh
 * @desc 树形数据生成接口实现类
 * @date 2022年01月22日 下午 11:21
 */
public class TreeServiceImpl<T, V> implements TreeService<T, V> {

    /**
     * 如果不想全部字段可以按照下面的写法添加
     * tree.putExtra("id", object.getId());
     * tree.putExtra("parentId", object.getParentId());
     * tree.putExtra("cityName", object.getCityName());
     */
    @Override
    public List<Tree<V>> buildTreeList(List<T> list, TreeNodeConfig config, V parentId) {
        List<Tree<V>> build = TreeUtil.build(list, parentId, config, (object, tree) -> {
            // 也可以使用 tree.setId(object.getId());等一些默认值
            Field[] fields = ReflectUtil.getFieldsDirectly(object.getClass(), true);
            for (Field field : fields) {
                String fieldName = field.getName();
                Object fieldValue = ReflectUtil.getFieldValue(object, field);
                tree.putExtra(fieldName, fieldValue);

            }
        });
        return build;
    }

    @Override
    public List<AuthResource> buildTreeList(List<AuthResource> list) {
        List<AuthResource> authResourcesList = ObjectUtils.cloneObjList(list, AuthResource.class);;
        List<AuthResource> firstAuthResourceList = authResourcesList.stream().filter
                         (s -> StrUtil.isEmpty(s.getParentId()))
                .collect(Collectors.toList());
        for (AuthResource resource : firstAuthResourceList) {
            getChildrenList(resource,authResourcesList);
        }
        return firstAuthResourceList;
    }


    public AuthResource getChildrenList(AuthResource authResource, List<AuthResource> authResourceList) {
        Map<String ,Object> map =null;
        for (AuthResource resource : authResourceList) {
            map =new HashMap<>(10);
            map.put("title",resource.getResourceName());
            map.put("icon",resource.getIcon());
            resource.setMeta(map);
            if (authResource.getId().equals(resource.getParentId())) {
                authResource.getChildren().add(getChildrenList(resource, authResourceList));
            }
        }
        return authResource;
    }


}
