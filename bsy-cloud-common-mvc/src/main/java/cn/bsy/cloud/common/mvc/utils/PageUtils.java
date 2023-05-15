package cn.bsy.cloud.common.mvc.utils;

import cn.bsy.cloud.common.core.utils.ObjectUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description: 分页工具类
 * @Author gaoh
 * @Date 2022/6/20 0020
 **/
@Slf4j
@UtilityClass
public class PageUtils {
    /**
     * 复制分页对象
     * @param page
     * @param targetClazz
     * @param <T>
     * @return
     */
    public static <T> Page<T> copy(IPage page, Class<T> targetClazz) {
        Page pageResult = new Page(page.getCurrent(),page.getSize(),page.getTotal());
        pageResult.setPages(page.getPages());
        pageResult.setRecords(ObjectUtils.cloneObjList(page.getRecords(), targetClazz));
        return pageResult;
    }
}
