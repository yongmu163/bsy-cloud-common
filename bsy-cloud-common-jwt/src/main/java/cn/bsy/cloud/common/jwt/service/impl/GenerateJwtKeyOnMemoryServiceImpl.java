package cn.bsy.cloud.common.jwt.service.impl;

import cn.bsy.cloud.common.core.constant.CommonConstant;
import cn.bsy.cloud.common.core.utils.RsaUtils;
import cn.bsy.cloud.common.jwt.service.IGenerateJwtKeyService;
import cn.hutool.core.collection.CollUtil;

import javax.annotation.PostConstruct;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Map;

/**
 * @Description: 生成用于构建jwt的公钥私钥对，并存入缓存redis
 * 1、该bean由于将密钥对的字符串存储在了内存中，因此仅仅适用于在单体项目场景下调用
 * 2、基于安全考虑，单体项目使用时，每次容器重启，密钥对字符串会重新刷新
 * @Author gaoh
 * @Date 2022/6/26 0026
 **/
public class GenerateJwtKeyOnMemoryServiceImpl implements IGenerateJwtKeyService {
    public static Map<String, String> RsaKeyMap = CollUtil.newHashMap();


    /**
     * 初始化时将密钥对存储在内存中
     */
    @PostConstruct
    public void init() {
        RsaKeyMap = RsaUtils.generateKey();
    }


    @Override
    public PrivateKey getPrivateKey() {
        return RsaUtils.generatePrivateKey(RsaKeyMap.get(CommonConstant.PRIVATE_KEY));
    }

    @Override
    public PublicKey getPublicKey() {
        return RsaUtils.generatePublicKey(RsaKeyMap.get(CommonConstant.PUBLIC_KEY));
    }
}
