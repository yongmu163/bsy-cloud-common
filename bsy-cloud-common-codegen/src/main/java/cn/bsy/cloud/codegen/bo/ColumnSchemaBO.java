package cn.bsy.cloud.codegen.bo;

import lombok.Data;

/**
 * 列属性
 *
 * @author gaoheng
 * 2019年7月28日上午12:35:50
 */
@Data
public class ColumnSchemaBO {
    /**
     * 列表
     */
    private String columnName;
    /**
     * 数据类型
     */
    private String dataType;
    /**
     * 备注
     */
    private String comments;

    /**
     * 驼峰属性
     */
    private String caseAttrName;
    /**
     * 普通属性
     */
    private String lowerAttrName;
    /**
     * 属性类型
     */
    private String attrType;
    /**
     * 其他信息
     */
    private String extra;
}
