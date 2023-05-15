package cn.bsy.cloud.common.core.utils;

import cn.hutool.core.util.StrUtil;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * IP工具类
 * @author bell
 */
@Slf4j
@UtilityClass
public class IpUtil {

    public static final String UNKNOWN = "unknown";

    /**
     * 获取当前IP地址
     * @return IP地址
     */
    public static String getIp()
    {
        InetAddress ip=null;
        try {
            ip = Inet4Address.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return ip.getHostAddress();
    }

    /**
     * 获取客户端IP
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request)
    {
        if (request == null)
        {
            return null;
        }

        String ip = null;

        // X-Forwarded-For：Squid 服务代理
        String ipAddresses = request.getHeader("X-Forwarded-For");
        if (ipAddresses == null || ipAddresses.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddresses))
        {
            // Proxy-Client-IP：apache 服务代理
            ipAddresses = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddresses == null || ipAddresses.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddresses))
        {
            // WL-Proxy-Client-IP：weblogic 服务代理
            ipAddresses = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddresses == null || ipAddresses.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddresses))
        {
            // HTTP_CLIENT_IP：有些代理服务器
            ipAddresses = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ipAddresses == null || ipAddresses.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddresses))
        {
            // X-Real-IP：nginx服务代理
            ipAddresses = request.getHeader("X-Real-IP");
        }

        // 有些网络通过多层代理，那么获取到的ip就会有多个，一般都是通过逗号（,）分割开来，并且第一个ip为客户端的真实IP
        if (ipAddresses != null && ipAddresses.length() != 0)
        {
            ip = ipAddresses.split(",")[0];
        }

        // 还是不能获取到，最后再通过request.getRemoteAddr();获取
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddresses))
        {
            ip = request.getRemoteAddr();
        }
        return StrUtil.equals(ip, "0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip;
    }

}
