package cn.bsy.cloud.common.jwt.dto;

import cn.bsy.cloud.common.core.exception.CustomizeException;
import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author gaoh
 * @desc Token包含的认证信息
 * @date 2022年01月12日 下午 11:01
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginInfoDTO {

    /**
     * 用户ID
     */
    private String userId;
    /**
     * 所属客户ID
     */
    private String custId;
    /**
     * 所属租户ID
     */
    private String tenantId;
    /**
     * 角色编码
     */
    private List<String> roleCodes;
    /**
     * 用户名
     */
    private String userName;

    public LoginInfoDTO(String userId, String custId, String tenantId) {
        this.userId = userId;
        this.custId = custId;
        this.tenantId = tenantId;
    }

    /**
     * 信息完整性校验
     */
    public void validate() {
        if (StrUtil.isBlank(this.getUserId())) {
            throw new CustomizeException("登陆信息缺失用户ID，无法生成Token");
        }
        if (StrUtil.isBlank(this.getCustId())) {
            throw new CustomizeException("登陆信息缺失客户ID，无法生成Token");
        }
        if (StrUtil.isBlank(this.getCustId())) {
            throw new CustomizeException("登陆信息缺失系统标识，无法生成Token");
        }
    }
}
