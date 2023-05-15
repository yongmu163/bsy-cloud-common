package cn.bsy.cloud.common.imgcode.service;

import com.google.code.kaptcha.util.Config;

/**
 * @author gaoh
 * @desc 图片验证码样式配置接口
 * @date 2022年01月27日 下午 12:12
 */
public interface ImageCodeStyleService {
    /**
     * 获取图片验证码配置信息
     * @return
     */
    Config getConfig();
}