package cn.bsy.cloud.common.imgcode.service.impl;


import cn.bsy.cloud.common.core.constant.RedisDbIndex;
import cn.bsy.cloud.common.imgcode.service.BaseImageCodeProcessorService;
import cn.bsy.cloud.common.redis.utils.RedisOperator;
import lombok.AllArgsConstructor;

/**
 * @author gaoh
 * @desc 图片验证码实现类
 * @date 2022年01月27日 下午 9:13
 */
@AllArgsConstructor
public class ImageCodeProcessorServiceImpl extends BaseImageCodeProcessorService {
    private RedisOperator redisOperator;

    @Override
    protected Long getImageCodeExpireSecond() {
        return 60L;
    }

    @Override
    protected void saveImageCodeToCache(String randomCode, String imageCodeStr, Long imageCodeExpireSeconds) {
        redisOperator.set(RedisDbIndex.REDIS_DB_INDEX_13.getIndex(), randomCode, imageCodeStr, imageCodeExpireSeconds);
    }

    @Override
    protected String getImageCodeFromCache(String randomCode) {
        return redisOperator.get(RedisDbIndex.REDIS_DB_INDEX_13.getIndex(), randomCode);
    }

    @Override
    protected void removeImageCodeFromCache(String randomCode) {
        redisOperator.remove(RedisDbIndex.REDIS_DB_INDEX_13.getIndex(),randomCode);
    }
}