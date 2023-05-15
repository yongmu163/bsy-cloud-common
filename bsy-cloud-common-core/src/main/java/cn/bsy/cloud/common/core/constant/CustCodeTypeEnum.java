package cn.bsy.cloud.common.core.constant;

/**
 * @author gaoh
 * @desc 客户代码类型
 * @date 2022年01月24日 下午 5:43
 */
public enum CustCodeTypeEnum {
    /**
     * 身份证号
     */
    LEGAL_PERSON_ID_CARD_TYPE("01","身份证号"),
    /**
     * 统一社会信用代码
     */
    LEGAL_PERSON_CREDIT_TYPE("02","统一信用社会代码"),
    /**
     * 手机号
     */
    LEGAL_PERSON_MOBILE_TYPE("03","手机号"),
    /**
     * 护照
     */
    LEGAL_PERSON_PASSPORT_TYPE("04","护照"),
    /**
     * 无
     */
    LEGAL_PERSON_NULL_TYPE("05","无");

    private final String code;

    private final String desc;


    CustCodeTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }}