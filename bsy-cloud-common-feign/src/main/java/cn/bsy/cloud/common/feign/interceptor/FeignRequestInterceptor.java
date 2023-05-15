package cn.bsy.cloud.common.feign.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * @author gaoh
 * @desc OpenFeign拦截器
 * @date 2022年02月12日 下午 11:54
 */
@Slf4j
public class FeignRequestInterceptor implements RequestInterceptor {
    private static final String CONTENT_LENGTH = "content-length";
    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String name = headerNames.nextElement();
                String values = request.getHeader(name);
                /**
                 * 不能把当前请求的 content-length 传递到下游的服务提供方
                 * 因为有可能导致请求可能一直返回不了, 或者是请求响应数据被截断
                 */
                if (!name.equalsIgnoreCase(CONTENT_LENGTH)) {
                    log.info("name:{},values:{}", name, values);
                    requestTemplate.header(name, values);
                }
            }
        }
    }
}