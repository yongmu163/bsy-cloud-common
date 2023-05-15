package cn.bsy.cloud.common.mvc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author gaoh
 * @desc 分页显示对象
 * @date 2022年02月07日 上午 12:25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageDTO<T> implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 总条数
     */
    @JsonProperty(index = 0)
    private Long total;
    /**
     * 页条数
     */
    @JsonProperty(index = 1)
    private Long size;
    /**
     * 当前页
     */
    @JsonProperty(index = 2)
    private Long current;
    /**
     * 结果
     */
    @JsonProperty(index = 3)
    private List<T> records;
}