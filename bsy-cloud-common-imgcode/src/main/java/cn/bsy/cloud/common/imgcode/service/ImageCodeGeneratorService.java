package cn.bsy.cloud.common.imgcode.service;
import cn.bsy.cloud.common.imgcode.model.ImageCode;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * @author gaoh
 * @desc 图片验证码生成接口
 * @date 2022年01月26日 下午 3:35
 */
public interface ImageCodeGeneratorService {
    /**
     * 生成验证码
     * @param servlet
     * @return
     */
    ImageCode generate(ServletWebRequest servlet);
}