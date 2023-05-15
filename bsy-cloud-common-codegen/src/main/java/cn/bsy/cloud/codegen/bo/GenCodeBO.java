package cn.bsy.cloud.codegen.bo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Description: 配置模型
 * @Author gaoh
 * @Date 2022/6/18 0018
 **/
@Data
public class GenCodeBO {
    /**
     * 包名
     */
    @NotBlank(message = "包名不能为空")
    private String packageName;
    /**
     * 作者
     */
    private String author;
    /**
     * 模块名称
     */
    @NotBlank(message = "模块名称不能为空")
    private String moduleName;
    /**
     * 表前缀
     */
    private String tablePrefix;
    /**
     * 表名称
     */
    @NotBlank(message = "表名称不能为空")
    private String tableName;

    /**
     * 表备注
     */
    private String comments;
}
