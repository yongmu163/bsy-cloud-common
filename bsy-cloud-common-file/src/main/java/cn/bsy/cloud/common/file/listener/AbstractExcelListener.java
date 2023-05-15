package cn.bsy.cloud.common.file.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * ExcelListener
 * @desc
 * @author: wh
 * @date: 2022/11/22
 */
public abstract class AbstractExcelListener<T> extends AnalysisEventListener<T> {
    /**
     * 每隔5条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 500;
    private List<T> list = new ArrayList<>();

    @Override
    public void invoke(T data, AnalysisContext context) {
        list.add(data);
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (list.size() >= BATCH_COUNT) {
            saveData();
            // 存储完成清理 list
            list.clear();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        saveData();
    }

    /**
     * 公用保存逻辑
     */
    private void saveData () {
        customSaveData(list);
    }

    /**
     * 各业务逻辑实现
     *
     * @param list 数据集
     */
    public abstract void customSaveData(List<T> list);
}
