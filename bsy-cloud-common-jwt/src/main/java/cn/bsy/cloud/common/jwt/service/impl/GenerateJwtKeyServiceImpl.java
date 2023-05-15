package cn.bsy.cloud.common.jwt.service.impl;

import cn.bsy.cloud.common.core.utils.RsaUtils;
import cn.bsy.cloud.common.jwt.constant.JwtConstant;
import cn.bsy.cloud.common.jwt.service.IGenerateJwtKeyService;
import lombok.AllArgsConstructor;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @Description: 生成用于构建jwt的公钥私钥对
 * @Author gaoh
 * @Date 2022年08月09日 下午 4:22
 **/
@AllArgsConstructor
public class GenerateJwtKeyServiceImpl implements IGenerateJwtKeyService {
    @Override
    public PrivateKey getPrivateKey() {
        return RsaUtils.generatePrivateKey(JwtConstant.PRIVATE_KEY_STR);
    }

    @Override
    public PublicKey getPublicKey() {
        return RsaUtils.generatePublicKey(JwtConstant.PUBLIC_KEY_STR);
    }
}
