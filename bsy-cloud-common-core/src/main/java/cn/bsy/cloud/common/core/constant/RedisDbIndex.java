package cn.bsy.cloud.common.core.constant;

/**
 * @author gaoh
 * @desc 缓存数据库枚举
 * @date 2022年01月31日 下午 3:45
 */
public enum RedisDbIndex {
    /**
     * redis0数据库
     */
    REDIS_DB_INDEX_0(0),
    /**
     * redis1数据库
     */
    REDIS_DB_INDEX_1(1),
    /**
     * redis2数据库
     */
    REDIS_DB_INDEX_2(2),
    /**
     * redis3数据库
     */
    REDIS_DB_INDEX_3(3),
    /**
     * redis4数据库
     */
    REDIS_DB_INDEX_4(4),
    /**
     * redis5数据库
     */
    REDIS_DB_INDEX_5(5),
    /**
     * redis6数据库
     */
    REDIS_DB_INDEX_6(6),
    /**
     * redis7数据库
     */
    REDIS_DB_INDEX_7(7),
    /**
     * redis8数据库
     */
    REDIS_DB_INDEX_8(8),
    /**
     * redis9数据库
     */
    REDIS_DB_INDEX_9(9),
    /**
     * redis10数据库
     */
    REDIS_DB_INDEX_10(10),
    /**
     * redis11数据库
     */
    REDIS_DB_INDEX_11(11),
    /**
     * redis12数据库
     */
    REDIS_DB_INDEX_12(12),
    /**
     * redis13数据库
     */
    REDIS_DB_INDEX_13(13),
    /**
     * redis14数据库
     */
    REDIS_DB_INDEX_14(14),
    /**
     * redis15数据库
     */
    REDIS_DB_INDEX_15(15);

    Integer index;

    public Integer getIndex() {
        return index;
    }

    RedisDbIndex(Integer index) {
        this.index = index;
    }
}