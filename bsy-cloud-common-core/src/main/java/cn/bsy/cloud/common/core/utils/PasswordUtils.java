package cn.bsy.cloud.common.core.utils;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import lombok.experimental.UtilityClass;
import org.springframework.util.DigestUtils;

/**
 * @author gaoh
 * @desc md5加密
 * @date 2022年01月19日 下午 7:13
 */
@UtilityClass
public class PasswordUtils {
    /**
     * 获得一个随机的字符串（只包含数字和字符）
     *
     * @param length
     * @return
     */
    public String getRandomString(int length) {
        return RandomUtil.randomString(length);
    }

    /**
     * 获得一个只包含数字的字符串
     *
     * @param length
     * @return
     */
    public String getRandomInt(int length) {
        return RandomUtil.randomNumbers(5);
    }

    /**
     * 获取MD5加密后的信息(加盐)
     *
     * @param str
     * @return
     */
    public String getMd5(String str, String salt) {
        if (StrUtil.isNotBlank(salt)) {
            str = str + "/" + salt;
        }
        return PasswordUtils.getMd5(str);
    }

    /**
     * 获取MD5加密后的信息(不加盐)
     * @param str
     * @return
     */
    public String getMd5(String str) {
        String md5 = DigestUtils.md5DigestAsHex(str.getBytes());
        return md5;
    }
}