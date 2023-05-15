package cn.bsy.cloud.common.core.constant;

import cn.hutool.core.util.ObjectUtil;

/**
 * @author gaoh
 * @desc 性别枚举
 * @date 2022年01月24日 下午 4:26
 */
public enum SexTypeEnum {
    /**
     * 性别未知
     */
    SEX_UNKNOWN_TYPE(2, "未知"),
    /**
     * 男
     */
    SEX_MALE_TYPE(0, "男"),
    /**
     * 女
     */
    SEX_FEMALE_TYPE(1, "女");

    private final Integer code;

    private final String desc;

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    SexTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 根据性别编码获取性别枚举
     *
     * @param code
     * @return
     */
    public static SexTypeEnum getSexTypeEnum(Integer code) {
        for (SexTypeEnum sexTypeEnum : values()) {
            if (ObjectUtil.isNull(code) && code.equals(sexTypeEnum.code)) {
                return sexTypeEnum;
            }
        }
        return null;
    }

}

