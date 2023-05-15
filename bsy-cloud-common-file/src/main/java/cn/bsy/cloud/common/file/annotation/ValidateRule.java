package cn.bsy.cloud.common.file.annotation;

import cn.bsy.cloud.common.file.constant.ColumnDataTypeEnum;
import cn.bsy.cloud.common.file.constant.ColumnDateFormatEnum;

import java.lang.annotation.*;

/**
 * 校验字段格式
 *
 * @author sunxf
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ValidateRule {

    /**
     * 字段列名
     */
    String columnName() default "";
    /**
     * 是否必填
     */
    boolean required() default false;

    /**
     * 类型
     */
    ColumnDataTypeEnum type() default ColumnDataTypeEnum.STRING;

    /**
     * 长度约束
     */
    int maxLen() default 0;

    /**
     * 字典
     */
    String dict() default "";

    /**
     * 日期格式
     */
    ColumnDateFormatEnum dateFormat() default  ColumnDateFormatEnum.YYYY_MM_DD_1;

    /**
     * 是否单独校验重复数据
     */
    boolean isUnique() default false;

    /**
     * 是否组合校验重复数据
     */
    boolean isGroupUnique() default false;
}
