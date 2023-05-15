package cn.bsy.cloud.common.core.properties;

import lombok.Data;

/**
 * @author gaoh
 * @desc 租户信息本地配置类
 * @date 2022年01月21日 下午 8:14
 */
@Data
public class TenantProperties {

    /**
     * 系统平台代码
     */
    private String code;
    /**
     * 系统平台名称
     */
    private String name;
}