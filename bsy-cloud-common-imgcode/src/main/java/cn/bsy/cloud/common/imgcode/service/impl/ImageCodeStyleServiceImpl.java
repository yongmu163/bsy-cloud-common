package cn.bsy.cloud.common.imgcode.service.impl;


import cn.bsy.cloud.common.imgcode.service.ImageCodeStyleService;
import cn.hutool.core.util.StrUtil;
import com.google.code.kaptcha.util.Config;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Properties;

/**
 * @author gaoh
 * @desc 图片样式配置接口
 * @date 2022年01月27日 上午 11:07
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageCodeStyleServiceImpl implements ImageCodeStyleService {
    private static final String BORDER = "kaptcha.border";
    private static final String FONT_COLOR = "kaptcha.textproducer.font.color";
    private static final String CHAR_SPACE = "kaptcha.textproducer.char.space";
    private static final String IMAGE_WIDTH = "kaptcha.image.width";
    private static final String IMAGE_HEIGHT = "kaptcha.image.height";
    private static final String CHAR_LENGTH = "kaptcha.textproducer.char.length";
    private static final Object IMAGE_FONT_SIZE = "kaptcha.textproducer.font.size";
    /**
     * 验证码个数
     */
    String length;
    /**
     * 验证码图片宽度
     */
    String width;
    /**
     * 验证码图片高度
     */
    String height;
    /**
     * 验证码颜色
     */
    String fontColor;
    /**
     * 验证码图片是否有边框
     */
    boolean isBorder;
    /**
     * 验证码图片图片间隔
     */
    String space;
    /**
     * 验证码图片文字大小
     */
    String textSize;
    /**
     * 是否添加干扰线
     */
    boolean isNoise = false;
    @Override
    public Config getConfig() {
        Properties properties = new Properties();
        /** 验证码图片是否存在边框 */
        properties.put(BORDER, this.isBorder?"yes":"no");
        /** 验证码图片颜色 */
        properties.put(FONT_COLOR, StrUtil.isBlank(this.getFontColor())?"black":this.getFontColor());
        /** 验证码图片间隔 */
        properties.put(CHAR_SPACE, StrUtil.isBlank(this.getSpace())?"5":this.getSpace());
        /** 验证码图片宽度 */
        properties.put(IMAGE_WIDTH, StrUtil.isBlank(this.getWidth())?"110":this.getWidth());
        /** 验证码图片高度*/
        properties.put(IMAGE_HEIGHT, StrUtil.isBlank(this.getHeight())?"38":this.getHeight());
        /** 验证码图片文字大小 */
        properties.put(IMAGE_FONT_SIZE, StrUtil.isBlank(this.getTextSize())?"30":this.getTextSize());
        /** 验证码图片字符个数 */
        properties.put(CHAR_LENGTH, StrUtil.isBlank(this.getLength())?"4":this.getLength());
        /** 去除干扰线 */
        if(!this.isNoise) {
            properties.put("kaptcha.noise.impl", "com.google.code.kaptcha.impl.NoNoise");
        }
        return  new Config(properties);
    }
}
