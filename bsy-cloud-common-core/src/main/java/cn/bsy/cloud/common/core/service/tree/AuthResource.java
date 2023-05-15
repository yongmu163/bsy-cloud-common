package cn.bsy.cloud.common.core.service.tree;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 资源信息表
 *
 * @author bell
 * @date 2022-07-11 18:43:06
 */
@Data
public class AuthResource  {
    private static final long serialVersionUID=1L;


    private String id;
    /**
     * 资源编码
     */
    private String resourceCode;

    /**
     * 资源名称
     */
    private String resourceName;

    /**
     * 资源类型： 菜单 01 | 按钮 02
     */
    private String resourceType;

    /**
     * 父ID
     */
    private String parentId;

    /**
     * 路径
     */
    private String path;

    /**
     * 路由名称
     */
    private String name;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 租户ID
     */
    private String tenantId;

    /**
     * 是否隐藏
     */
    private Integer hidden;

    /**
     * 重定向
     */
    private String redirect;

    /**
     * 权限标识
     */
    private String perms;


    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 一直显示
     */
    private Integer alwaysShow;

    /**
     * icon
     */
    private String icon;

    /**
     * 组件名称
     */
    private String component;

    private List<AuthResource> children = new ArrayList<>();

    private Map<String,Object> meta;


}
