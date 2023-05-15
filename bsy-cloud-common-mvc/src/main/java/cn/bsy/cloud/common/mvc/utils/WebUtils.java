package cn.bsy.cloud.common.mvc.utils;


import cn.bsy.cloud.common.core.constant.CommonConstant;
import cn.bsy.cloud.common.core.vo.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * 网络请求工具类
 *
 * @author gaoheng
 * @date: 2022年6月11日 下午5:30:25
 */
@Slf4j
@UtilityClass
public class WebUtils {

    /**
     * 获取 HttpServletRequest
     */
    public HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    /**
     * 获取 HttpServletResponse
     */
    public HttpServletResponse getResponse() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    }

    /**
     * 将过滤器链抛出的异常转为json对象
     *
     * @throws
     * @param: @param resultDTO
     * @return: void
     */
    public <T> void exceptionToJsonObject(Result<T> resultDTO) {
        HttpServletResponse response = WebUtils.getResponse();
        ObjectMapper mapper = new ObjectMapper();
        response.setContentType("text/html");
        response.setCharacterEncoding(CommonConstant.CHARSET);
        // 设置允许跨域请求
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Origin", response.getHeader("Origin"));
        response.setStatus(resultDTO.getCode());
        try {
            PrintWriter printWriter = response.getWriter();
            printWriter.append(mapper.writeValueAsString(resultDTO));
        } catch (Exception e) {
            log.error("IO异常:", e);
        }
    }
}
