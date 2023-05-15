package cn.bsy.cloud.common.file.config;

import io.minio.MinioClient;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
/**
 * @Description: minIO配置类
 * @Author gaoh
 * @Date 2022年07月20日 下午 4:46
 **/
@Configuration
@AllArgsConstructor
@ConditionalOnProperty(prefix = "minio", name = {"endpoint", "accessKey", "secretKey", "port"}, matchIfMissing = false)
public class MinIoClientConfig {
    private final MinIoClientProperties minIoClientProperties;

    @Bean("minioClient")
    public MinioClient getMinioClient() {
        MinioClient client = MinioClient.builder()
                .endpoint(minIoClientProperties.getEndpoint(),
                        minIoClientProperties.getPort(),
                        minIoClientProperties.getMinioHttps())
                .credentials(minIoClientProperties.getAccessKey(),
                        minIoClientProperties.getSecretKey())
                .build();
        try {
            client.ignoreCertCheck();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return client;
    }
}
