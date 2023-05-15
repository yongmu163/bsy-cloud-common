package cn.bsy.cloud.common.core.constant;

/**
 * @author gaoh
 * @desc 客户状态枚举
 * @date 2022年01月28日 下午 9:06
 */
public enum CustStatusTypeEnum {
    /**
     * 禁用状态
     */
    STATUS_TYPE_DISABLE("0","禁用"),
    /**
     * 启用状态
     */
    STATUS_TYPE_ENABLE("1","启用");

    private String code;

    private String desc;

    CustStatusTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

}