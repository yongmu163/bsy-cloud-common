package cn.bsy.cloud.common.file.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Description: 文件系统配置类
 * @Author gaoh
 * @Date 2022年07月20日 下午 4:35
 **/
@Data
@Slf4j
@Component
@ConfigurationProperties(prefix = "minio")
public class MinIoClientProperties {
    /**
     * 分布式文件系统地址
     */
    private String endpoint;
    /**
     * 用户名
     */
    private String accessKey;
    /**
     * 密码
     */
    private String secretKey;
    /**
     * 端口
     */
    private int port;
    /**
     * 是否启用https
     */
    private Boolean minioHttps;
    /**
     * 加入预览转码队列前缀（kkFileViewUrl前缀）
     */
    private String addTaskPrefix;

    /**
     * 在线预览前缀（kkFileViewUrl前缀）
     */
    private String onLinePrefix;
    /**
     * 分享URL前缀地址（nginx反向代理地址）
     */
    private String shareUrlPrefix;
}
