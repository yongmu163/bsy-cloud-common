package cn.bsy.cloud.common.core.constant;

/**
 * @author gaoh
 * @desc 全局常量
 * @date 2022年01月11日 下午 6:13
 */

public class CommonConstant {
    /**
     * 编码类型
     */
    public static final String CHARSET = "UTF-8";
    /**
     * RSA公钥
     */
    public static final String PUBLIC_KEY = "publicKey";
    /**
     * RSA私钥
     */
    public static final String PRIVATE_KEY = "privateKey";
    /**
     * JWT 中存储用户信息的 key
     */
    public static final String JWT_LOGIN_INFO_KEY = "login-info";
    /**
     * 保存在线程变量中的token key值
     */
    public static final String THREAD_LOCAL_TOKEN = "token";
    /**
     * 保存在线程变量中刷新后的token key值
     */
    public static final String THREAD_LOCAL_TOKEN_REFRESHED = "refreshedToken";

}