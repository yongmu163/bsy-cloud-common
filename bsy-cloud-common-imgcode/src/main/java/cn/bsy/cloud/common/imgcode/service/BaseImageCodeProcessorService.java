package cn.bsy.cloud.common.imgcode.service;


import cn.bsy.cloud.common.core.exception.CustomizeException;
import cn.bsy.cloud.common.imgcode.model.ImageCode;
import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import org.springframework.web.context.request.ServletWebRequest;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import java.io.IOException;

/**
 * @author gaoh
 * @desc 图片验证码加工抽象类
 * @date 2022年01月27日 下午 5:04
 */
@AllArgsConstructor
public abstract class BaseImageCodeProcessorService {

    /**
     * 创建图片验证码、保存以及返回
     *
     * @param servlet
     * @param randomCode
     */
    public void create(ServletWebRequest servlet, String randomCode, ImageCodeGeneratorService service) throws IOException {
        ImageCode imageCode = service.generate(servlet);
        this.saveImageCodeToCache(randomCode, imageCode.getCode(), this.getImageCodeExpireSecond());
        this.send(servlet, imageCode);
    }

    /**
     * 校验图片验证码
     *
     * @param randomCode
     * @param paramImageCode
     */
    public void validate(String randomCode, String paramImageCode) {
        if (StrUtil.isBlank(paramImageCode)) {
            throw new CustomizeException("图片验证码为空");
        }
        if (StrUtil.isBlank(randomCode)) {
            throw new CustomizeException("图片验证码校验错误");
        }
        if (!StrUtil.equals(this.getImageCodeFromCache(randomCode), paramImageCode)) {
            throw new CustomizeException("图片验证码校验错误");
        }
        // 校验成功后从缓存中删除
        this.removeImageCodeFromCache(randomCode);
    }

    /**
     * 返回图片验证码失效时间
     *
     * @return
     */
    protected abstract Long getImageCodeExpireSecond();

    /**
     * 保存图片验证码
     *
     * @param randomCode
     * @param imageCodeStr
     * @param imageCodeExpireSeconds
     */
    protected abstract void saveImageCodeToCache(String randomCode, String imageCodeStr, Long imageCodeExpireSeconds);

    /**
     * 从缓存中获取图片验证码
     * @param randomCode
     * @return
     */
    protected abstract String getImageCodeFromCache(String randomCode);

    /**
     * 将图片验证码从缓存中删除
     *
     * @param randomCode
     */
    protected abstract void removeImageCodeFromCache(String randomCode);

    /**
     * 发送图片验证码
     *
     * @param request
     * @param imageCode
     */
    private void send(ServletWebRequest request, ImageCode imageCode) {
        ServletOutputStream out;
        try {
            out = request.getResponse().getOutputStream();
            ImageIO.write(imageCode.getImage(), "JPEG", out);
            out.close();
        } catch (IOException e) {
            throw new CustomizeException("返回图片验证码报错");
        }
    }
}