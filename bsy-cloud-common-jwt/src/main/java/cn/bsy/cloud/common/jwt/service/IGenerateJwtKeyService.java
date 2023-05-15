package cn.bsy.cloud.common.jwt.service;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @author gaoh
 * @desc 获取密钥对字符串接口
 * @date 2022年01月13日 上午 12:55
 */
public interface  IGenerateJwtKeyService {

    /**
     * 获取私钥对象
     *
     * @return
     */
    PrivateKey getPrivateKey();

    /**
     * 获取公钥对象
     *
     * @return
     */
    PublicKey getPublicKey();
}