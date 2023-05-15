package cn.bsy.cloud.common.core.constant;

import cn.hutool.core.util.StrUtil;

/**
 * @author gaoh
 * @desc 客户类型枚举
 * @date 2022年01月21日 上午 11:22
 */
public enum CustomerTypeEnum {
    /**
     * 集团客户
     */
    CUSTOMER_TYPE_BUSINESS("02","集团客户"),

    /**
     * 个人客户
     */
    CUSTOMER_TYPE_PERSON("01","个人客户");


    private String code;

    private String desc;

    CustomerTypeEnum(String code, String desc) {
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
     * 根据客户类型编码获取客户类型枚举
     * @param code
     * @return
     */
    public static CustomerTypeEnum getCustomerTypeEnum(String code) {
        for (CustomerTypeEnum customerTypeEnum : values()) {
            if (StrUtil.equals(code, customerTypeEnum.code)) {
                return customerTypeEnum;
            }
        }
        return null;
    }

}