package cn.bsy.cloud.common.core.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * @author gaoh
 * @desc json工具类
 * @date 2022年05月30日 下午 1:36
 */
@Slf4j
@UtilityClass
public class JsonUtils {

    /**
     * 判断字符串是否为JSON格式
     * @param str
     * @return
     */
    public static boolean isJsonStr(String str) {
        try {
            JSONObject jsonStr= JSONObject.parseObject(str);
            return  true;
        } catch (Exception e) {
            return false;
        }
    }
}