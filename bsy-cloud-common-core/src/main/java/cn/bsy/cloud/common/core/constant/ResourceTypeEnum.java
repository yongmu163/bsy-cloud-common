package cn.bsy.cloud.common.core.constant;

/**
 * @author gaoh
 * @desc 资源类型枚举
 * @date 2022年01月21日 下午 11:03
 */
public enum ResourceTypeEnum {

    /**
     * 菜单资源类型
     */
    RESOURCE_TYPE_MENU("01","菜单"),
    /**
     * 按钮资源类型
     */
    RESOURCE_TYPE_BUTTON("02","按钮"),
    /**
     * 其他资源类型
     */
    RESOURCE_TYPE_OTHER("9","其他");

    private String code;

    private String desc;

    ResourceTypeEnum(String code, String desc) {
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
