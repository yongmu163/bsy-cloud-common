package cn.bsy.cloud.common.imgcode.constant;

/**
 * @author gaoh
 * @desc 图片验证码枚举
 * @date 2022年01月26日 下午 4:12
 */
public enum ImageCodeEnum {
    ;
    /** 验证码个数*/
    String length;
    /** 验证码图片宽度 */
    String width;
    /** 验证码图片高度 */
    String height;
    /** 验证码图片边框颜色 */
    String borderColor;
    /** 验证码图片是否有边框 */
    String border;
    /** 验证码图片图片间隔 */
    String space;
    /** 验证码图片文字大小 */
    String textSize;
    /** 验证码失效时间 （秒） */
    Integer expireSecond;

    ImageCodeEnum(String length, String width, String height, String borderColor, String border, String space, String textSize, Integer expireSecond) {
        this.length = length;
        this.width = width;
        this.height = height;
        this.borderColor = borderColor;
        this.border = border;
        this.space = space;
        this.textSize = textSize;
        this.expireSecond = expireSecond;
    }
}