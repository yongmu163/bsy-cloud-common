package cn.bsy.cloud.common.core.constant;

/**
 * @Description: 组织机构标识
 * @Author sunxf
 * @Date 2022-10-18 10:43
 **/

public enum IdentificationEnum {
    /**
     * 单位标识
     */
    CUST_IDENTIFICATION("cust", "单位标识"),
    /**
     * 岗位标识
     */
    JOB_IDENTIFICATION("job", "岗位标识"),
    /**
     * 部门标识
     */
    DEPARTMENT_IDENTIFICATION("department", "部门标识"),
    /**
     * 职位标识
     */
    POSITION_IDENTIFICATION("position", "职位标识");
    private final String code;

    private final String desc;

    IdentificationEnum(String code, String desc) {
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
