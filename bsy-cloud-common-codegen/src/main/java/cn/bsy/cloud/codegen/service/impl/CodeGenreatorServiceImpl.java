package cn.bsy.cloud.codegen.service.impl;

import cn.bsy.cloud.codegen.bo.GenCodeBO;
import cn.bsy.cloud.codegen.service.ICodeGenreatorService;
import cn.bsy.cloud.codegen.utils.GenUtils;
import cn.bsy.cloud.common.core.exception.CustomizeException;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipOutputStream;

/**
 * @Description: 代码生成器数据库表操作类
 * @Author gaoh
 * @Date 2023年05月13日 下午 5:21
 **/
@AllArgsConstructor
public class CodeGenreatorServiceImpl implements ICodeGenreatorService {
    private DataSourceConfig dataSourceConfig;

    @Override
    public List<TableInfo> getTableList(String tableName) {
        ConfigBuilder config = new ConfigBuilder(null, dataSourceConfig, null, null, null);
        List<TableInfo> tableInfoList = config.getTableInfoList();
        if (StrUtil.isNotBlank(tableName)) {
            return tableInfoList.stream().filter(tableInfo -> StringUtils.
                    endsWith(tableName, tableInfo.getName())).collect(Collectors.toList());
        }
        return tableInfoList;
    }

    @Override
    public byte[] generatorCode(GenCodeBO genCodeBO) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        List<TableInfo> tableInfoList = this.getTableList(genCodeBO.getTableName());
        if (CollUtil.isEmpty(tableInfoList)) {
            throw new CustomizeException("未匹配到表");
        }
        if (tableInfoList.size() > 1) {
            throw new CustomizeException("匹配到多张表,无法生成代码");
        }
        TableInfo tableInfo = tableInfoList.get(0);
        GenUtils.generatorCode(genCodeBO, tableInfo, zip);
        IoUtil.close(zip);
        return outputStream.toByteArray();
    }
}
