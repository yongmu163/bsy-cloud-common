package cn.bsy.cloud.common.file.service.impl;


import cn.bsy.cloud.common.file.model.BookMark;
import cn.bsy.cloud.common.file.model.BookMarkNode;
import cn.bsy.cloud.common.file.model.BookMarkTableNode;
import cn.bsy.cloud.common.file.model.BookMarks;
import cn.bsy.cloud.common.file.service.IGenerateWordService;
import cn.bsy.cloud.common.file.utils.MinIoUtil;
import cn.bsy.cloud.common.mvc.utils.WebUtils;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHeight;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrPr;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.w3c.dom.Node;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 使用POI,进行Word相关的操作接口实现类
 *
 * @author gaoh
 * @date 2022-10-21 16:57:25
 */
@Slf4j
@Service("generateWordService")
public class GenerateWordServiceImpl implements IGenerateWordService {
    /**
     * 内部使用的文档对象
     **/
    private XWPFDocument document;
    private BookMarks bookMarks = null;
    /**
     * minIO操作服务
     */
    @Resource
    private  MinIoUtil minIoUtil;

    @Override
    public void setTemplate(String templatePath) throws IOException {
        try {
            this.document = new XWPFDocument(POIXMLDocument.openPackage(templatePath));
            bookMarks = new BookMarks(document);
        } catch (IOException e) {
            log.error(e.toString());
            throw e;
        }
    }

