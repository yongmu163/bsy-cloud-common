package cn.bsy.cloud.common.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @Description: TODO
 * @Author gaoh
 * @Date 2022年10月09日 下午 11:39
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseTree {
    /**
     * 树节点主键ID
     */
    String id;
    /**
     * 树节点父ID
     */
    String pid;
    /**
     * 树节点名称
     */
    String name;
    /**
     * 树节点顺讯
     */
    Integer sort;
    /**
     * 扩展信息
     */
    Map<String, Object> extraMap;
}
