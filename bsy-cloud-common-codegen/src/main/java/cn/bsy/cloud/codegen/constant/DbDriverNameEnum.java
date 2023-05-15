package cn.bsy.cloud.codegen.constant;

/**
 * @Description: 数据库驱动名称枚举
 * @Author gaoh
 * @Date 2023/5/15 0015 上午 10:32
 **/
public enum DbDriverNameEnum {

    /**
     * mysql驱动
     */
    MYSQL_DRIVER("com.mysql.cj.jdbc.Driver","mysql驱动"),
    /**
     * oracle驱动
     */
    ORACLE_DRIVER("oracle.jdbc.driver.OracleDriver","oracle驱动");

    /**
     * 驱动名称
     */
    private  String driverName;
    /**
     * 描述
     */
    private  String desc;

    DbDriverNameEnum(String driverName, String desc) {
        this.driverName = driverName;
        this.desc = desc;
    }

    public String getDriverName() {
        return driverName;
    }
}
