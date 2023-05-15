package cn.bsy.cloud.common.file.model;

import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlException;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBookmark;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.List;
import java.util.Stack;

/**
 * @Description: Word 文件中标签的封装类，保存了其定义和内部的操作
 * @Author gaoh
 * @Date 2022年10月23日 下午 6:29
 **/
public class BookMark {
    /**
     * 替换标签时，设于标签的后面
     **/
    public static final int INSERT_AFTER = 0;

    /**
     * 替换标签时，设于标签的前面
     **/
    public static final int INSERT_BEFORE = 1;

    /**
     * 替换标签时，将内容替换书签
     **/
    public static final int REPLACE = 2;

    /**
     * docx中定义的部分常量引用
     **/
    public static final String RUN_NODE_NAME = "w:r";
    public static final String TEXT_NODE_NAME = "w:t";
    public static final String BOOKMARK_START_TAG = "bookmarkStart";
    public static final String BOOKMARK_END_TAG = "bookmarkEnd";
    public static final String BOOKMARK_ID_ATTR_NAME = "w:id";
    public static final String STYLE_NODE_NAME = "w:rPr";
    public static final String RETURN_CODE = "\r";

    /**
     * 内部的标签定义类
     **/
    private CTBookmark ctBookmark = null;

    /**
     * 标签所处的段落
     **/
    private XWPFParagraph para = null;

    /**
     * 标签所在的表cell对象
     **/
    private XWPFTableCell tableCell = null;

    /**
     * 标签名称
     **/
    private String bookmarkName = null;

    /**
     * 该标签是否处于表格内
     **/
    private boolean isCell = false;


    public BookMark(CTBookmark ctBookmark, XWPFParagraph para) {
        this.ctBookmark = ctBookmark;
        this.para = para;
        this.bookmarkName = ctBookmark.getName();
        this.tableCell = null;
        this.isCell = false;
    }


    public BookMark(CTBookmark ctBookmark, XWPFParagraph para, XWPFTableCell tableCell) {
        this(ctBookmark, para);
        this.tableCell = tableCell;
        this.isCell = true;
    }

    public boolean isInTable() {
        return this.isCell;
    }

    public XWPFTable getContainerTable() {
        return this.tableCell.getTableRow().getTable();
    }

    public XWPFTableRow getContainerTableRow() {
        return this.tableCell.getTableRow();
    }

    public String getBookmarkName() {
        return this.bookmarkName;
    }

    /**
     * 固定位置插入标签
     *
     * @param bookmarkValue
     * @param where
     */
    public void insertTextAtBookMark(String bookmarkValue, int where) {
        /* 根据标签的类型，进行不同的操作 */
        XWPFRun run = this.para.createRun();
        bookmarkValue = bookmarkValue.replaceAll("\\p{C}", RETURN_CODE);
        if (((String) bookmarkValue).indexOf(RETURN_CODE) > 0) {
            //设置换行
            String[] text = bookmarkValue.toString().split("\r");
            this.para.removeRun(0);
            run = this.para.insertNewRun(0);
            for (int f = 0; f < text.length; f++) {
                if (f == 0) {
                    //此处不缩进因为word模板已经缩进了。
                    run.setText(text[f].trim());
                } else {
                    run.addCarriageReturn();//硬回车
                    run.addTab();
                    //注意：wps换行首行缩进是三个空格符，office要的话可以用 run.addTab();缩进或者四个空格符
                    run.setText(text[f].trim());
                }
            }
        } else {
            run.setText(bookmarkValue);
        }
        switch (where) {
            case BookMark.INSERT_AFTER:
                this.insertAfterBookmark(run);
                break;
            case BookMark.INSERT_BEFORE:
                this.insertBeforeBookmark(run);
                break;
            case BookMark.REPLACE:
                this.replaceBookmark(run);
                break;
            default:
        }
    }

    /**
     * 替换标签时，设于标签的后面
     *
     * @param run
     */
    private void insertAfterBookmark(XWPFRun run) {
        Node nextNode = null;
        Node insertBeforeNode = null;
        Node styleNode = null;
        int bookmarkStartId = 0;
        int bookmarkEndId = -1;
        bookmarkStartId = this.ctBookmark.getId().intValue();
        nextNode = this.ctBookmark.getDomNode();
        while (bookmarkStartId != bookmarkEndId) {
            nextNode = nextNode.getNextSibling();
            if (nextNode.getNodeName().contains(BookMark.BOOKMARK_END_TAG)) {
                try {
                    bookmarkEndId = Integer.parseInt(
                            nextNode.getAttributes().getNamedItem(BookMark.BOOKMARK_ID_ATTR_NAME).getNodeValue());
                } catch (NumberFormatException nfe) {
                    bookmarkEndId = bookmarkStartId;
                }
            } else {
                if (nextNode.getNodeName().equals(BookMark.RUN_NODE_NAME)) {
                    styleNode = this.getStyleNode(nextNode);
                }
            }
        }
        insertBeforeNode = nextNode.getNextSibling();
        if (styleNode != null) {
            run.getCTR().getDomNode().insertBefore(styleNode.cloneNode(true),
                    run.getCTR().getDomNode().getFirstChild());
        }
        if (insertBeforeNode != null) {
            this.para.getCTP().getDomNode().insertBefore(run.getCTR().getDomNode(), insertBeforeNode);
        }
    }

