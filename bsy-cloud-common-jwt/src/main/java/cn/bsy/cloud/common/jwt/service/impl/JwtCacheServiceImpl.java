package cn.bsy.cloud.common.jwt.service.impl;

import cn.bsy.cloud.common.core.constant.RedisDbIndex;
import cn.bsy.cloud.common.core.constant.TokenExpireDayType;
import cn.bsy.cloud.common.jwt.dto.JwtTokenDTO;
import cn.bsy.cloud.common.jwt.service.IJwtCacheService;
import cn.bsy.cloud.common.redis.utils.RedisOperator;
import cn.hutool.core.util.ObjectUtil;
import lombok.AllArgsConstructor;

/**
 * @Description: JWTredis缓存操作接口实现类
 * @Author gaoh
 * @Date 2022/6/26 0026
 **/
@AllArgsConstructor
public class JwtCacheServiceImpl implements IJwtCacheService {
    /**
     * redis操作服务
     */
    private final RedisOperator redisOperator;


    @Override
    public void setTokenToCache(JwtTokenDTO jwtToken) {
        // 通过token失效期，从枚举中查询token在缓存中的失效期
        Long seconds = TokenExpireDayType.getTokenInCacheExpireDay(jwtToken.getExpireDay()) * 24 * 3600L;
        redisOperator.setObject(RedisDbIndex.REDIS_DB_INDEX_0.getIndex(), jwtToken.getToken(), jwtToken, seconds);
    }

    @Override
    public JwtTokenDTO getTokenFromCache(String tokenStr) {
        return (JwtTokenDTO) redisOperator.getObject(RedisDbIndex.REDIS_DB_INDEX_0.getIndex(),
                tokenStr, JwtTokenDTO.class);
    }

    @Override
    public void removeTokenFromCache(String tokenStr) {
        redisOperator.remove(RedisDbIndex.REDIS_DB_INDEX_0.getIndex(), tokenStr);
    }

    @Override
    public boolean hasToken(String tokenStr) {
        return ObjectUtil.isNotNull(this.getTokenFromCache(tokenStr));
    }
}
