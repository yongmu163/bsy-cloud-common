package cn.bsy.cloud.common.jwt.service.impl;

import cn.bsy.cloud.common.core.constant.CommonConstant;
import cn.bsy.cloud.common.core.constant.RedisDbIndex;
import cn.bsy.cloud.common.core.utils.RsaUtils;
import cn.bsy.cloud.common.jwt.service.IGenerateJwtKeyService;
import cn.bsy.cloud.common.redis.utils.RedisOperator;
import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;

import javax.annotation.PostConstruct;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Map;

/**
 * @author gaoh
 * @desc 生成用于构建jwt的公钥私钥对，并存入缓存redis
 * @date 2022年06月18日 下午 2:29
 */

@AllArgsConstructor
public class GenerateJwtKeyOnRedisServiceImpl implements IGenerateJwtKeyService {
    /**
     * redis操作服务
     */
    private final RedisOperator redisOperator;

    @PostConstruct
    public void init() {
        Map<String, String> keyMap = RsaUtils.generateKey();
        if (StrUtil.isBlank(redisOperator.get(RedisDbIndex.REDIS_DB_INDEX_13.getIndex(), CommonConstant.PUBLIC_KEY))
                || StrUtil.isBlank(redisOperator.get(RedisDbIndex.REDIS_DB_INDEX_14.getIndex(), CommonConstant.PRIVATE_KEY))) {
            redisOperator.set(RedisDbIndex.REDIS_DB_INDEX_13.getIndex(), CommonConstant.PUBLIC_KEY, keyMap.get(CommonConstant.PUBLIC_KEY));
            redisOperator.set(RedisDbIndex.REDIS_DB_INDEX_14.getIndex(), CommonConstant.PRIVATE_KEY, keyMap.get(CommonConstant.PRIVATE_KEY));
        }
    }

    /**
     * 从缓存中获取私钥key
     *
     * @return
     */
    private String getPrivateKeyStrFromRedis() {
        return redisOperator.get(RedisDbIndex.REDIS_DB_INDEX_14.getIndex(), CommonConstant.PRIVATE_KEY);
    }

    /**
     * 从缓存中获取公钥key
     *
     * @return
     */
    private String getPublicKeyStrFromredis() {
        return redisOperator.get(RedisDbIndex.REDIS_DB_INDEX_13.getIndex(), CommonConstant.PUBLIC_KEY);
    }

    @Override
    public PrivateKey getPrivateKey() {
        return RsaUtils.generatePrivateKey(getPrivateKeyStrFromRedis());
    }

    @Override
    public PublicKey getPublicKey() {
        return RsaUtils.generatePublicKey(getPublicKeyStrFromredis());
    }
}