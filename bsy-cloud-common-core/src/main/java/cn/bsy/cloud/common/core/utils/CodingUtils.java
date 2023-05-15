package cn.bsy.cloud.common.core.utils;

import cn.bsy.cloud.common.core.constant.CommonConstant;
import cn.bsy.cloud.common.core.exception.CustomizeException;
import lombok.experimental.UtilityClass;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

/**
 * @Description: 编解码工具类
 * @Author gaoh
 * @Date 2022年10月08日 下午 12:08
 **/
@UtilityClass
public class CodingUtils {

     final Base64.Decoder decoder = Base64.getDecoder();
     final Base64.Encoder encoder = Base64.getEncoder();
    public static final String ALLOWED_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_.!~*'()";

    /**
     * base64 编码
     *
     * @param str
     * @return
     * @throws UnsupportedEncodingException
     */
    public String base64Encoder(String str) {
        try {
            byte[] textByte = str.getBytes(CommonConstant.CHARSET);
            return encoder.encodeToString(textByte);
        } catch (UnsupportedEncodingException e) {
            throw new CustomizeException("base64编码异常");
        }
    }

    /**
     * base64 解码
     *
     * @param str
     * @return
     */
    public String base64Decoder(String str) {
        try {
            return new String(decoder.decode(str), CommonConstant.CHARSET);
        } catch (UnsupportedEncodingException e) {
            throw new CustomizeException("base64解码异常");
        }
    }

    /**
     * URL链接编码
     *
     * @param input
     * @return
     */
    public String encodeUriComponent(String input) {
        if (input == null || "".equals(input)) {
            return input;
        }
        int l = input.length();
        StringBuilder o = new StringBuilder(l * 3);
        try {
            for (int i = 0; i < l; i++) {
                String e = input.substring(i, i + 1);
                if (ALLOWED_CHARS.indexOf(e) == -1) {
                    byte[] b = e.getBytes(CommonConstant.CHARSET);
                    o.append(getHex(b));
                    continue;
                }
                o.append(e);
            }
            return o.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return input;
    }

    /**
     * URL链接解码
     *
     * @param input
     * @return
     */
    public  String decodeUriComponent(String input) {
        char actualChar;
        StringBuffer buffer = new StringBuffer();
        int bytePattern, sumb = 0;
        for (int i = 0, more = -1; i < input.length(); i++) {
            actualChar = input.charAt(i);
            switch (actualChar) {
                case '%': {
                    actualChar = input.charAt(++i);
                    int hb = (Character.isDigit(actualChar) ? actualChar - '0'
                            : 10 + Character.toLowerCase(actualChar) - 'a') & 0xF;
                    actualChar = input.charAt(++i);
                    int lb = (Character.isDigit(actualChar) ? actualChar - '0'
                            : 10 + Character.toLowerCase(actualChar) - 'a') & 0xF;
                    bytePattern = (hb << 4) | lb;
                    break;
                }
                case '+': {
                    bytePattern = ' ';
                    break;
                }
                default: {
                    bytePattern = actualChar;
                }
            }
            if ((bytePattern & 0xc0) == 0x80) {
                sumb = (sumb << 6) | (bytePattern & 0x3f);
                if (--more == 0) {
                    buffer.append((char) sumb);
                }
            } else if ((bytePattern & 0x80) == 0x00) {
                buffer.append((char) bytePattern);
            } else if ((bytePattern & 0xe0) == 0xc0) {
                sumb = bytePattern & 0x1f;
                more = 1;
            } else if ((bytePattern & 0xf0) == 0xe0) {
                sumb = bytePattern & 0x0f;
                more = 2;
            } else if ((bytePattern & 0xf8) == 0xf0) {
                sumb = bytePattern & 0x07;
                more = 3;
            } else if ((bytePattern & 0xfc) == 0xf8) {
                sumb = bytePattern & 0x03;
                more = 4;
            } else {
                sumb = bytePattern & 0x01;
                more = 5;
            }
        }
        return buffer.toString();
    }

    /**
     * 获取十六进制字符串
     * @param buf
     * @return
     */
    private  String getHex(byte[] buf) {
        StringBuilder o = new StringBuilder(buf.length * 3);
        for (int i = 0; i < buf.length; i++) {
            int n = (int) buf[i] & 0xff;
            o.append("%");
            if (n < 0x10) {
                o.append("0");
            }
            o.append(Long.toString(n, 16).toUpperCase());
        }
        return o.toString();
    }
}
