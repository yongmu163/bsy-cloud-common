package cn.bsy.cloud.common.jwt.service;

import cn.bsy.cloud.common.jwt.dto.JwtTokenDTO;

/**
 * @Description: 组装jwt对象接口
 * @Author gaoh
 * @Date 2022/6/26 0026
 **/
public interface IJwtObjectInitService{
    /**
     * 返回JWTToken对象
     * @return
     */
    JwtTokenDTO getJwtToken();
}
