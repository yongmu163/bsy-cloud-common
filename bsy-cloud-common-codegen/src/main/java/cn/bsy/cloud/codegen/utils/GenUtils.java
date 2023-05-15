package cn.bsy.cloud.codegen.utils;


import cn.bsy.cloud.codegen.bo.ColumnSchemaBO;
import cn.bsy.cloud.codegen.bo.GenCodeBO;
import cn.bsy.cloud.codegen.bo.TableSchemaBO;
import cn.bsy.cloud.codegen.constant.CommonConstants;
import cn.bsy.cloud.common.core.exception.CustomizeException;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.google.common.base.CaseFormat;
import lombok.experimental.UtilityClass;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 代码生成器   工具类
 *
 * @author gaoheng
 * 2022年6月19日下午11:49:23
 */
@UtilityClass
public class GenUtils {
    /**
     * POJO实体类
     */
    private final String ENTITY_JAVA_VM = "Entity.java.vm";
    /**
     * BO实体类
     */
    private final String BO_JAVA_VM = "Bo.java.vm";
    /**
     * DTO请求对象
     */
    private final String ENTITY_REQUEST_DTO_JAVA_VM = "RequestDTO.java.vm";
    /**
     * DTO响应对象
     */
    private final String ENTITY_RESPONSE_DTO_JAVA_VM = "ResponseDTO.java.vm";
    /**
     * 接口
     */
    private final String INTERFACE_JAVA_VM = "Interface.java.vm";
    /**
     * 实现类
     */
    private final String SERVICE_JAVA_VM = "ServiceImpl.java.vm";
    /**
     * 持久层接口
     */
    private final String MAPPER_JAVA_VM = "Mapper.java.vm";
    /**
     * 持久层mybatis XML文件
     */
    private final String MAPPER_XML_VM = "Mapper.xml.vm";
    /**
     * 转换对象类
     */
    private final String CONVERTER_JAVA_VM = "Converter.java.vm";
    /**
     * 控制层
     */
    private final String CONTROLLER_JAVA_VM = "Controller.java.vm";


    private final String EXTERNAL_RESPONSE_DTO_JAVA_VM = "ExternalResponseDTO.java.vm";
    private final String EXTERNAL_REQUEST_DTO_JAVA_VM = "ExternalRequestDTO.java.vm";
    private final String EXTERNAL_CLIENT_DTO_JAVA_VM = "ExternalClient.java.vm";
    private final String EXTERNAL_HYSTRIX_DTO_JAVA_VM = "ExternalClientHystrix.java.vm";
    private final String EXTERNAL_CONTROLLER_DTO_JAVA_VM = "ExternalController.java.vm";
    private final String EXTERNAL_CONVERTER_DTO_JAVA_VM = "ExternalConverter.java.vm";
    private final String[] CONTROLLER_PACKAGES = {"api", "front", "mobile", "web"};

    //private final String VUE_LIST = "list.vue.vm";
    //private final String VUE_MODAL = "modal.vue.vm";
    //private final String ENTITY_JSON_VM = "Entity.json.vm";
    //private final String MENU_SQL_VM = "menu.sql.vm";
    //private final String API_JS_VM = "api.js.vm";
    //private final String CRUD_JS_VM = "crud.js.vm";

    /**
     * 获取通用模板
     *
     * @return
     */
    private List<String> getCommonTemplates() {
        List<String> templates = new ArrayList<>();
        // pojo实体类模板
        templates.add("template/" + ENTITY_JAVA_VM);
        // BO实体类模板
        templates.add("template/" + BO_JAVA_VM);
        //持久层接口
        templates.add("template/" + MAPPER_JAVA_VM);
        //mybatis xmlw文件
        templates.add("template/" + MAPPER_XML_VM);
        //接口文件
        templates.add("template/" + INTERFACE_JAVA_VM);
        //接口实现类
        templates.add("template/" + SERVICE_JAVA_VM);
        templates.add("template/" + EXTERNAL_RESPONSE_DTO_JAVA_VM);
        templates.add("template/" + EXTERNAL_REQUEST_DTO_JAVA_VM);
        templates.add("template/" + EXTERNAL_CLIENT_DTO_JAVA_VM);
        templates.add("template/" + EXTERNAL_HYSTRIX_DTO_JAVA_VM);
        templates.add("template/" + EXTERNAL_CONTROLLER_DTO_JAVA_VM);
        templates.add("template/" + EXTERNAL_CONVERTER_DTO_JAVA_VM);
        return templates;
    }

