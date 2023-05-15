package cn.bsy.cloud.common.file.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description: 书签BO节点模型
 * @Author gaoh
 * @Date 2022年10月23日 下午 3:24
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookMarkTableNode implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 行标识
     */
    private String rowId;

    /**
     * 表列名
     */
    private String tableColumnName;

    /**
     * 表列值
     */
    private String tableColumnValue;
}
