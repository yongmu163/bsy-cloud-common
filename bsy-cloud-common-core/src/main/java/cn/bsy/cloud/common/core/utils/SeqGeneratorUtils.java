package cn.bsy.cloud.common.core.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Random;
import java.util.UUID;

/**
 * @Description: 编号生成器
 * @Author gaoh
 * @Date 2022年06月28日 下午 3:11
 **/
@Slf4j
@UtilityClass
public class SeqGeneratorUtils {
    /**
     * 得到32位唯一的UUID
     *
     * @return
     */
    public Serializable uuid() {
        UUID uid = UUID.randomUUID();
        return uid.toString().replace("-", "");
    }

    /**
     * 根据长度得到唯一编号
     *
     * @param length
     * @return
     */
    public Serializable uuid(int length) {
        UUID uid = UUID.randomUUID();
        String temp = uid.toString().replace("-", "");
        if (length > 0 && length < temp.length()) {
            temp = temp.substring(temp.length() - length);
        }
        return temp;
    }

    /**
     * 根据长度得到随机字符串，一位字母一位数字
     *
     * @param length
     * @return
     */
    public String unique(int length) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < length; i++) {
            if (i % 2 == 0) {
                str.append(getRandom(97, 122));
            } else {
                str.append(getRandom(48, 57));
            }
        }
        return str.toString();
    }

    /**
     * 得到纯数字编号
     *
     * @param length
     * @return
     */
    public String numberStr(int length) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < length; i++) {
            if (i == 0) {
                str.append(getRandom(49, 57));
            } else {
                str.append(getRandom(48, 57));
            }
        }
        return str.toString();
    }

    /**
     * 根据前缀返回纯数字的字符串
     *
     * @param length
     * @param prefixStr
     * @return
     */
    public String numberStr(int length, String prefixStr) {
        return prefixStr + numberStr(length);
    }

    /**
     * 根据开始和结束大小得到单一字符
     *
     * @param begin 开始值
     * @param end   结束值
     * @return 单一字符
     */
    private String getRandom(int begin, int end) {
        String str = "";
        Random rd = new Random();
        int number = 0;
        while (str.length() == 0) {
            number = rd.nextInt(end + 1);
            if (number >= begin && number <= end) {
                str = String.valueOf((char) number);
            }
        }
        return str;
    }


    public static void main(String[] args) {
        log.info("number(int length)==={}", numberStr(4, "0001"));
    }
}