    /**
     * 替换标签时，设于标签的前面
     *
     * @param run
     */
    private void insertBeforeBookmark(XWPFRun run) {
        Node insertBeforeNode = null;
        Node childNode = null;
        Node styleNode = null;
        insertBeforeNode = this.ctBookmark.getDomNode();
        childNode = insertBeforeNode.getPreviousSibling();
        /* 如果找到节点，尝试从中获得样式 */
        if (childNode != null) {
            styleNode = this.getStyleNode1(childNode);
            /* 如果对上一个节点进行了样式设置，则将此样式应用于将要插入的文本 */
            if (styleNode != null) {
                run.getCTR().getDomNode().insertBefore(styleNode.cloneNode(true),
                        run.getCTR().getDomNode().getFirstChild());
            }
        }
        /* 将文本插入bookmarkStart标记前面的段落中 */
        this.para.getCTP().getDomNode().insertBefore(run.getCTR().getDomNode(), insertBeforeNode);
    }

    /**
     * 将内容替换书签
     *
     * @param run
     */
    private void replaceBookmark(XWPFRun run) {
        Node nextNode = null;
        Node styleNode = null;
        Node lastRunNode = null;
        Node toDelete = null;
        NodeList childNodes = null;
        Stack<Node> nodeStack = null;
        boolean textNodeFound = false;
        boolean foundNested = true;
        int bookmarkStartId = 0;
        int bookmarkEndId = -1;
        int numChildNodes = 0;
        nodeStack = new Stack<Node>();
        bookmarkStartId = this.ctBookmark.getId().intValue();
        nextNode = this.ctBookmark.getDomNode();
        nodeStack.push(nextNode);
        /* 循环遍历节点，查找匹配的bookmarkEnd标记 */
        while (bookmarkStartId != bookmarkEndId) {
            nextNode = nextNode.getNextSibling();
            nodeStack.push(nextNode);
            /* 如果找到结束标记，判断是否与开始标记匹配,是则结束while循环 */
            if (nextNode.getNodeName().contains(BookMark.BOOKMARK_END_TAG)) {
                try {
                    bookmarkEndId = Integer.parseInt(
                            nextNode.getAttributes().getNamedItem(BookMark.BOOKMARK_ID_ATTR_NAME).getNodeValue());
                } catch (NumberFormatException nfe) {
                    bookmarkEndId = bookmarkStartId;
                }
            }
        }
        /* 如果在书签标签之间找到的节点堆栈不为空，则必须将其删除 */
        if (!nodeStack.isEmpty()) {
            lastRunNode = nodeStack.peek();
            if ((lastRunNode.getNodeName().equals(BookMark.RUN_NODE_NAME))) {
                styleNode = this.getStyleNode(lastRunNode);
                if (styleNode != null) {
                    run.getCTR().getDomNode().insertBefore(styleNode.cloneNode(true),
                            run.getCTR().getDomNode().getFirstChild());
                }
            }
            this.deleteChildNodes(nodeStack);
        }
        /* 将文本放置在书签标记之间的位置 */
        this.para.getCTP().getDomNode().insertBefore(run.getCTR().getDomNode(), nextNode);
    }

    /**
     * 替换书签的文本时，删除任何节点
     *
     * @param nodeStack
     */
    private void deleteChildNodes(Stack<Node> nodeStack) {
        Node toDelete = null;
        int bookmarkStartId = 0;
        int bookmarkEndId = 0;
        boolean inNestedBookmark = false;
        /* 列表中的第一个元素是bookmarkStart标签,不得删除 */
        for (int i = 1; i < nodeStack.size(); i++) {
            toDelete = nodeStack.elementAt(i);
            if (toDelete.getNodeName().contains(BookMark.BOOKMARK_START_TAG)) {
                bookmarkStartId = Integer
                        .parseInt(toDelete.getAttributes().getNamedItem(BookMark.BOOKMARK_ID_ATTR_NAME).getNodeValue());
                inNestedBookmark = true;
            } else if (toDelete.getNodeName().contains(BookMark.BOOKMARK_END_TAG)) {
                bookmarkEndId = Integer
                        .parseInt(toDelete.getAttributes().getNamedItem(BookMark.BOOKMARK_ID_ATTR_NAME).getNodeValue());
                if (bookmarkEndId == bookmarkStartId) {
                    inNestedBookmark = false;
                }
            } else {
                if (!inNestedBookmark) {
                    this.para.getCTP().getDomNode().removeChild(toDelete);
                }
            }
        }
    }

