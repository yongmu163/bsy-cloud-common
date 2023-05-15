package cn.bsy.cloud.common.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author gaoh
 * @desc shiroToken封装对象
 * @date 2022年01月13日 上午 11:48
 */
public class Oauth2Token implements AuthenticationToken {
    private final String token;

    public Oauth2Token(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}