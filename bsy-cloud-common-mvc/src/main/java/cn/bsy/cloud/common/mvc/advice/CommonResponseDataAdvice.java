package cn.bsy.cloud.common.mvc.advice;



import cn.bsy.cloud.common.core.constant.CommonConstant;
import cn.bsy.cloud.common.core.utils.ThreadLocalUtils;
import cn.bsy.cloud.common.core.vo.Result;
import cn.bsy.cloud.common.mvc.annotation.IgnoreResponseAdvice;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.List;


/**
 * @author gaoh
 * @desc 实现统一响应
 * @date 2022年01月08日 下午 10:10
 */
@Slf4j
@RestControllerAdvice(annotations = RestController.class)
public class CommonResponseDataAdvice implements ResponseBodyAdvice<Object> {
    /**
     * 判断是否需要对相应进行处理
     *
     * @param methodParameter
     * @param aClass
     * @return
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        // 存在IgnoreResponseAdvice的类，不进行统一响应处理
        if (methodParameter.getDeclaringClass()
                .isAnnotationPresent(IgnoreResponseAdvice.class)) {
            return false;
        }
        // 存在IgnoreResponseAdvice的方法，不进行统一响应处理
        return !methodParameter.getMethod().isAnnotationPresent(IgnoreResponseAdvice.class);
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType,
                                  Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest,
                                  ServerHttpResponse serverHttpResponse) {
        Result result;
        String refreshedToken=null;
        if (ObjectUtil.isNull(o)) {
            result = Result.ok();
        } else if (o instanceof Result) {
            result = (Result) o;
        } else {
            result = Result.ok(o);
        }
        List<String> refreshedTokenList = serverHttpRequest.getHeaders().get(CommonConstant.THREAD_LOCAL_TOKEN_REFRESHED);
        if(CollUtil.isNotEmpty(refreshedTokenList)) {
            refreshedToken =  refreshedTokenList.get(0);
        }
        if(ObjectUtil.isNotNull(ThreadLocalUtils.getValue(CommonConstant.THREAD_LOCAL_TOKEN_REFRESHED))) {
            refreshedToken = (String) ThreadLocalUtils.getValue(CommonConstant.THREAD_LOCAL_TOKEN_REFRESHED);
        }
        // 如果线程变量里没有刷新后的token，说明前端传的token没有任何过期问题，返回即可
        if(StrUtil.isNotBlank(refreshedToken)) {
            result.setRefreshedJwtToken(refreshedToken);
        }
        return result;
    }
}