    /**
     * 获取节点样式信息
     *
     * @param parentNode
     * @return
     */
    private Node getStyleNode(Node parentNode) {
        Node childNode = null;
        Node styleNode = null;
        if (parentNode != null) {
            /* 如果节点表示运行，并且它具有子节点，那么可以进一步处理 */
            if (parentNode.getNodeName().equalsIgnoreCase(BookMark.RUN_NODE_NAME) && parentNode.hasChildNodes()) {
                childNode = parentNode.getFirstChild();
                if (childNode.getNodeName().equals(STYLE_NODE_NAME)) {
                    styleNode = childNode;
                } else {
                    while ((childNode = childNode.getNextSibling()) != null) {
                        if (childNode.getNodeName().equals(BookMark.STYLE_NODE_NAME)) {
                            styleNode = childNode;
                            childNode = null;
                        }
                    }
                }
            }
        }
        return (styleNode);
    }

    private Node getStyleNode1(Node parentNode) {
        Node styleNode = null;
        if (parentNode != null) {
            /* 如果节点表示运行，并且它具有子节点，那么可以进一步处理 */
            if (parentNode.hasChildNodes()) {
                NodeList ls = parentNode.getChildNodes();
                for (int i = 0; i < ls.getLength(); i++) {
                    if (ls.item(i).getNodeName().equals(STYLE_NODE_NAME)) {
                        styleNode = ls.item(i);
                        break;
                    }
                }
            }
        }
        return (styleNode);
    }

    /**
     * 获取文本数据
     *
     * @return
     * @throws XmlException
     */
    public String getBookmarkText() throws XmlException {
        StringBuilder builder = null;
        // Are we dealing with a bookmarked table cell? If so, the entire
        // contents of the cell - if anything - must be recovered and returned.
        if (this.tableCell != null) {
            builder = new StringBuilder(this.tableCell.getText());
        } else {
            builder = this.getTextFromBookmark();
        }
        return (builder == null ? null : builder.toString());
    }

    /**
     * 获取标签文本信息
     *
     * @return
     * @throws XmlException
     */
    private StringBuilder getTextFromBookmark() throws XmlException {
        int startBookmarkId = 0;
        int endBookmarkId = -1;
        Node nextNode = null;
        StringBuilder builder = null;
        startBookmarkId = this.ctBookmark.getId().intValue();
        nextNode = this.ctBookmark.getDomNode();
        builder = new StringBuilder();
        while (startBookmarkId != endBookmarkId) {
            nextNode = nextNode.getNextSibling();
            if (nextNode.getNodeName().contains(BookMark.BOOKMARK_END_TAG)) {
                try {
                    endBookmarkId = Integer.parseInt(
                            nextNode.getAttributes().getNamedItem(BookMark.BOOKMARK_ID_ATTR_NAME).getNodeValue());
                } catch (NumberFormatException nfe) {
                    endBookmarkId = startBookmarkId;
                }
            } else {
                if (nextNode.getNodeName().equals(BookMark.RUN_NODE_NAME) && nextNode.hasChildNodes()) {
                    /* 从子节点获取文本 */
                    builder.append(this.getTextFromChildNodes(nextNode));
                }
            }
        }
        return (builder);
    }

    /**
     * 获取子节点文本信息
     *
     * @param node
     * @return
     * @throws XmlException
     */
    private String getTextFromChildNodes(Node node) throws XmlException {
        NodeList childNodes = null;
        Node childNode = null;
        CTText text = null;
        StringBuilder builder = new StringBuilder();
        int numChildNodes = 0;
        childNodes = node.getChildNodes();
        numChildNodes = childNodes.getLength();
        for (int i = 0; i < numChildNodes; i++) {
            childNode = childNodes.item(i);
            if (childNode.getNodeName().equals(BookMark.TEXT_NODE_NAME)) {
                if (childNode.getNodeType() == Node.TEXT_NODE) {
                    builder.append(childNode.getNodeValue());
                } else {
                    text = CTText.Factory.parse(childNode);
                    builder.append(text.getStringValue());
                }
            }
        }
        return (builder.toString());
    }

    private void handleBookmarkedCells(String bookmarkValue, int where) {
        List<XWPFParagraph> paraList = null;
        XWPFParagraph para = null;
        /* 如果段落来自表单元格，则获取列表，并删除任何段落以及所有段落 */
        paraList = this.tableCell.getParagraphs();
        for (int i = 0; i < paraList.size(); i++) {
            this.tableCell.removeParagraph(i);
        }
        para = this.tableCell.addParagraph();
        para.createRun().setText(bookmarkValue);
    }
}