package cn.bsy.cloud.common.shiro;


import cn.bsy.cloud.common.core.exception.CustomizeException;
import cn.bsy.cloud.common.jwt.dto.LoginInfoDTO;
import cn.bsy.cloud.common.jwt.service.IGenerateJwtKeyService;
import cn.bsy.cloud.common.jwt.service.IJwtService;
import cn.bsy.cloud.common.shiro.service.UserPermissionAndRoleService;
import cn.bsy.cloud.common.shiro.utils.LoginContext;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.authc.AuthenticationInfo;
import java.util.Set;

/**
 * @author gaoh
 * @desc shiroRealm类
 * @date 2022年06月13日 上午 11:50
 */

@AllArgsConstructor
public class Oauth2Realm extends AuthorizingRealm {
    private final IJwtService jwtService;
    private final IGenerateJwtKeyService generateJwtKeyService;
    private final UserPermissionAndRoleService userPermissionAndRoleService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof Oauth2Token;
    }

    /**
     * 授权方法 （验证权限时调用）
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        /**
         * principalCollection.getPrimaryPrincipal()的信息是在doGetAuthenticationInfo的方法通过
         * 认证操作设置进去的，即本程序的第79行
         */
        LoginInfoDTO loginInfo = (LoginInfoDTO) principalCollection.getPrimaryPrincipal();
        String userId = loginInfo.getUserId();
        // 获取登录用户对应的权限编码集合
        Set<String> permissionsSet = userPermissionAndRoleService.getUserPermissions(loginInfo);
        // 获取登录用户对应的角色编码集合
        Set<String> rolesSet = userPermissionAndRoleService.getUserRoles(loginInfo);
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        if (CollUtil.isNotEmpty(permissionsSet)) {
            info.setStringPermissions(permissionsSet);
        }
        if (CollUtil.isNotEmpty(rolesSet)) {
            info.setRoles(rolesSet);
        }
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String accessToken = (String) token.getPrincipal();
        // 从令牌中获取userID ，然后检测该用户是否被冻结
        LoginInfoDTO loginInfo = jwtService.getLoginInfoToken(accessToken, generateJwtKeyService);
        if (StrUtil.isBlank(loginInfo.getCustId())) {
            throw new CustomizeException("该用户已被封禁");
        }
        // 将认证信息设置到环境变量里，在控制层有可能会用到
        LoginContext.setLoginInfo(loginInfo);
        // 向info对象中添加用户信息和token
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(loginInfo, accessToken, loginInfo.getUserId());
        return info;
    }
}