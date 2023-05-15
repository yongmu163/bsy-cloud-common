package cn.bsy.cloud.common.imgcode.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.image.BufferedImage;

/**
 * @author gaoh
 * @desc 图片验证码对象
 * @date 2022年01月26日 下午 3:27
 */
@Data
@NoArgsConstructor
public class ImageCode {
    /**
     * 缓冲区图像实现
     */
    private BufferedImage image;
    /**
     * 验证码值
     */
    private String code;

    public ImageCode(BufferedImage image, String code) {
        this.image = image;
        this.code = code;
    }
}