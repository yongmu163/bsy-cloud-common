package cn.bsy.cloud.codegen.service;

import cn.bsy.cloud.codegen.bo.GenCodeBO;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;

import java.util.List;

/**
 * @Description: 代码生成接口
 * @Author gaoh
 * @Date 2023/5/13 0013 下午 4:57
 **/
public interface ICodeGenreatorService {
    /**
     * 获取数据库表结构信息
     * @param tableName
     * @return
     */
    List<TableInfo> getTableList(String tableName);

    /**
     * 生成代码
     * @param genCodeBO
     * @return
     */
    byte[] generatorCode(GenCodeBO genCodeBO);
}
