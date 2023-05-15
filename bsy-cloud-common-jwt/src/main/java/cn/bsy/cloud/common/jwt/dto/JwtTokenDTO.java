package cn.bsy.cloud.common.jwt.dto;



import cn.bsy.cloud.common.core.constant.TokenExpireDayType;
import cn.bsy.cloud.common.jwt.constant.TokenTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author gaoh
 * @desc token对象
 * @date 2022年01月12日 下午 11:14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtTokenDTO implements Serializable {

    /**
     * token
     */
    private String token;
    /**
     * token对象类型-> 0 用户 , 1 客户
     * 默认为用户ID
     */
    private String tokenType;

    /**
     * token字符串嵌入的失效天数
     * 默认为1天
     */
    private Integer expireDay;

    /**
     * 默认生成的token为用户类型token
     *
     * @param token
     */
    public JwtTokenDTO(String token) {
        this.token = token;
        this.tokenType = null;
        this.expireDay = null;
    }

    /**
     * 构建JWT方法
     * @param tokenTypeEnum
     * @param tokenExpireDayType
     */
    public JwtTokenDTO(TokenTypeEnum tokenTypeEnum, TokenExpireDayType tokenExpireDayType) {
        this.token = null;
        this.tokenType = tokenTypeEnum.getType();
        this.expireDay = tokenExpireDayType.getTokenSelfExpireDay();
    }
}