package cn.bsy.cloud.common.shiro.service;

import java.util.LinkedHashMap;

/**
 * @author gaoh
 * @desc 获取不需要Shiro拦截的路径->无实现类
 * @date 2022年06月14日 下午 3:30
 */
public interface InterceptAnonPathService {
    /**
     * 获取需要shiro框架拦截及不拦截web方法路径
     *
     * @return
     */
    LinkedHashMap<String, String> getAnonfilterMap();
}