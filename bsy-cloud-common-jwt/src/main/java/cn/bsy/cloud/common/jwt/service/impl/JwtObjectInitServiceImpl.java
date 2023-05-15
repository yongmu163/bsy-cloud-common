package cn.bsy.cloud.common.jwt.service.impl;

import cn.bsy.cloud.common.core.constant.TokenExpireDayType;
import cn.bsy.cloud.common.jwt.constant.TokenTypeEnum;
import cn.bsy.cloud.common.jwt.dto.JwtTokenDTO;
import cn.bsy.cloud.common.jwt.service.IJwtObjectInitService;
import lombok.AllArgsConstructor;

/**
 * @Description: 组装jwt对象接口实现
 * @Author gaoh
 * @Date 2022/6/26 0026
 **/
@AllArgsConstructor
public class JwtObjectInitServiceImpl implements IJwtObjectInitService {
    private final TokenTypeEnum tokenTypeEnum;
    private final TokenExpireDayType tokenExpireDayType;
    @Override
    public JwtTokenDTO getJwtToken() {
        return new JwtTokenDTO(this.tokenTypeEnum,this.tokenExpireDayType);
    }
}