    @Override
    public void setTemplate(InputStream is) throws IOException {
        try {
            this.document = new XWPFDocument(OPCPackage.open(is));
            bookMarks = new BookMarks(document);
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void replaceBookMark(List<BookMarkNode> bookMarkNodeList) {
        for (BookMarkNode bookMarkNode : bookMarkNodeList) {
            if (CollUtil.isEmpty(bookMarkNode.getBookMarkTableNodeList())) {
                Map<String, String> map = CollUtil.newHashMap();
                map.put(bookMarkNode.getBookmarkName(), bookMarkNode.getBookmarkValue());
                this.replaceBookMark(map);
            } else {
                this.fillTable(bookMarkNode.getBookmarkName(), bookMarkNode.getBookMarkTableNodeList());
            }
        }
    }

    @Override
    public void replaceBookMark(Map<String, String> indicator) {
        //循环进行替换
        Iterator<String> bookMarkIter = bookMarks.getNameIterator();
        while (bookMarkIter.hasNext()) {
            String bookMarkName = bookMarkIter.next();
            //得到标签名称
            BookMark bookMark = bookMarks.getBookmark(bookMarkName);
            //进行替换
            if (indicator.get(bookMarkName) != null) {
                bookMark.insertTextAtBookMark(indicator.get(bookMarkName), BookMark.INSERT_BEFORE);
            }
        }
    }

    @Override
    public void fillTable(String bookmarkName, List<BookMarkTableNode> bookMarkTableNodeList) {
        if (StrUtil.isBlank(bookmarkName) || CollUtil.isEmpty(bookMarkTableNodeList)) {
            return;
        }
        for (BookMarkTableNode bookMarkTableNode : bookMarkTableNodeList) {
            if (ObjectUtil.isNull(bookMarkTableNode) || StrUtil.isBlank(bookMarkTableNode.getRowId())) {
                return;
            }
        }
        Map<String, List<BookMarkTableNode>> map = groupingArrayListOne(bookMarkTableNodeList);
        List<Map<String, String>> content = CollUtil.newArrayList();
        for (String s : map.keySet()) {
            List<BookMarkTableNode> rowBookMarkTableNodeList = map.get(s);
            Map<String, String> mapTemp = CollUtil.newHashMap();
            for (BookMarkTableNode bookMarkTableNode : rowBookMarkTableNodeList) {
                mapTemp.put(bookMarkTableNode.getTableColumnName(), bookMarkTableNode.getTableColumnValue());
            }
            content.add(mapTemp);
        }
        this.fillTableAtBookMark(bookmarkName, content);
    }

    @Override
    public void fillTableAtBookMark(String bookMarkName, List<Map<String, String>> content) {
        /* rowNum来比较标签在表格的哪一行*/
        int rowNum = 0;
        /*首先得到标签*/
        BookMark bookMark = bookMarks.getBookmark(bookMarkName);
        Map<String, String> columnMap = CollUtil.newHashMap();
        Map<String, Node> styleNode = CollUtil.newHashMap();
        /*标签是否处于表格内*/
        if (bookMark.isInTable()) {
            /*获得标签对应的Table对象和Row对象*/
            XWPFTable table = bookMark.getContainerTable();
            XWPFTableRow row = bookMark.getContainerTableRow();
            List<XWPFTableCell> rowCell = row.getTableCells();
            for (int i = 0; i < rowCell.size(); i++) {
                columnMap.put(i + "", rowCell.get(i).getText().trim());
                /*获取该单元格段落的xml，得到根节点*/
                Node node1 = rowCell.get(i).getParagraphs().get(0).getCTP().getDomNode();
                /*遍历根节点的所有子节点*/
                for (int x = 0; x < node1.getChildNodes().getLength(); x++) {
                    if (node1.getChildNodes().item(x).getNodeName().equals(BookMark.RUN_NODE_NAME)) {
                        Node node2 = node1.getChildNodes().item(x);
                        /*遍历所有节点为"w:r"的所有自己点，找到节点名为"w:rPr"的节点*/
                        for (int y = 0; y < node2.getChildNodes().getLength(); y++) {
                            if (node2.getChildNodes().item(y).getNodeName().endsWith(BookMark.STYLE_NODE_NAME)) {
                                /*将节点为"w:rPr"的节点(字体格式)存到HashMap中*/
                                styleNode.put(i + "", node2.getChildNodes().item(y));
                            }
                        }
                    } else {
                        continue;
                    }
                }
            }
            /*循环对比，找到该行所处的位置，删除改行	*/
            for (int i = 0; i < table.getNumberOfRows(); i++) {
                if (table.getRow(i).equals(row)) {
                    rowNum = i;
                    break;
                }
            }
            table.removeRow(rowNum);
            for (int i = 0; i < content.size(); i++) {
				/*创建新的一行,单元格数是表的第一行的单元格数,
				后面添加数据时，要判断单元格数是否一致*/
                XWPFTableRow tableRow = table.createRow();
                CTTrPr trPr = tableRow.getCtRow().addNewTrPr();
                CTHeight ht = trPr.addNewTrHeight();
                ht.setVal(BigInteger.valueOf(360));
            }
            /*得到表格行数*/
            int rcount = table.getNumberOfRows();
            for (int i = rowNum; i < rcount; i++) {
                XWPFTableRow newRow = table.getRow(i);
                /*判断newRow的单元格数是不是该书签所在行的单元格数*/
                if (newRow.getTableCells().size() != rowCell.size()) {
					/*计算newRow和书签所在行单元格数差的绝对值
					如果newRow的单元格数多于书签所在行的单元格数，不能通过此方法来处理，可以通过表格中文本的替换来完成
					如果newRow的单元格数少于书签所在行的单元格数，要将少的单元格补上*/
                    int sub = Math.abs(newRow.getTableCells().size() - rowCell.size());
                    //将缺少的单元格补上
                    for (int j = 0; j < sub; j++) {
                        newRow.addNewTableCell();
                    }
                }
                List<XWPFTableCell> cells = newRow.getTableCells();
                for (int j = 0; j < cells.size(); j++) {
                    XWPFParagraph para = cells.get(j).getParagraphs().get(0);
                    XWPFRun run = para.createRun();
                    if (content.get(i - rowNum).get(columnMap.get(j + "")) != null) {
                        //改变单元格的值，标题栏不用改变单元格的值
                        run.setText(content.get(i - rowNum).get(columnMap.get(j + "")) + "");
                        //将单元格段落的字体格式设为原来单元格的字体格式
                        run.getCTR().getDomNode().insertBefore(styleNode.get(j + "").cloneNode(true), run.getCTR().getDomNode().getFirstChild());
                    }
                }
            }
        }
    }

    @Override
    public void replaceText(Map<String, String> bookmarkMap, String bookMarkName) {
        //首先得到标签
        BookMark bookMark = bookMarks.getBookmark(bookMarkName);
        //获得书签标记的表格
        XWPFTable table = bookMark.getContainerTable();
        //获得所有的表
        if (table != null) {
            //得到该表的所有行
            int rcount = table.getNumberOfRows();
            for (int i = 0; i < rcount; i++) {
                XWPFTableRow row = table.getRow(i);
                //获到改行的所有单元格
                List<XWPFTableCell> cells = row.getTableCells();
                for (XWPFTableCell c : cells) {
                    for (Entry<String, String> e : bookmarkMap.entrySet()) {
                        if (c.getText().equals(e.getKey())) {
                            /*删掉单元格内容*/
                            c.removeParagraph(0);
                            /*给单元格赋值*/
                            c.setText(e.getValue());
                        }
                    }
                }
            }
        }
    }

    @Override
    public void outPutFileObj(String fileName) throws Exception {
        HttpServletResponse httpServletResponse = WebUtils.getResponse();
        fileName = URLEncoder.encode(fileName, CharsetUtil.UTF_8);
        httpServletResponse.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        ServletOutputStream servletOutputStream = null;
        try {
            servletOutputStream = httpServletResponse.getOutputStream();
            this.document.write(servletOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void downloadBookMarkWordFile(String bucketName,
                                         String fileName,
                                         List<BookMarkNode> bookMarkNodeList) {
        this.downloadBookMarkWordFile(bucketName, fileName, fileName, bookMarkNodeList);
    }

    @Override
    public void downloadBookMarkWordFile(String bucketName, String fileName,
                                         String realFileName, List<BookMarkNode> bookMarkNodeList) {
        realFileName = StrUtil.isBlank(realFileName) ? fileName : realFileName;
        try {
            InputStream inputStream = minIoUtil.getObject(bucketName, fileName);
            this.setTemplate(inputStream);
            this.replaceBookMark(bookMarkNodeList);
            String objectName = realFileName.substring(realFileName.indexOf("/") + 1, realFileName.length());
            this.outPutFileObj(objectName);
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void downloadBookMarkWordFileBy(String filePath, String fileName, List<BookMarkNode> bookMarkNodeList){
        try {
            ClassPathResource classPathResource = new ClassPathResource(filePath);
            this.downloadBookMarkWordFile(fileName, classPathResource.getInputStream(), bookMarkNodeList);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void downloadBookMarkWordFile(String fileName, InputStream template,
                                         List<BookMarkNode> bookMarkNodeList) {
        try {
            this.setTemplate(template);
            this.replaceBookMark(bookMarkNodeList);
            String objectName = fileName.substring(fileName.indexOf("/") + 1, fileName.length());
            this.outPutFileObj(objectName);
            template.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 对列表中书签对象进行分组
     * @param list
     * @return
     */
    private  Map<String, List<BookMarkTableNode>> groupingArrayListOne(List<BookMarkTableNode> list) {
        Map<String, List<BookMarkTableNode>> map = CollUtil.newHashMap();
        //分组
        for (BookMarkTableNode l : list) {
            //判断是否存在该key
            if (map.containsKey(l.getRowId())) {
                //存在就获取该key的value然后add
                map.get(l.getRowId()).add(l);
            } else {
                List<BookMarkTableNode> lt = new ArrayList<>();
                lt.add(l);
                //不存在就put
                map.put(l.getRowId(), lt);
            }
        }
        return map;
    }
}
