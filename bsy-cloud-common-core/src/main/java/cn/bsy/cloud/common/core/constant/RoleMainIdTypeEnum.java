package cn.bsy.cloud.common.core.constant;

/**
 * @author gaoh
 * @desc 角色主体标识类型枚举
 * @date 2022年01月24日 下午 9:57
 */
public enum RoleMainIdTypeEnum {
    /**
     * 角色关系表客户主体标识类型
     */
    ROLE_MAIN_ID_CUSTOMER_TYPE("0","客户标识"),
    /**
     * 角色关系表用户主体标识类型
     */
    ROLE_MAIN_ID_USER_TYPE("1","用户标识"),
    /**
     * 角色关系表部门主体标识类型
     */
    ROLE_MAIN_ID_DEP_TYPE("2","部门标识"),
    /**
     * 角色关系表岗位主体标识类型
     */
    ROLE_MAIN_ID_POSITION_TYPE("3","岗位标识");
    private String code;

    private String desc;

    RoleMainIdTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }}