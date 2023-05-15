package cn.bsy.cloud.common.core.constant;

/**
 * @Description: 单位级别
 * @Author gaoh
 * @Date 2022年11月01日 下午 3:30
 **/
public enum CustLevelEnum {
    /**
     * 一级单位，最高级别
     */
    CUST_LEVEL_ONE(1,"一级单位"),
    /**
     * 二级单位
     */
    CUST_LEVEL_TWO(2,"二级单位"),
    /**
     * 三级单位
     */
    CUST_LEVEL_THREE(3,"三级单位");
    /**
     * 单位级别
     */
    private  Integer code;
    /**
     * 单位级别描述
     */
    private  String desc;


    CustLevelEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
