package cn.bsy.cloud.common.jwt.service;

import cn.bsy.cloud.common.jwt.dto.JwtTokenDTO;

/**
 * @Description: JWTredis缓存操作接口
 * @Author gaoh
 * @Date 2022/6/26 0026
 **/
public interface IJwtCacheService {

    /**
     * 向缓存中存储JWTToken对象实例
     * @param jwtToken
     */
    void setTokenToCache(JwtTokenDTO jwtToken);

    /**
     * 从缓存中获取Token
     * @param tokenStr
     * @return
     */
    JwtTokenDTO getTokenFromCache(String tokenStr);

    /**
     * 删除缓存中的token
     * @param tokenStr
     */
    void  removeTokenFromCache(String tokenStr);

    /**
     * 检测缓存中是否存在token
     * @param tokenStr
     * @return
     */
    boolean hasToken(String tokenStr);
}
