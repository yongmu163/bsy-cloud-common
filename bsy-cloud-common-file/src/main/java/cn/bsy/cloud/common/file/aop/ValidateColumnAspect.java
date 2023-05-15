package cn.bsy.cloud.common.file.aop;

import cn.bsy.cloud.common.core.exception.CustomizeException;
import cn.bsy.cloud.common.core.utils.RegexUtils;
import cn.bsy.cloud.common.file.annotation.AutoValidateExcel;
import cn.bsy.cloud.common.file.annotation.ValidateRule;
import cn.bsy.cloud.common.file.constant.ColumnDataTypeEnum;
import cn.bsy.cloud.common.file.constant.ColumnDateFormatEnum;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.bidimap.DualHashBidiMap;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 字段校验切面
 *
 * @author sunxf
 */
@Aspect
@Component
@Slf4j
public class ValidateColumnAspect {

    private static final int ARGUMENTS_LENGTH = 3;


    /**
     * 切面校验
     *
     * @param point
     * @param autoValidateExcel
     * @return java.lang.Object
     * @author SuiHaiyang
     * @date 2023/4/23 13:34
     */
    @Around("@annotation(autoValidateExcel)")
    public Object doAround(ProceedingJoinPoint point, AutoValidateExcel autoValidateExcel) throws Throwable {
        log.info("我进入了切面！！！");
        // 参数检查
        argsCheck(point);
        // 格式检查
        List<String> validate = validate(point.getArgs()[0], Integer.valueOf(point.getArgs()[1].toString()), Integer.valueOf(point.getArgs()[2].toString()));

        log.error("validate:{}", validate);

        if (validate.size() > 0) {
            throw new CustomizeException(JSONObject.toJSON(validate).toString());
        }
        return point.proceed(point.getArgs());
    }

    /**
     * 参数检查
     *
     * @Author: lichen
     * @Date: 2023/3/17
     **/
    private void argsCheck(ProceedingJoinPoint point) {
        Object[] arguments = point.getArgs();
        if (arguments.length != ARGUMENTS_LENGTH || !(arguments[0] instanceof List<?>)) {
            throw new IllegalArgumentException("请确保参数是3个，且泛型与注解中的rowObjClazz一致。");
        }

    }

    /**
     * 格式检查带行号头的
     *
     * @Author: SuiHaiyang
     * @Date: 2023/3/17
     **/
    private List<String> validate(Object arg, int headRowNum, int exceptionLineCount) throws IllegalAccessException {
        List<?> rows = (List<?>) arg;
        //错误详情集合
        List<String> exceptionMessage = new ArrayList<>();
        //组合重复校验
        BidiMap groupMap = new DualHashBidiMap();
        //单值重复校验
        BidiMap oneMap = new DualHashBidiMap();
        boolean groupUniqueFlag = false;
        // 开始遍历excel数据进行校验
        for (int i = 1; i <= rows.size(); i++) {
            Object row = rows.get(i - 1);
            String columnName = "";
            Field[] fields = row.getClass().getDeclaredFields();
            StringBuffer groupString = new StringBuffer();
            StringBuffer groupColumnNameString = new StringBuffer();
            for (Field field : fields) {
                if (field.isAnnotationPresent(ValidateRule.class)) {
                    ValidateRule annotation = field.getAnnotation(ValidateRule.class);
                    field.setAccessible(true);
                    // 取列名
                    if (StrUtil.isBlank(annotation.columnName())) {
                        // easyExcel 自带属性，用index 配置
                        ExcelProperty excelProperty = field.getAnnotation(ExcelProperty.class);
                        throw new CustomizeException("第" + excelProperty.index() + "列，对应列名不能为空");
                    } else {
                        columnName = annotation.columnName();
                    }
                    if (annotation.isGroupUnique() && annotation.isUnique()) {
                        throw new CustomizeException("isUnique和isGroupUnique只能选择一个");
                    }
                    // 单元格的值
                    Object value = field.get(row);
                    //非空校验
                    if (annotation.required() && StringUtils.isEmpty((CharSequence) value)) {
                        addExceptionMessage(exceptionMessage, i, columnName + " 不能为空", headRowNum, exceptionLineCount);
                    }
                    //类型校验
                    typeValid(i, annotation, value, exceptionMessage, columnName, headRowNum, exceptionLineCount);
                    // 组合重复数据字段
                    if (annotation.isGroupUnique()) {
                        groupUniqueFlag = true;
                        groupString.append(value);
                        groupColumnNameString.append(columnName + " ");
                    }
                    // 重复数据字段
                    if (annotation.isUnique()) {
                        if (!oneMap.containsValue(value)) {
                            oneMap.put(i, value);
                        } else {
                            //原来的行号
                            Object key = oneMap.getKey(value);
                            if (exceptionMessage.size() >= exceptionLineCount) {
                                throw new CustomizeException(JSONObject.toJSON(exceptionMessage).toString());
                            }
                            exceptionMessage.add(" 第 " + ((int) key + headRowNum) + " 行" + "与第 " + (i + headRowNum) + " 行：" + columnName + "列 数据是重复的");
                        }
                    }
                    //长度校验
                    if (annotation.maxLen() > 0 && ObjectUtil.isNotNull(value) && ((CharSequence) value).length() > annotation.maxLen()) {
                        addExceptionMessage(exceptionMessage, i, columnName + " 的长度不得超过" + annotation.maxLen() + "个字", headRowNum, exceptionLineCount);
                    }
                    // 枚举校验
                    if (StrUtil.isNotBlank(annotation.dict()) && StrUtil.isNotEmpty((CharSequence) value)) {
                        String dict = annotation.dict();
                        // 将字典项转为map
                        Map<String, String> parse = (Map<String, String>) JSONObject.parse(dict);
                        String mapKey = getMapKey((String) value, parse);
                        if (StrUtil.isBlank(mapKey)) {
                            // 没取到值，则报异常
                            addExceptionMessage(exceptionMessage, i, columnName + " 的字典项填写不正确", headRowNum, exceptionLineCount);
                        } else {
                            // 导入的枚举实际值赋给列表
                            field.set(row, mapKey);
                        }
                    }
                }
            }
            //重复数据组合校验
            if (groupUniqueFlag) {
                if (i == 0 || (!groupMap.containsValue(groupString.toString()))) {
                    groupMap.put(i, groupString.toString());
                } else {
                    //原来的行号
                    Object key = groupMap.getKey(groupString.toString());
                    if (exceptionMessage.size() >= exceptionLineCount) {
                        throw new CustomizeException(JSONObject.toJSON(exceptionMessage).toString());
                    }
                    exceptionMessage.add(" 第 " + ((int) key + headRowNum) + " 行" + "与第 " + (i + headRowNum) + " 行：" + groupColumnNameString.toString() + " 的数据组合 是重复的");
                }
            }
        }
        return exceptionMessage;
    }

