package cn.bsy.cloud.common.shiro.filter;


import cn.bsy.cloud.common.core.constant.CommonConstant;
import cn.bsy.cloud.common.core.constant.HttpMethodEnum;
import cn.bsy.cloud.common.core.constant.TokenExpireDayType;
import cn.bsy.cloud.common.core.vo.Result;
import cn.bsy.cloud.common.jwt.constant.TokenTypeEnum;
import cn.bsy.cloud.common.jwt.dto.JwtTokenDTO;
import cn.bsy.cloud.common.jwt.dto.LoginInfoDTO;
import cn.bsy.cloud.common.jwt.service.IGenerateJwtKeyService;
import cn.bsy.cloud.common.jwt.service.IJwtCacheService;
import cn.bsy.cloud.common.jwt.service.IJwtService;
import cn.bsy.cloud.common.jwt.service.impl.JwtObjectInitServiceImpl;
import cn.bsy.cloud.common.mvc.utils.WebUtils;
import cn.bsy.cloud.common.shiro.Oauth2Token;
import cn.bsy.cloud.common.shiro.utils.LoginContext;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.springframework.context.annotation.Scope;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author gaoh
 * @desc 验证令牌过滤器
 * @Scope的含义，将OAuth2Filter变为多例对象
 * @date 2022年06月13日 下午 2:52
 */
@Slf4j
@Scope("prototype")
@AllArgsConstructor
@NoArgsConstructor
public class Oauth2Filter extends AuthenticatingFilter {
    private IJwtService jwtService;
    private IJwtCacheService jwtCacheService;
    private IGenerateJwtKeyService generateJwtKeyService;

    /**
     * 判断哪些请求需要被shiro框架拦截
     *
     * @param request
     * @param response
     * @param mappedValue
     * @return
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        HttpServletRequest req = (HttpServletRequest) request;
        // OPTIONS为http试探性握手网络连接，shiro框架不拦截
        return req.getMethod().equals(HttpMethodEnum.OPTIONS.getName());
    }

    /**
     * shiro拦截请求之后，用于将令牌字符串封装为shiro框架接收的令牌对象
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest req = (HttpServletRequest) request;
        String token = getRequestToken(req);
        if (StrUtil.isBlank(token)) {
            return null;
        }
        // 如果线程变量中存在token，说明原token已经过期，线程变量中的token是在onAccessDenied方法中重新生成的
        if(StrUtil.isNotBlank(LoginContext.getToken())) {
            token = LoginContext.getToken();
        }
        // 封装成shiro的令牌对象返回
        return new Oauth2Token(token);
    }

    /**
     * onAccessDenied 的executeLogin返回false
     * 说明“认证”失败，则调用该方法
     *
     * @param token
     * @param e
     * @param request
     * @param response
     * @return
     */
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        return false;
    }

    /**
     * 如果请求被shiro处理（拦截），则该方法开始处理请求
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest req = (HttpServletRequest) request;
        // 清空线程变量里存储的token
        LoginContext.clearToken();
        // 清空线程变量里存储的刷新token
        LoginContext.clearRefreshedToken();
        String token = getRequestToken(req);
        if (StrUtil.isBlank(token)) {
            WebUtils.exceptionToJsonObject(Result.error(HttpStatus.SC_UNAUTHORIZED, "令牌不存在"));
            return false;
        }
        try {
            jwtService.verifierToken(token, generateJwtKeyService);
            // 如token校验没有抛出异常，则将从头信息中传入的token重新存入线程变量
            LoginContext.setToken(token);
        } catch (ExpiredJwtException e) {
            // 令牌本身已经过期，检测缓存数据库中的token是否过期，如果缓存中存在，说明令牌没有完全过期，进行刷新
            if (jwtCacheService.hasToken(token)) {
                jwtCacheService.removeTokenFromCache(token);
                // 从超时异常信息中获取令牌的payload信息
                Claims body = e.getClaims();
                // 转为loginInfo对象
                LoginInfoDTO loginInfo = JSON.parseObject(body.get(CommonConstant.JWT_LOGIN_INFO_KEY)
                        .toString(), LoginInfoDTO.class);
                // 重新生成token
                JwtTokenDTO jwtToken = jwtService.generateToken(loginInfo, generateJwtKeyService,
                        new JwtObjectInitServiceImpl(TokenTypeEnum.USER_TYPE, TokenExpireDayType.TOKEN_EXPIRE_DAY_TYPE_1D_5D_DEFAULT));
                // 重新刷新缓存
                jwtCacheService.setTokenToCache(jwtToken);
                // 将刷新后的token对象存入线程变量中
                LoginContext.setToken(jwtToken.getToken());
                // 将刷新后的token对象存入线程变量中，该token通过"统一响应拦截器"返回给前端Result对象中
                LoginContext.setRefreshedToken(jwtToken.getToken());
            } else {
                // 令牌已经完全过期，需要重新申请
                WebUtils.exceptionToJsonObject(Result.error(HttpStatus.SC_UNAUTHORIZED, "令牌已过期"));
            }
        } catch (SignatureException e) {
            WebUtils.exceptionToJsonObject(Result.error(HttpStatus.SC_UNAUTHORIZED, "令牌鉴签错误"));
            return false;
        } catch (Exception e) {
            WebUtils.exceptionToJsonObject(Result.error(HttpStatus.SC_UNAUTHORIZED, "无效的令牌"));
            return false;
        }
        // 执行这个方法会触发shiro框架调用Realm类
        return executeLogin(request, response);
    }

    /**
     * 在请求头信息中获取token字符串
     *
     * @param request
     * @return
     */
    private String getRequestToken(HttpServletRequest request) {
        String token = request.getHeader(CommonConstant.THREAD_LOCAL_TOKEN);
        if (StrUtil.isBlank(token)) {
            token = request.getParameter(CommonConstant.THREAD_LOCAL_TOKEN);
        }
        return token;
    }

    @Override
    public void doFilterInternal(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        super.doFilterInternal(request, response, chain);
    }
}