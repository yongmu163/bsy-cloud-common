package cn.bsy.cloud.common.core.service.password;


import org.springframework.util.DigestUtils;

/**
 * @author gaoh
 * @desc 11
 * @date 2022年01月19日 下午 6:37
 */
public class PasswordEncoderServiceImpl  {
    /**
     * 盐，用于混交md5
     */
    private static String salt = "asdwqAsd12_qS";

    /**
     * 生成md5
     * @param str
     * @return
     */
    public static String getMd5(String str) {
        String base = str + "/" + salt;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }
    public static void main(String[] args) {
        System.out.println(getMd5("12346"));
        System.out.println(getMd5("12346"));
    }
}