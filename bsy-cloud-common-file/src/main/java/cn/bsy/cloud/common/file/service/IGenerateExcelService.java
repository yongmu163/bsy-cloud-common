package cn.bsy.cloud.common.file.service;

import cn.bsy.cloud.common.file.listener.AbstractExcelListener;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @ClassName IGenerateExcelService
 * @Description excel工具服务接口
 * @Author wh
 * @Date 2022/11/22
 */
public interface IGenerateExcelService {

    /**
     * 按照模版导出Excel
     * @param targetFileName 目标文件名称 含后缀
     * @param template 导出模版
     * @param dataList 数据
     * @param <T>
     */
    <T> void download(String targetFileName, InputStream template, List<T> dataList);

    /**
     * 按照模版导出Excel
     * @param filePath 模板文件路径  resource文件夹下相对路径 例如：/static/aaa.xlsx
     * @param targetFileName 目标文件名称 含后缀
     * @param dataList 数据
     * @param <T>
     */
    <T> void download(String filePath, String targetFileName, List<T> dataList);

    /**
     * 按照模版导出Excel
     * @param targetFileName 目标文件名称  含后缀
     * @param template 导出模版
     * @param dataList 数据
     * @param response 网络流
     * @param <T>
     */
    <T> void download(String targetFileName, InputStream template, List<T> dataList, HttpServletResponse response);

    /**
     * 按照模版导出Excel
     * @param filePath 模板文件路径  resource文件夹下相对路径 例如：/static/aaa.xlsx
     * @param targetFileName 目标文件名称  含后缀
     * @param dataList 数据
     * @param response 网络流
     * @param <T>
     */
    <T> void download(String filePath, String targetFileName, List<T> dataList, HttpServletResponse response);

    /**
     * 按照模版导出多sheet页数据Excel
     * @param targetFileName 目标文件名称  含后缀
     * @param template 导出模版
     * @param dataList 数据集合，该参数传入顺序就是sheet页数据写入顺序。
     * @param <T>
     */
    <T> void download(String targetFileName, InputStream template, List<T> ...dataList);

    /**
     * 按照模版导出多sheet页数据Excel
     * @param targetFileName 目标文件名称  含后缀
     * @param template 导出模版
     * @param response 网络流
     * @param dataList 数据集合，该参数传入顺序就是sheet页数据写入顺序。
     * @param <T>
     */
    <T> void download(String targetFileName, InputStream template, HttpServletResponse response, List<T> ...dataList);

    /**
     * 读取Excel
     * @param inputStream 数据流
     * @param abstractExcelListener 数据处理监听器
     * @param <T>
     */
    <T> void read(InputStream inputStream, AbstractExcelListener<T> abstractExcelListener);

    /**
     * 读取Excel
     * @param inputStream 数据流
     * @param headRowNumber 指定从第几行开始读取数据
     * @param abstractExcelListener 数据处理监听器
     * @param <T>
     */
    <T> void read(InputStream inputStream, Integer headRowNumber, AbstractExcelListener<T> abstractExcelListener);

    /**
     * 读取Excel
     * @param file 文件
     * @param type 文件类型
     * @param abstractExcelListener 数据处理监听器
     * @param <T>
     */
    <T> void read(MultipartFile file, String type, AbstractExcelListener<T> abstractExcelListener);

    /**
     * 读取Excel
     * @param file 文件
     * @param type 文件类型
     * @param headRowNumber 指定从第几行开始读取数据
     * @param abstractExcelListener 数据处理监听器
     * @param <T>
     */
    <T> void read(MultipartFile file, String type, Integer headRowNumber, AbstractExcelListener<T> abstractExcelListener);

    /**
     * 读取Excel指定sheet页下标
     * @param inputStream 数据流
     * @param abstractExcelListener 数据处理监听器
     * @param sheetIndex sheet页下标，从0开始。
     * @param <T>
     */
    <T> void readAssignSheetIndex(InputStream inputStream, Integer sheetIndex,AbstractExcelListener<T> abstractExcelListener);

    /**
     * 按照占位符填充Excel模版
     * @param filePath 模板文件路径 resource目录下相对路径 例如：/static/aaa.xlsx
     * @param targetFileName 导出文件名称  含后缀
     * @param dataMap 数据
     */
    void fill(String filePath, String targetFileName, Map dataMap);

    /**
     * 按照占位符填充Excel模版
     * @param filePath 模板文件路径 resource目录下相对路径 例如：/static/aaa.xlsx
     * @param targetFileName 导出文件名称  含后缀
     * @param dataMap 数据
     * @param response 网络流
     */
    void fill(String filePath, String targetFileName, Map dataMap, HttpServletResponse response);

    /**
     * 按照占位符填充Excel模版
     * @param targetFileName 目标文件名称
     * @param template 导出模版
     * @param dataMap 数据
     */
    void fill(String targetFileName, InputStream template, Map dataMap);

    /**
     * 按照占位符填充Excel模版
     * @param targetFileName 目标文件名称
     * @param template 导出模版
     * @param dataMap 数据
     * @param response 网络流
     */
    void fill(String targetFileName, InputStream template, Map dataMap, HttpServletResponse response);
}
