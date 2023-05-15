package cn.bsy.cloud.common.shiro.service;

import cn.bsy.cloud.common.jwt.dto.LoginInfoDTO;
import java.util.Set;

/**
 * @author gaoh
 * @desc 用户权限获取接口
 * @date 2022年06月28日 下午 6:27
 */
public interface UserPermissionAndRoleService {
    /**
     * 根据认证信息获取权限编码列表
     * @param loginInfo
     * @return
     */
    Set<String> getUserPermissions(LoginInfoDTO loginInfo);

    /**
     * 根据认证信息获取角色编码列表
     * @param loginInfo
     * @return
     */
    Set<String> getUserRoles(LoginInfoDTO loginInfo);
}