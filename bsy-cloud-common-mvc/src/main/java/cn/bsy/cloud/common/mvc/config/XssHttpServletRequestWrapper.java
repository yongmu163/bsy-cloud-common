package cn.bsy.cloud.common.mvc.config;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HtmlUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author gaoh
 * @desc http请求抵御跨站脚本攻击装饰器
 * @date 2022年01月11日 下午 5:19
 */
@Slf4j
@SuppressWarnings("all")
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

    public XssHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getParameter(String name) {
        String value = super.getParameter(name);
        if (!StrUtil.hasEmpty(value)) {
            value = HtmlUtil.filter(value);
        }
        return value;
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if (values != null) {
            values = transferreArray(values);
        }
        return values;
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> parameters = super.getParameterMap();
        LinkedHashMap<String, String[]> map = new LinkedHashMap();
        if (parameters != null) {
            for (String key : parameters.keySet()) {
                String[] values = transferreArray(parameters.get(key));
                map.put(key, values);
            }
        }
        return map;
    }

    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        if (!StrUtil.hasEmpty(value)) {
            value = HtmlUtil.filter(value);
        }
        return value;
    }

    /**
     * 将请求的字符串数组信息进行转义操作
     *
     * @param values
     * @return
     */
    private String[] transferreArray(String[] values) {
        List<String> newList = CollUtil.newArrayList(values)
                .stream().map(item -> {
                    if (!StrUtil.hasEmpty(item)) {
                        item = HtmlUtil.filter(item);
                    }
                    return item;
                }).collect(Collectors.toList());
        return newList.toArray(new String[newList.size()]);
    }
}