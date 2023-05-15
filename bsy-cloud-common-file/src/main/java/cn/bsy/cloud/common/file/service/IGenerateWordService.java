package cn.bsy.cloud.common.file.service;

import cn.bsy.cloud.common.file.model.BookMarkNode;
import cn.bsy.cloud.common.file.model.BookMarkTableNode;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * 使用POI,进行Word相关的操作
 * @author gaoh
 * @date 2022-10-21 16:57:25
 */
public interface IGenerateWordService {
    /**
     * 为文档设置模板
     *
     * @param templatePath
     * @throws IOException
     */
    void setTemplate(String templatePath) throws IOException;

    /**
     * 为文档设置模板
     *
     * @param is
     * @throws IOException
     */
    void setTemplate(InputStream is) throws IOException;

    /**
     * 替换文档中普通标签和表格标签内容
     * @param bookMarkNodeList
     */
    void replaceBookMark(List<BookMarkNode> bookMarkNodeList);

    /**
     * 进行标签替换的例子,传入的Map中，key表示标签名称，value是替换的信息
     *
     * @param indicator
     */
    void replaceBookMark(Map<String, String> indicator);


    /**
     * 填充模板中表格书签
     * @param bookmarkName
     * @param bookMarkTableNodeList
     */
    void fillTable(String bookmarkName,List<BookMarkTableNode> bookMarkTableNodeList);

    /**
     * 填充表格中的标签
     * @param bookMarkName
     * @param content
     */
    void fillTableAtBookMark(String bookMarkName, List<Map<String, String>> content);

    /**
     * 替换标签内容
     * @param bookmarkMap
     * @param bookMarkName
     */
    void replaceText(Map<String, String> bookmarkMap, String bookMarkName);

    /**
     * 返回文件流
     * @param fileName
     * @throws Exception
     */
    void outPutFileObj(String fileName) throws Exception;

    /**
     * 下载带有书签值的word文档
     * @param bucketName
     * @param fileName
     * @param bookMarkNodeList
     */
    void downloadBookMarkWordFile(String bucketName,String fileName,List<BookMarkNode> bookMarkNodeList);

    /**
     * 下载带有书签值的word文档
     * @param bucketName
     * @param fileName
     * @param realFileName 下载文件展示名称。若为空，使用fileName
     * @param bookMarkNodeList
     */
    void downloadBookMarkWordFile(String bucketName,String fileName,String realFileName, List<BookMarkNode> bookMarkNodeList);

    /**
     * 下载带有书签值的word文档 resource目录下文件
     * @param filePath resource下相对路径 例如：/static/aaa.docx
     * @param fileName 文件名 带后缀
     * @param bookMarkNodeList
     */
    void downloadBookMarkWordFileBy(String filePath, String fileName, List<BookMarkNode> bookMarkNodeList);

    /**
     * 下载带有书签值的word文档
     * @param fileName 文件名 带后缀
     * @param template 模板文件流
     * @param bookMarkNodeList
     */
    void downloadBookMarkWordFile(String fileName, InputStream template, List<BookMarkNode> bookMarkNodeList);

}
