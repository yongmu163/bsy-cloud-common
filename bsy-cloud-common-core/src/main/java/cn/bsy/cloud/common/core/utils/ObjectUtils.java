package cn.bsy.cloud.common.core.utils;


import cn.bsy.cloud.common.core.exception.CustomizeException;
import cn.hutool.core.util.ObjectUtil;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 对象转换工具类
 *
 * @ClassName:ObjectUtils
 * @Date:2022年6月22日 下午10:51:39
 * @Author:gaoheng
 */

@Slf4j
@UtilityClass
public class ObjectUtils {
    /**
     * 单个对象克隆，结果中null由""(空字符串)代替
     *
     * @param poObj
     * @param voClass
     * @param <T>
     * @return
     * @throws Exception
     */
    public <T> T cloneObj(Object poObj, final Class<T> voClass) {
        return setValuesToSpacecCharacter(cloneObject(poObj, voClass));
    }

    /**
     * 克隆对象列表
     *
     * @param poList
     * @param voClass
     * @param <T>
     * @return
     * @throws Exception
     */
    public <T> List<T> cloneObjList(List<? extends Object> poList, final Class<T> voClass) {
        List<T> voList = new ArrayList<T>();
        T voObj = null;
        for (Object poObj : poList) {
            voObj = cloneObject(poObj, voClass);
            voList.add(voObj);
        }
        return voList;
    }

    /**
     * 将对象转为map
     *
     * @param map
     * @param obj
     */
    @SuppressWarnings("unchecked")
    public Map copyBeanToMap(@SuppressWarnings("rawtypes") Map map, Object obj) {
        PropertyDescriptor[] pds = PropertyUtils.getPropertyDescriptors(obj);
        for (int i = 0; i < pds.length; i++) {
            PropertyDescriptor pd = pds[i];
            String propname = pd.getName();
            try {
                Object propvalue = PropertyUtils.getSimpleProperty(obj, propname);
                map.put(propname, propvalue);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        return map;
    }

    /**
     * 将map转为对象
     *
     * @param map
     * @param t
     * @param <T>
     * @return
     */
    public <T> T mapToBean(Map<String, Object> map, T t) {
        try {
            org.apache.commons.beanutils.BeanUtils.populate(t, map);
        } catch (Exception e) {
            throw new CustomizeException("Map转Bean失败");
        }
        return t;
    }

    /**
     * 获取bean实例小写名称
     */
    public <T> String getLowerNameFromClass(final Class<T> beanClass) {
        return beanClass.getSimpleName().toLowerCase();
    }

    /**
     * 将对象中为null的字符串属性变为""
     *
     * @param source
     * @param <T>
     * @return
     */
    public <T> T setValuesToSpacecCharacter(T source) {
        try {
            Field[] fields = source.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (("class java.lang.String").equals(field.getGenericType().toString())) {
                    field.setAccessible(true);
                    Object obj = field.get(source);
                    if (ObjectUtil.isNull(obj)) {
                        field.set(source, "");
                    }
                }
            }
            return source;
        } catch (Exception e) {
            throw new CustomizeException("转换对象属性值错误");
        }
    }

    /**
     * 克隆单个对象
     *
     * @param poObj
     * @param voClass
     * @param <T>
     * @return
     * @throws Exception
     */
    public <T> T cloneObject(Object poObj, final Class<T> voClass){
        if (poObj == null) {
            return null;
        }
        T voObj = null;
        try {
            voObj = voClass.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(poObj, voObj);
            // 自定义时间戳转换
            PropertyDescriptor[] targetPds = BeanUtils.getPropertyDescriptors(voClass);
            for (PropertyDescriptor targetPd : targetPds) {
                if (targetPd.getWriteMethod() == null || !StringUtils.endsWithAny(targetPd.getName(), "Time", "Date")) {
                    continue;
                }
                // LocalDateTime -> Long;
                if (targetPd.getReadMethod().getReturnType() == Long.class) {
                    PropertyDescriptor sourcePd = BeanUtils.getPropertyDescriptor(poObj.getClass(), targetPd.getName());
                    if (sourcePd != null) {
                        if (sourcePd.getReadMethod().getReturnType() == LocalDateTime.class) {
                            Object value = sourcePd.getReadMethod().invoke(poObj);
                            if (value != null) {
                                targetPd.getWriteMethod().invoke(voObj, DateUtils.getMilliByTime((LocalDateTime) value));
                            }
                        }
                        if (sourcePd.getReadMethod().getReturnType() == LocalDate.class) {
                            Object value = sourcePd.getReadMethod().invoke(poObj);
                            if (value != null) {
                                targetPd.getWriteMethod().invoke(voObj, DateUtils.getMilliByTime((LocalDate) value));
                            }
                        }
                    }
                }
            }
            return voObj;
        } catch (Exception e) {
            throw new CustomizeException("克隆对象后端服务报错");
        }
    }
}
