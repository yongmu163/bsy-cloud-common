package cn.bsy.cloud.common.file.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 日期格式
 *
 * @Author: lichen
 * @Date: 2023/3/17
 **/
@AllArgsConstructor
public enum ColumnDateFormatEnum {
    /**
     * yyyy-mm-dd
     */
    YYYY_MM_DD_1("yyyy-mm-dd"),

    /**
     * yyyy-MM-dd HH:mm:ss
     */
    YYYY_MM_DD_HH_MM_SS("yyyy-MM-dd HH:mm:ss");

    @Getter
    private String formatString;

}
