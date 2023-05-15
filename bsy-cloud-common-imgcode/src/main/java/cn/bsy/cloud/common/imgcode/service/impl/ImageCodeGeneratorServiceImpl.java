package cn.bsy.cloud.common.imgcode.service.impl;


import cn.bsy.cloud.common.imgcode.model.ImageCode;
import cn.bsy.cloud.common.imgcode.service.ImageCodeGeneratorService;
import com.google.code.kaptcha.Producer;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.context.request.ServletWebRequest;

import java.awt.image.BufferedImage;

/**
 * @author gaoh
 * @desc 图片验证码生成接口实现类
 * @date 2022年01月26日 下午 3:54
 */
@Data
@AllArgsConstructor
public class ImageCodeGeneratorServiceImpl implements ImageCodeGeneratorService {
    private Producer producer;
    @Override
    public ImageCode generate(ServletWebRequest servlet) {
        servlet.getResponse().setHeader("Cache-Control", "no-store, no-cache");
        servlet.getResponse().setContentType("image/jpeg");
        /** 生成文字验证码 */
        String text = producer.createText();
        /** 生成图片验证码 */
        BufferedImage image = producer.createImage(text);
        return new ImageCode(image, text);
    }
}