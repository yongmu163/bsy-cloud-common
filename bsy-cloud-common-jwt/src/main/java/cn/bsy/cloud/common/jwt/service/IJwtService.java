package cn.bsy.cloud.common.jwt.service;

import cn.bsy.cloud.common.jwt.dto.JwtTokenDTO;
import cn.bsy.cloud.common.jwt.dto.LoginInfoDTO;

/**
 * @Description: jwt服务接口
 * @Author gaoh
 * @Date 2022/6/26 0026
 **/
public interface IJwtService {
    /**
     * 生成JWT对象
     * @param loginInfo
     * @param generateJwtKeyService
     * @param jwtObjectInitService
     * @return
     */
    JwtTokenDTO generateToken(LoginInfoDTO loginInfo,
                              IGenerateJwtKeyService generateJwtKeyService,
                              IJwtObjectInitService jwtObjectInitService);

    /**
     * 获取令牌包含信息
     *
     * @param tokenStr
     * @param generateJwtKeyService
     * @return
     */
    LoginInfoDTO getLoginInfoToken(String tokenStr, IGenerateJwtKeyService generateJwtKeyService);

    /**
     * 令牌鉴签
     *
     * @param tokenStr
     * @param generateJwtKeyService
     */
    void verifierToken(String tokenStr, IGenerateJwtKeyService generateJwtKeyService);
}
