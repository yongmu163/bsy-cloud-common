package cn.bsy.cloud.common.core.constant;

/**
 * @author sunxf
 * @desc 禁用枚举
 * @date 2023年03月08日
 */
public enum DisableEnum {
    /**
     * 不禁用
     */
    NOT_DISABLE(0, "不禁用"),
    /**
     * 禁用
     */
    IS_DISABLE(1, "禁用");

    private final Integer code;

    private final String desc;

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    DisableEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}

