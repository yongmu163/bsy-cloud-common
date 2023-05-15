package cn.bsy.cloud.common.core.constant;

/**
 * @author gaoh
 * @desc 客户主体标识类型
 * @date 2022年01月20日 下午 3:52
 */
public enum CustMainIdTypeEnum {
    /**
     * 身份证号码
     */
    CUST_ID_CARD_TYPE("1","身份证号类型"),

    /**
     * 企业统一社会信用代码
     */
    CUST_CREDIT_CODE_TYPE("2","企业统一社会信用代码");

    private String code;

    private String desc;

    CustMainIdTypeEnum(String code, String desc) {
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