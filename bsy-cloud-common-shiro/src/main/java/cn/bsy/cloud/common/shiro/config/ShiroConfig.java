package cn.bsy.cloud.common.shiro.config;


import cn.bsy.cloud.common.jwt.service.IGenerateJwtKeyService;
import cn.bsy.cloud.common.jwt.service.IJwtCacheService;
import cn.bsy.cloud.common.jwt.service.IJwtService;
import cn.bsy.cloud.common.shiro.Oauth2Realm;
import cn.bsy.cloud.common.shiro.filter.Oauth2Filter;
import cn.bsy.cloud.common.shiro.service.InterceptAnonPathService;
import cn.bsy.cloud.common.shiro.service.UserPermissionAndRoleService;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * @author gaoh
 * @desc 配置@AutoConfigureAfter(ShiroLifecycleConfig.class)，否则无法使用将外部Bean进行注入
 * @date 2022年06月13日 下午 12:11
 */
@Configuration
@AutoConfigureAfter(ShiroLifecycleConfig.class)
public class ShiroConfig {
    @Autowired
    private IJwtService jwtService;
    @Autowired
    private IJwtCacheService jwtCacheService;
    @Autowired
    private IGenerateJwtKeyService generateJwtKeyService;
    @Autowired
    private InterceptAnonPathService interceptAnonPathService;
    @Autowired
    private UserPermissionAndRoleService userPermissionAndRoleService;

    /**
     * 用于封装Realm对象
     * @param realm
     * @return
     */
    @Bean("securityManager")
    public SecurityManager securityManager(Oauth2Realm realm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        /** 设置自定义 realm */
        securityManager.setRealm(realm);
        securityManager.setRememberMeManager(null);
        /** 关闭shiro自带的session */
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        securityManager.setSubjectDAO(subjectDAO);
        return securityManager;
    }

    /**
     * 将Filter对象注入进shir框架，并设置拦截策略
     * @param securityManager
     * @return
     */
    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager);
        LinkedHashMap<String, String> filterMap=interceptAnonPathService.getAnonfilterMap();
        Map<String, Filter> filters = new LinkedHashMap<>();
        filters.put("auth", new Oauth2Filter(jwtService, jwtCacheService,generateJwtKeyService));
        shiroFilter.setFilters(filters);
        // 所有请求需要认证
        filterMap.put("/**", "auth");
        shiroFilter.setFilterChainDefinitionMap(filterMap);
        return shiroFilter;
    }
    /**
     * AOP切面，web方法执行前验证权限
     *
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    /**
     * 注入realm
     *
     * @return
     */
    @Bean("realm")
    public Oauth2Realm realm() {
        Oauth2Realm realm = new Oauth2Realm(jwtService,
                generateJwtKeyService,userPermissionAndRoleService);
        return realm;
    }
}