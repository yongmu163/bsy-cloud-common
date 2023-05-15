package cn.bsy.cloud.common.file.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: 书签BO节点模型
 * @Author gaoh
 * @Date 2022年10月23日 下午 3:24
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookMarkNode implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 书签英文标识
     */
    private String bookmarkName;

    /**
     * 书签对应值
     */
    private String bookmarkValue;

    /**
     * 书签表对象列表
     */
    private List<BookMarkTableNode> bookMarkTableNodeList;
}
