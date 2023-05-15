package cn.bsy.cloud.common.redis.utils;

import com.alibaba.fastjson.JSON;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @author gaoh
 * @desc redis操作
 * @date 2022年01月11日 下午 11:56
 */

public class RedisOperator {
    /**
     * 适用于存取普通字符串类型数据
     */
    private StringRedisTemplate customStringTemplate;

    public RedisOperator(StringRedisTemplate customStringTemplate) {
        this.customStringTemplate = customStringTemplate;
    }

    /**
     * 切换redis数据库
     *
     * @param index 下标
     * @return
     */
    public StringRedisTemplate getStringRedisTemplate(int index) {
        return (StringRedisTemplate) getTemplate(customStringTemplate, index);
    }


    /**
     * 获取redis实例
     *
     * @param index         -> redis数据库下标
     * @param redisTemplate
     * @return
     */
    public RedisTemplate getTemplate(RedisTemplate redisTemplate, int index) {
        JedisConnectionFactory jedisConnectionFactory = (JedisConnectionFactory) redisTemplate.getConnectionFactory();
        jedisConnectionFactory.getStandaloneConfiguration().setDatabase(index);
        redisTemplate.setConnectionFactory(jedisConnectionFactory);
        return redisTemplate;
    }

    /**
     * 向redis不同数据库存值
     *
     * @param index redis数据库下标
     * @param key   键
     * @param value 值
     */
    public void set(int index, String key, String value) {
        getStringRedisTemplate(index).opsForValue().set(key, value);
    }

    /**
     * 向redis不同数据库存值
     *
     * @param index      -> redis数据库下标
     * @param key        键
     * @param value      值
     * @param expireTime 过期时间 -> 单位: 秒
     */
    public void set(int index, String key, String value, long expireTime) {
        getStringRedisTemplate(index).opsForValue().set(key, value, expireTime, TimeUnit.SECONDS);
    }

    /**
     * 向redis不同数据库存值
     *
     * @param index redis数据库下标
     * @param key   键
     * @param value 值
     */
    public void setObject(int index, String key, Object value) {
        this.set(index,key,JSON.toJSONString(value));
    }

    /**
     * 向redis不同数据库存值
     *
     * @param index      redis数据库下标
     * @param key        键
     * @param value      值
     * @param expireTime 过期时间 -> 单位: 秒
     */
    public void setObject(int index, String key, Object value, long expireTime) {
        this.set(index,key,JSON.toJSONString(value),expireTime);
    }


    /**
     * 取redis不同数据库的值 并转换成String(字符串)
     *
     * @param index redis数据库下标
     * @param key   键
     * @return string
     */
    public String get(int index, String key) {
        return getStringRedisTemplate(index).opsForValue().get(key);
    }

    /**
     * 取redis不同数据库的值
     *
     * @param index redis数据库下标
     * @param key   键
     * @return object
     */
    public Object getObject(int index, String key, Class clazz) {
        String value = this.get(index, key);
        return JSON.parseObject(value,clazz);
    }

    /**
     * 删除缓存信息
     *
     * @param index
     * @param key
     */
    public void remove(int index, String key) {
        getStringRedisTemplate(index).delete(key);
    }
}