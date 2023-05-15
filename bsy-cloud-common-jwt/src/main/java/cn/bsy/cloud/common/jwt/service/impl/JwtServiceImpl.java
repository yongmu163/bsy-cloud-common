package cn.bsy.cloud.common.jwt.service.impl;

import cn.bsy.cloud.common.core.constant.CommonConstant;
import cn.bsy.cloud.common.core.exception.CustomizeException;
import cn.bsy.cloud.common.jwt.dto.JwtTokenDTO;
import cn.bsy.cloud.common.jwt.dto.LoginInfoDTO;
import cn.bsy.cloud.common.jwt.service.IGenerateJwtKeyService;
import cn.bsy.cloud.common.jwt.service.IJwtObjectInitService;
import cn.bsy.cloud.common.jwt.service.IJwtService;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * @Description: 令牌服务实现类
 * @Author gaoh
 * @Date 2022/6/26 0026
 **/
@Slf4j
public class JwtServiceImpl implements IJwtService {
    @Override
    public JwtTokenDTO generateToken(LoginInfoDTO loginInfo,
                                     IGenerateJwtKeyService generateJwtKeyService,
                                     IJwtObjectInitService jwtObjectInitService) {
        // 判断用户相关信息是否为null
        if (ObjectUtil.isNull(loginInfo)) {
            log.error("生成token的认证对象LoginInfoDTO信息为空");
            throw new CustomizeException("登陆信息不存在，无法生成Token");
        }
        // 信息完整性校验
        loginInfo.validate();
        // 获取JWT builder
        JwtTokenDTO jwtToken = jwtObjectInitService.getJwtToken();
        // 计算超时时间
        Date expireDate= DateUtil.offset(new Date(), DateField.DAY_OF_YEAR,jwtToken.getExpireDay());
        String token = Jwts.builder()
                // jwt payload --> KV
                .claim(CommonConstant.JWT_LOGIN_INFO_KEY, JSON.toJSONString(loginInfo))
                // jwt id
                .setId(RandomUtil.randomString(15))
                // jwt 过期时间
                .setExpiration(expireDate)
                // jwt 签名 --> 加密
                .signWith(generateJwtKeyService.getPrivateKey(), SignatureAlgorithm.RS256)
                .compact();
        jwtToken.setToken(token);
        return jwtToken;
    }

    @Override
    public LoginInfoDTO getLoginInfoToken(String tokenStr, IGenerateJwtKeyService generateJwtKeyService) {
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(generateJwtKeyService.getPublicKey())
                .parseClaimsJws(tokenStr);
        Claims body = claimsJws.getBody();
        String loginInfoStr = body.get(CommonConstant.JWT_LOGIN_INFO_KEY).toString();
        LoginInfoDTO loginInfo = JSON.parseObject(body.get(CommonConstant.JWT_LOGIN_INFO_KEY).toString(), LoginInfoDTO.class);
        return loginInfo;
    }

    @Override
    public void verifierToken(String tokenStr, IGenerateJwtKeyService generateJwtKeyService) {
        this.getLoginInfoToken(tokenStr, generateJwtKeyService);
    }
}
