package cn.bsy.cloud.common.file.annotation;

import java.lang.annotation.*;

/**
 * 校验字段格式
 *
 * @author sunxf
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AutoValidateExcel {

}
