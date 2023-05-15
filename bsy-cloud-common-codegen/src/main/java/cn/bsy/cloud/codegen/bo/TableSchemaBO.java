package cn.bsy.cloud.codegen.bo;

import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * 表属性
 *
 * @author gaoheng
 * 2019年7月28日上午12:36:20
 */
@Data
public class TableSchemaBO {
    /**
     * 名称
     */
    private String tableName;
    /**
     * 备注
     */
    private String comments;
    /**
     * 主键
     */
    private ColumnSchemaBO pk;
    /**
     * 列名
     */
    private List<ColumnSchemaBO> columns;
    /**
     * 驼峰类型
     */
    private String caseClassName;
    /**
     * 普通类型
     */
    private String lowerClassName;
    /**
     * 额外导入包
     */
    private Set<String> importPackages;
}