    /**
     * 类型校验
     *
     * @param i
     * @param annotation
     * @param value
     * @param exceptionMessage
     * @param columnName
     * @return void
     * @author SuiHaiyang
     * @date 2023/4/17 14:40
     */
    public void typeValid(int i, ValidateRule annotation, Object value, List<String> exceptionMessage, String columnName, int headRowNum, int exceptionLineCount) {
        //类型校验
        if (annotation.type() == ColumnDataTypeEnum.INTEGER) {
            if (ObjectUtil.isNotNull(value) && !RegexUtils.isInteger((String) value)) {
                addExceptionMessage(exceptionMessage, i, columnName + "的值应为整数类型", headRowNum, exceptionLineCount);
            }
        }
        // DOUBLE 类型校验
        if (annotation.type() == ColumnDataTypeEnum.DOUBLE) {
            if (ObjectUtil.isNotNull(value) && !RegexUtils.isDouble((String) value)) {
                addExceptionMessage(exceptionMessage, i, columnName + "的值应为小数类型", headRowNum, exceptionLineCount);
            }
        }
        // 日期类型校验
        if (annotation.type() == ColumnDataTypeEnum.DATE) {
            //yyyy-MM-dd HH:mm:ss 格式
            if (ColumnDateFormatEnum.YYYY_MM_DD_HH_MM_SS == annotation.dateFormat()) {
                if (ObjectUtil.isNotNull(value) && !RegexUtils.isDateTime((String) value)) {
                    addExceptionMessage(exceptionMessage, i, columnName + "的值应为" + ColumnDateFormatEnum.YYYY_MM_DD_HH_MM_SS.getFormatString() + "格式", headRowNum, exceptionLineCount);
                }
            }
            //yyyy-mm-dd 格式
            if (ColumnDateFormatEnum.YYYY_MM_DD_1 == annotation.dateFormat()) {
                if (ObjectUtil.isNotNull(value) && !RegexUtils.isDate1((String) value)) {
                    addExceptionMessage(exceptionMessage, i, columnName + "的值应为" + ColumnDateFormatEnum.YYYY_MM_DD_1.getFormatString() + "格式", headRowNum, exceptionLineCount);
                }
            }
        }
    }

    /**
     * 添加异常数据
     *
     * @param exceptionMessage
     * @param num
     * @param message
     * @return void
     * @author SuiHaiyang
     * @date 2023/4/14 10:29
     */
    private void addExceptionMessage(List<String> exceptionMessage, int num, String message, int headRowNum, int exceptionLineCount) {
        exceptionMessage.add("第 " + (num + headRowNum) + " 行：" + message);
        if (exceptionMessage.size() >= exceptionLineCount) {
            throw new CustomizeException(JSONObject.toJSON(exceptionMessage).toString());
        }
    }

    /**
     * 通过传入的 value 查询出Map 的键
     *
     * @param value
     * @param map
     * @return java.lang.String
     * @author SuiHaiyang
     * @date 2023/4/21 14:41
     */
    private String getMapKey(String value, Map<String, String> map) {
        String returnKey = "";
        if (ObjectUtil.isEmpty(map)) {
            return returnKey;
        }

        for (Map.Entry<String, String> m : map.entrySet()) {
            if (m.getValue().equals(value)) {
                returnKey = m.getKey();
            }
        }
        return returnKey;
    }
}
