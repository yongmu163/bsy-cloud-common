package cn.bsy.cloud.common.core.utils;

import lombok.experimental.UtilityClass;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

/**
 * @author gaoh
 * @desc URL匹配工具
 * @date 2022年01月30日 下午 9:30
 */
@UtilityClass
public class UrlPathMatcherUtils {
    /**
     * 匹配URL后缀信息，具体匹配规则查询AntPathMatcher匹配规则
     * @param pattern 后缀
     * @param path    url地址
     * @return 匹配成功：true 不匹配失败：false
     */
    public boolean matchUrlSuffix(String pattern, String path) {
        PathMatcher matcher = new AntPathMatcher();
        return matcher.match(pattern, path);
    }

    public static void main(String[] args) {
        System.out.println(matchUrlSuffix("/**/web/code/img/**", "/auth/web/code/img/1112222"));
    }
}