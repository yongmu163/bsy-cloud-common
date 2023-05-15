package cn.bsy.cloud.common.core.constant;

import cn.hutool.core.util.StrUtil;

/**
 * @author gaoh
 * @desc 用户名类型枚举
 * @date 2022年06月25日 下午 2:47
 */

public enum UserNameTypeEnum {
    /**
     * 用户名类型
     */
    USER_USER_NAME_TYPE("01", "用户名"),

    /**
     * 手机号类型
     */
    USER_MOBILE_PHONE_TYPE("02", "手机号"),
    /**
     * 邮箱类型
     */
    USER_MAIL_TYPE("03", "邮箱"),
    /**
     * 微信类型
     */
    USER_WECHAT_TYPE("04", "微信"),
    /**
     * 身份证号类型
     */
    USER_ID_CARD_TYPE("05", "身份证号");

    private String code;

    private String desc;

    UserNameTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    /**
     * 根据类型编码获取枚举对象
     * @param code
     * @return
     */
    public static UserNameTypeEnum getUserNameTypeEnum(String code) {
        for (UserNameTypeEnum userNameTypeEnum : values()) {
            if (StrUtil.equals(code, userNameTypeEnum.code)) {
                return userNameTypeEnum;
            }
        }
        return null;
    }
}