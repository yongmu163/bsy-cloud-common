package cn.bsy.cloud.common.file.service.impl;
import cn.bsy.cloud.common.core.exception.CustomizeException;
import cn.bsy.cloud.common.file.listener.AbstractExcelListener;
import cn.bsy.cloud.common.file.service.IGenerateExcelService;
import cn.bsy.cloud.common.mvc.utils.WebUtils;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * @ClassName GenerateExcelServiceImpl
 * @Description excel服务接口实现类
 * @Author wh
 * @Date 2022/11/22
 */
@Slf4j
public class GenerateExcelServiceImpl implements IGenerateExcelService {

    @Override
    public <T> void download(String targetFileName, InputStream template, List<T> dataList) {
        HttpServletResponse response = WebUtils.getResponse();
        this.download(targetFileName, template, dataList, response);
    }

    @Override
    public <T> void download(String filePath, String targetFileName, List<T> dataList) {

        try {
            ClassPathResource classPathResource = new ClassPathResource(filePath);
            this.download(targetFileName, classPathResource.getInputStream(), dataList);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public <T> void download(String targetFileName, InputStream template, List<T> dataList, HttpServletResponse response) {
        try {
            //设置下载文件名
            this.getResponse(targetFileName, response);
            Class targetClass = dataList == null || dataList.isEmpty() ? Object.class : Class.forName(dataList.get(0).getClass().getName());

            EasyExcel.write(response.getOutputStream(), targetClass)
                    .withTemplate(template)
                    .needHead(false)
                    .sheet()
                    .registerWriteHandler(this.getHorizontalAlignmentCenterCellStyle())
                    .doWrite(dataList);
        } catch (ClassNotFoundException | IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public <T> void download(String filePath, String targetFileName, List<T> dataList, HttpServletResponse response) {
        try {
            ClassPathResource classPathResource = new ClassPathResource(filePath);
            this.download(targetFileName, classPathResource.getInputStream(), dataList, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public <T> void download(String targetFileName, InputStream template, List<T>... dataList) {
        HttpServletResponse response = WebUtils.getResponse();
        this.download(targetFileName, template, response, dataList);
    }

    @Override
    public <T> void download(String targetFileName, InputStream template,HttpServletResponse response, List<T> ...dataList) {
        ExcelWriter excelWriter = null;
        try {
            //设置下载文件名
            this.getResponse(targetFileName, response);
            //新建ExcelWriter
            excelWriter = EasyExcel.write(response.getOutputStream()).withTemplate(template).build();
            for (int i = 0; i < dataList.length; i++) {
                Class targetClass = dataList[i] == null || dataList[i].isEmpty() ? Object.class : Class.forName(dataList[i].get(0).getClass().getName());
                // 获取sheet对象
                WriteSheet mainSheet = EasyExcel.writerSheet(i).head(targetClass).needHead(false).build();
                // 向sheet写入数据
                excelWriter.write(dataList[i], mainSheet);
            }
            //关闭流
            excelWriter.finish();
        } catch (ClassNotFoundException | IOException e){
            log.error(e.getMessage(), e);
        } finally {
            //关闭流
            excelWriter.finish();
        }
    }

    @Override
    public <T> void read(InputStream inputStream, AbstractExcelListener<T> abstractExcelListener) {
        Type type = ((ParameterizedType) abstractExcelListener.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        try {
            EasyExcel.read(inputStream, Class.forName(type.getTypeName()), abstractExcelListener)
                    .sheet().autoTrim(true).doRead();
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public <T> void read(InputStream inputStream, Integer headRowNumber, AbstractExcelListener<T> abstractExcelListener) {
        Type type = ((ParameterizedType) abstractExcelListener.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        try {
            EasyExcel.read(inputStream, Class.forName(type.getTypeName()), abstractExcelListener)
                    .headRowNumber(headRowNumber).sheet().autoTrim(true).doRead();
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public <T> void read(MultipartFile file, String type, AbstractExcelListener<T> abstractExcelListener) {
        try {
            if (ObjectUtil.isNull(file)) {
                throw new CustomizeException("文件不存在");
            }
            if (!isExcelFile(file, type)) {
                throw new CustomizeException("请上传" + type + "格式文件");
            }
            read(file.getInputStream(), abstractExcelListener);
        }catch (Exception e){
            throw new CustomizeException(e.getMessage());
        }
    }

    @Override
    public <T> void read(MultipartFile file, String type, Integer headRowNumber, AbstractExcelListener<T> abstractExcelListener) {
        try {
            if (ObjectUtil.isNull(file)) {
                throw new CustomizeException("文件不存在");
            }
            if (!isExcelFile(file, type)) {
                throw new CustomizeException("请上传" + type + "格式文件");
            }
            read(file.getInputStream(), headRowNumber, abstractExcelListener);
        }catch (Exception e){
            throw new CustomizeException(e.getMessage());
        }
    }

    @Override
    public <T> void readAssignSheetIndex(InputStream inputStream, Integer sheetIndex,AbstractExcelListener<T> abstractExcelListener) {
        Type type = ((ParameterizedType) abstractExcelListener.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        try {
            EasyExcel.read(inputStream, Class.forName(type.getTypeName()), abstractExcelListener).sheet(sheetIndex).autoTrim(true).doRead();
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void fill(String filePath, String targetFileName, Map dataMap) {
        HttpServletResponse response = WebUtils.getResponse();
        this.fill(filePath, targetFileName, dataMap, response);
    }

    @Override
    public void fill(String filePath, String targetFileName, Map dataMap, HttpServletResponse response) {
        try {
            ClassPathResource classPathResource = new ClassPathResource(filePath);
            this.fill(targetFileName, classPathResource.getInputStream(), dataMap, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void fill(String targetFileName, InputStream template, Map dataMap) {
        HttpServletResponse response = WebUtils.getResponse();
        this.fill(targetFileName, template, dataMap, response);
    }

    @Override
    public void fill(String targetFileName, InputStream template, Map dataMap, HttpServletResponse response) {
        ExcelWriter excelWriter = null;
        try {
            //设置下载文件重命名
            this.getResponse(targetFileName, response);
            //创建
            excelWriter = EasyExcel.write(response.getOutputStream())
                    .withTemplate(template).build();
            //创建sheet
            WriteSheet writeSheet = EasyExcel.writerSheet().build();
            //数据填充
            excelWriter.fill(dataMap, writeSheet);
            //关闭流
            excelWriter.finish();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            //关闭流
            excelWriter.finish();
        }
    }

    /**
     * 判断是否是Excel文件
     *
     * @param excelFile
     * @param type
     * @return
     */
    private boolean isExcelFile(MultipartFile excelFile, String type) {
        boolean result = false;
        if (excelFile.getOriginalFilename().endsWith(type)) {
            result = true;
        }
        return result;
    }

    /**
     * 设置response
     * @param targetFileName 下载文件重命名
     * @param response
     * @return
     */
    private HttpServletResponse getResponse(String targetFileName, HttpServletResponse response) throws UnsupportedEncodingException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码
        String fileName = URLEncoder.encode(targetFileName, "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        return response;
    }

    /**
     * 设置单元格边框及自动垂直居中换行
     * @return
     */
    private HorizontalCellStyleStrategy getHorizontalAlignmentCenterCellStyle() {
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        contentWriteCellStyle.setBorderLeft(BorderStyle.THIN);
        contentWriteCellStyle.setBorderTop(BorderStyle.THIN);
        contentWriteCellStyle.setBorderRight(BorderStyle.THIN);
        contentWriteCellStyle.setBorderBottom(BorderStyle.THIN);
        contentWriteCellStyle.setWrapped(true);
        // 水平居中
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        // 垂直居中
        contentWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        return new HorizontalCellStyleStrategy(contentWriteCellStyle, contentWriteCellStyle);
    }
}
