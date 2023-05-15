package cn.bsy.cloud.common.core.constant;

/**
 * @author gaoh
 * @desc Token失效期类型枚举
 * @date 2022年01月31日 下午 12:00
 */
public enum TokenExpireDayType {

    /**
     * 令牌本身失效期1天，在缓存库失效期5天(默认)
     */
    TOKEN_EXPIRE_DAY_TYPE_1D_5D_DEFAULT(1,5),
    /**
     * 令牌本身失效期2天，在缓存库失效期7天
     */
    TOKEN_EXPIRE_DAY_TYPE_2D_7D(2,10),
    /**
     * 令牌本身失效期3天，在缓存库失效期8天
     */
    TOKEN_EXPIRE_DAY_TYPE_3D_8D(3,15),
    /**
     * 令牌本身失效期4天，在缓存库失效期9天
     */
    TOKEN_EXPIRE_DAY_TYPE_4D_9D(4,20),
    /**
     * 令牌本身失效期5天，在缓存库失效期10天
     */
    TOKEN_EXPIRE_DAY_TYPE_5D_10D(5,25);

    Integer tokenSelfExpireDay;

    Integer tokenInCacheExpireDay;
    public Integer getTokenSelfExpireDay() {
        return tokenSelfExpireDay;
    }
    public Integer getTokenInCacheExpireDay() {
        return tokenInCacheExpireDay;
    }
    TokenExpireDayType(Integer tokenSelfExpireDay, Integer tokenInCacheExpireDay) {
        this.tokenSelfExpireDay = tokenSelfExpireDay;
        this.tokenInCacheExpireDay = tokenInCacheExpireDay;
    }

    /**
     * 根据令牌本身失效期获取缓存失效期
     * @param tokenSelfExpireDay
     * @return
     */
    public static Integer getTokenInCacheExpireDay(Integer tokenSelfExpireDay) {
        for (TokenExpireDayType tokenExpireDayType : values()) {
            if (tokenSelfExpireDay.intValue() == tokenExpireDayType.tokenSelfExpireDay.intValue()) {
                return tokenExpireDayType.tokenInCacheExpireDay;
            }
        }
        return null;
    }

}