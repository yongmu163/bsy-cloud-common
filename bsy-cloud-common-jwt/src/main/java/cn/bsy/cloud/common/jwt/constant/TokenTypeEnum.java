package cn.bsy.cloud.common.jwt.constant;

/**
 * @author gaoh
 * @desc token类型枚举
 * @date 2022年01月13日 下午 3:53
 */
public enum TokenTypeEnum {
    /**
     * 用户类型
     */
    USER_TYPE("0", "用户类JWT"),
    /**
     * 客户类型
     */
    CUSTOMER_TYPE("1", "客户类JWT");

    String type;
    String desc;

    TokenTypeEnum(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    /**
     * 获取token类型
     *
     * @return
     */
    public String getType() {
        return type;
    }

    /**
     * 获取token类型描述
     *
     * @return
     */
    public String getDesc() {
        return desc;
    }
}