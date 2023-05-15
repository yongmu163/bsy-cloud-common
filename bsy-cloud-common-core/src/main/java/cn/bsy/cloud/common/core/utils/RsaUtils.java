package cn.bsy.cloud.common.core.utils;

import cn.bsy.cloud.common.core.constant.CommonConstant;
import cn.bsy.cloud.common.core.exception.CustomizeException;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollUtil;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Map;

/**
 * @Description: TODO
 * @Author gaoh
 * @Date 2022/6/25 0025
 **/
@Slf4j
@UtilityClass
public class RsaUtils {
    /**
     * 获取公钥私钥对
     *
     * @return
     * @throws NoSuchAlgorithmException
     */
    public Map<String, String> generateKey() {
        try {
            Map<String, String> keyMap = CollUtil.newHashMap();
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            // 生成公钥和私钥对
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            // 获取公钥对象
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            // 获取私钥对象
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            keyMap.put(CommonConstant.PUBLIC_KEY, Base64.encode(publicKey.getEncoded()));
            keyMap.put(CommonConstant.PRIVATE_KEY, Base64.encode(privateKey.getEncoded()));
            return keyMap;
        } catch (NoSuchAlgorithmException e) {
            throw new CustomizeException("生成密钥对失败!");
        }
    }

    /**
     * 生成私钥的对象
     *
     * @param privateKeyStr
     * @return
     */
    public PrivateKey generatePrivateKey(String privateKeyStr) {
        try {
            byte[] result = java.util.Base64.getDecoder().decode(privateKeyStr);
            PKCS8EncodedKeySpec priPksc8 = new PKCS8EncodedKeySpec(result);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(priPksc8);
        } catch (Exception e) {
            throw new CustomizeException("无法获取私钥对象", e);
        }
    }

    /**
     * 生成公钥对象
     *
     * @param publicKeyStr
     * @return
     */
    public PublicKey generatePublicKey(String publicKeyStr) {
        byte[] result = java.util.Base64.getDecoder().decode(publicKeyStr);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(result);
        try {
            return KeyFactory.getInstance("RSA").generatePublic(keySpec);
        } catch (Exception e) {
            throw new CustomizeException("无法获取公钥对象", e);
        }
    }
}
