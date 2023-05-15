package cn.bsy.cloud.common.shiro.utils;


import cn.bsy.cloud.common.core.constant.CommonConstant;
import cn.bsy.cloud.common.core.utils.ThreadLocalUtils;
import cn.bsy.cloud.common.jwt.dto.LoginInfoDTO;
import lombok.experimental.UtilityClass;

/**
 * @author gaoh
 * @desc 使用ThreadLocal存取认证信息
 * @date 2022年02月06日 上午 11:22
 */
@UtilityClass
public class LoginContext {
    /**
     * 获取线程变量中的令牌信息
     *
     * @return
     */
    public String getToken() {
        return (String) ThreadLocalUtils.getValue(CommonConstant.THREAD_LOCAL_TOKEN);
    }

    /**
     * 获取线程变量中的刷新后令牌信息
     *
     * @return
     */
    public String getRefreshedToken() {
        return (String) ThreadLocalUtils.getValue(CommonConstant.THREAD_LOCAL_TOKEN_REFRESHED);
    }

    /**
     * 获取线程变量中的认证信息
     *
     * @return
     */
    public LoginInfoDTO getLoginInfo() {
        return (LoginInfoDTO) ThreadLocalUtils.getValue(CommonConstant.JWT_LOGIN_INFO_KEY);
    }

    /**
     * 将令牌信息存入线程变量
     *
     * @param token
     */
    public void setToken(String token) {
        ThreadLocalUtils.setValue(CommonConstant.THREAD_LOCAL_TOKEN, token);
    }

    /**
     * 将刷新后的令牌信息存入线程变量
     *
     * @param refreshedToken
     */
    public void setRefreshedToken(String refreshedToken) {
        ThreadLocalUtils.setValue(CommonConstant.THREAD_LOCAL_TOKEN_REFRESHED, refreshedToken);
    }

    /**
     * 将令牌解析后的认证信息存入线程变量
     *
     * @param loginInfo
     */
    public void setLoginInfo(LoginInfoDTO loginInfo) {
        ThreadLocalUtils.setValue(CommonConstant.JWT_LOGIN_INFO_KEY, loginInfo);
    }

    /**
     * 清除线程变量中的令牌信息
     */
    public void clearToken() {
        ThreadLocalUtils.removeValue(CommonConstant.THREAD_LOCAL_TOKEN);
    }

    /**
     * 清除线程变量中的刷新后令牌信息
     */
    public void clearRefreshedToken() {
        ThreadLocalUtils.removeValue(CommonConstant.THREAD_LOCAL_TOKEN_REFRESHED);
    }

    /**
     * 清空线程变量中的认证信息
     */
    public void clearLoginInfo() {
        ThreadLocalUtils.removeValue(CommonConstant.JWT_LOGIN_INFO_KEY);
    }
}