package cn.bsy.cloud.common.core.constant;

/**
 * @author gaoh
 * @desc http协议方法类型
 * @date 2022年01月13日 下午 4:53
 */
public enum HttpMethodEnum {
    /**
     * POST常量
     */
    POST("POST"),
    /**
     * DELETE常量
     */
    DELETE("DELETE"),
    /**
     * GET常量
     */
    GET("GET"),
    /**
     * PUT常量
     */
    PUT("PUT"),
    /**
     * OPTIONS常量
     */
    OPTIONS("OPTIONS");

    String name;

    HttpMethodEnum(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
}