    private List<String> getTemplates() {
        List<String> templates = new ArrayList<>();
        templates.add("template/" + ENTITY_RESPONSE_DTO_JAVA_VM);
        templates.add("template/" + ENTITY_REQUEST_DTO_JAVA_VM);
        templates.add("template/" + CONTROLLER_JAVA_VM);
        templates.add("template/" + CONVERTER_JAVA_VM);

        return templates;
    }

    /**
     * 生成代码
     *
     * @param genCodeBO
     * @param table
     * @param zip
     */
    public void generatorCode(GenCodeBO genCodeBO, TableInfo table, ZipOutputStream zip) {
        //配置信息
        Configuration config = getConfig();
        boolean hasBigDecimal = false;
        boolean hasLocalDateTime = false;
        //封装表信息
        TableSchemaBO tableEntity = new TableSchemaBO();
        tableEntity.setTableName(table.getName());
        tableEntity.setImportPackages(table.getImportPackages());
        // 表注释
        if (StrUtil.isNotBlank(genCodeBO.getComments())) {
            tableEntity.setComments(genCodeBO.getComments());
        } else {
            tableEntity.setComments(table.getComment());
        }

        String tablePrefix;
        if (StrUtil.isNotBlank(genCodeBO.getTablePrefix())) {
            tablePrefix = genCodeBO.getTablePrefix();
        } else {
            tablePrefix = config.getString("tablePrefix");
        }

        //表名转换成Java类名
        String className = tableToJava(tableEntity.getTableName(), tablePrefix);
        tableEntity.setCaseClassName(className);
        tableEntity.setLowerClassName(StringUtils.uncapitalize(className));

        //列信息
        List<ColumnSchemaBO> columnList = new ArrayList<>();
        for (TableField column : table.getFields()) {
            ColumnSchemaBO columnEntity = new ColumnSchemaBO();
            columnEntity.setColumnName(column.getName());
            columnEntity.setDataType(column.getType());
            columnEntity.setComments(column.getComment());

            //列名转换成Java属性名
            String attrName = columnToJava(columnEntity.getColumnName());
            columnEntity.setCaseAttrName(attrName);
            columnEntity.setLowerAttrName(StringUtils.uncapitalize(attrName));

            //列的数据类型，转换成Java类型
            columnEntity.setAttrType(column.getPropertyType());
            if (!hasBigDecimal && "BigDecimal".equals(column.getPropertyType())) {
                hasBigDecimal = true;
            }
            if (!hasLocalDateTime && "LocalDateTime".equals(column.getPropertyType())) {
                hasLocalDateTime = true;
            }
            //是否主键
            if (column.isKeyFlag()) {
                tableEntity.setPk(columnEntity);
            }

            columnList.add(columnEntity);
        }
        tableEntity.setColumns(columnList);

        //没主键，则第一个字段为主键
        if (tableEntity.getPk() == null) {
            tableEntity.setPk(tableEntity.getColumns().get(0));
        }

        //设置velocity资源加载器
        Properties prop = new Properties();
        prop.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init(prop);
        //封装模板数据
        Map<String, Object> map = new HashMap<>(16);
        map.put("tableName", tableEntity.getTableName());
        map.put("pk", tableEntity.getPk());
        map.put("className", tableEntity.getCaseClassName());
        map.put("classname", tableEntity.getLowerClassName());
        map.put("importPackages", tableEntity.getImportPackages());
        map.put("pathName", tableEntity.getLowerClassName().toLowerCase());
        map.put("columns", tableEntity.getColumns());
        map.put("hasBigDecimal", hasBigDecimal);
        map.put("hasLocalDateTime", hasLocalDateTime);
        map.put("datetime", DateUtil.now());
        if (StrUtil.isNotBlank(genCodeBO.getComments())) {
            map.put("comments", genCodeBO.getComments());
        } else {
            map.put("comments", tableEntity.getComments());
        }

        if (StrUtil.isNotBlank(genCodeBO.getAuthor())) {
            map.put("author", genCodeBO.getAuthor());
        } else {
            map.put("author", config.getString("author"));
        }

        if (StrUtil.isNotBlank(genCodeBO.getModuleName())) {
            map.put("moduleName", genCodeBO.getModuleName());
        } else {
            map.put("moduleName", config.getString("moduleName"));
        }

        if (StrUtil.isNotBlank(genCodeBO.getPackageName())) {
            map.put("package", genCodeBO.getPackageName());
            map.put("mainPath", genCodeBO.getPackageName());
        } else {
            map.put("package", config.getString("package"));
            map.put("mainPath", config.getString("mainPath"));
        }
        VelocityContext context = new VelocityContext(map);
        //获取通用模板列表
        List<String> templates = getCommonTemplates();
        for (String template : templates) {
            //渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, CharsetUtil.UTF_8);
            tpl.merge(context, sw);

            try {
                //添加到zip
                zip.putNextEntry(new ZipEntry(Objects
                        .requireNonNull(getFileName(template, tableEntity.getCaseClassName()
                                , map.get("package").toString(), map.get("moduleName").toString(), null))));
                IoUtil.write(zip, StandardCharsets.UTF_8, false, sw.toString());
                IoUtil.close(sw);
                zip.closeEntry();
            } catch (IOException e) {
                throw new CustomizeException("渲染模板失败，表名：" + tableEntity.getTableName(), e);
            }
        }
        //获取通用模板列表
        List<String> templateList = getTemplates();
        for (String subPackage : CONTROLLER_PACKAGES) {
            String prefix = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, subPackage);
            if (!StringUtils.equals(subPackage, "web")) {
                context.put("prefix", prefix);
            }

            context.put("subPackage", subPackage);

            for (String template : templateList) {
                //渲染模板
                StringWriter sw = new StringWriter();
                Template tpl = Velocity.getTemplate(template, CharsetUtil.UTF_8);
                tpl.merge(context, sw);

                try {
                    //添加到zip
                    zip.putNextEntry(new ZipEntry(Objects
                            .requireNonNull(getFileName(template, tableEntity.getCaseClassName()
                                    , map.get("package").toString(), map.get("moduleName").toString(), subPackage))));
                    IoUtil.write(zip, StandardCharsets.UTF_8, false, sw.toString());
                    IoUtil.close(sw);
                    zip.closeEntry();
                } catch (IOException e) {
                    throw new CustomizeException("渲染模板失败，表名：" + tableEntity.getTableName(), e);
                }
            }
        }

    }


    /**
     * 列名转换成Java属性名
     */
    private String columnToJava(String columnName) {
        return WordUtils.capitalizeFully(columnName, new char[]{'_'}).replace("_", "");
    }

    /**
     * 表名转换成Java类名
     */
    private String tableToJava(String tableName, String tablePrefix) {
        if (StringUtils.isNotBlank(tablePrefix)) {
            tableName = tableName.replaceFirst(tablePrefix, "");
        }
        return columnToJava(tableName);
    }

    /**
     * 获取配置信息
     */
    private Configuration getConfig() {
        try {
            return new PropertiesConfiguration("generator.properties");
        } catch (ConfigurationException e) {
            throw new CustomizeException("获取配置文件失败，", e);
        }
    }

    /**
     * 获取文件名
     */
    private String getFileName(String template, String className, String packageName, String moduleName, String subPackage) {
        String packagePath = CommonConstants.BACK_END_PROJECT + File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator;
        String microPackagePath = CommonConstants.MICRO_PROJECT + File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator;
        if (StringUtils.isNotBlank(packageName)) {
            packagePath += packageName.replace(".", File.separator) + File.separator + moduleName + File.separator;
            microPackagePath += packageName.replace(".", File.separator) + File.separator + moduleName + File.separator;
        }
        // api
        if (template.contains(EXTERNAL_CLIENT_DTO_JAVA_VM)) {
            return microPackagePath + File.separator + "client" + File.separator + "External" + className + "Client.java";
        }
        if (template.contains(EXTERNAL_HYSTRIX_DTO_JAVA_VM)) {
            return microPackagePath + File.separator + "client" + File.separator + "External" + className + "ClientHystrix.java";
        }
        if (template.contains(EXTERNAL_RESPONSE_DTO_JAVA_VM)) {
            return microPackagePath + File.separator + "dto" + File.separator + "External" + className + "ResponseDTO.java";
        }
        if (template.contains(EXTERNAL_REQUEST_DTO_JAVA_VM)) {
            return microPackagePath + File.separator + "dto" + File.separator + "External" + className + "RequestDTO.java";
        }

        if (template.contains(EXTERNAL_CONTROLLER_DTO_JAVA_VM)) {
            return packagePath + File.separator + "external/controller" + File.separator + "External" + className + "Controller.java";
        }
        if (template.contains(EXTERNAL_CONVERTER_DTO_JAVA_VM)) {
            return packagePath + File.separator + "external/converter" + File.separator + "External" + className + "Converter.java";
        }
        // main-model
        if (template.contains(ENTITY_JAVA_VM)) {
            return packagePath + File.separator + "model" + File.separator + className + ".java";
        }
        // main-bo
        if (template.contains(BO_JAVA_VM)) {
            return packagePath + File.separator + "bo" + File.separator + className + "BO.java";
        }
        // main-mapper
        if (template.contains(MAPPER_JAVA_VM)) {
            return packagePath + File.separator + "mapper" + File.separator + className + "Mapper.java";
        }
        // main-service
        if (template.contains(SERVICE_JAVA_VM)) {
            String sss = packagePath + File.separator + "service" + File.separator + "impl" + File.separator + className + "ServiceImpl.java";
            return sss;
        }
        // main-service
        if (template.contains(INTERFACE_JAVA_VM)) {
            return packagePath + File.separator + "service" + File.separator + "I" + className + "Service.java";
        }

        // main-api
        String prefix = (subPackage == null || StringUtils.equals(subPackage, "web")) ? "" : CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, subPackage);
        if (template.contains(ENTITY_RESPONSE_DTO_JAVA_VM)) {
            return packagePath + File.separator + subPackage + File.separator + "dto" + File.separator + prefix + className + "ResponseDTO.java";
        }


        if (template.contains(ENTITY_REQUEST_DTO_JAVA_VM)) {
            return packagePath + File.separator + subPackage + File.separator + "dto" + File.separator + prefix + className + "RequestDTO.java";
        }

        if (template.contains(CONTROLLER_JAVA_VM)) {
            return packagePath + File.separator + subPackage + File.separator + "controller" + File.separator + prefix + className + "Controller.java";
        }

        if (template.contains(CONVERTER_JAVA_VM)) {
            return packagePath + File.separator + subPackage + File.separator + "converter" + File.separator + prefix + className + "Converter.java";
        }

        if (template.contains(MAPPER_XML_VM)) {
            return CommonConstants.BACK_END_PROJECT + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "mapper" + File.separator + className + "Mapper.xml";
        }

        /*if (template.contains(ENTITY_JSON_VM)) {
            return CommonConstants.BACK_END_PROJECT  + File.separator + className + ".json";
        }*/

        /*if (template.contains(MENU_SQL_VM)) {
            return className.toLowerCase() + "_menu.sql";
        }*/
        /*if (template.contains(VUE_LIST)) {
            return CommonConstants.FRONT_END_PROJECT + File.separator + "src" + File.separator + "vue" + File.separator + StringUtils.uncapitalize(className) + File.separator + "list.vue";
        }*/

        /*if (template.contains(VUE_MODAL)) {
            return CommonConstants.FRONT_END_PROJECT + File.separator + "src" + File.separator + "vue" + File.separator + StringUtils.uncapitalize(className) + File.separator + "modal.vue";
        }*/

        /*if (template.contains(API_JS_VM)) {
            return CommonConstants.FRONT_END_PROJECT + File.separator + "src" + File.separator + "api" + File.separator + className.toLowerCase() + ".js";
        }*/

        /*if (template.contains(CRUD_JS_VM)) {
            return CommonConstants.FRONT_END_PROJECT + File.separator + "src" + File.separator + "const" + File.separator + "crud" + File.separator + className.toLowerCase() + ".js";
        }*/

        return null;
    }
}
