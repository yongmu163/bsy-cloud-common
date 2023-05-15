package cn.bsy.cloud.common.core.utils;

import cn.hutool.core.collection.CollUtil;
import lombok.experimental.UtilityClass;

import java.util.Map;

/**
 * @author gaoh
 * @desc 全局上下文线程变量工具类
 * @date 2022年01月13日 下午 2:36
 */
@UtilityClass
public final class ThreadLocalUtils {

    private final ThreadLocal<Map<Object, Object>> myContext = new ThreadLocal<Map<Object, Object>>() {
        @Override
        protected Map<Object, Object> initialValue() {
            return CollUtil.newHashMap(100);
        }
    };

    /**
     * 根据key获取值
     */
    public Object getValue(Object key) {
        if (myContext.get() == null) {
            return null;
        }
        return myContext.get().get(key);
    }

    /**
     * 存储
     */
    public Object setValue(Object key, Object value) {
        Map<Object, Object> cacheMap = myContext.get();
        if (cacheMap == null) {
            cacheMap = CollUtil.newHashMap(100);
            myContext.set(cacheMap);
        }
        return cacheMap.put(key, value);
    }

    /**
     * 根据key移除值
     */
    public void removeValue(Object key) {
        myContext.remove();
    }

    /**
     * 重置
     */
    public void reset() {
        if (myContext.get() != null) {
            myContext.get().clear();
        }
    }

}