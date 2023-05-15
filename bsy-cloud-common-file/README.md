[TOC]

#common-file
## Excel导入格式检验组件
1.在excel导入时调用校验方法
~~~
HEAD_ROW_NUMBER ： excel读取数据开始行
EXCEPTION_LINE_COUNT： 异常报错行数

@Override
@Transactional(rollbackFor = Exception.class)
public void upload(MultipartFile file) {

    iGenerateExcelService.read(file, EXCEL_FILE_TYPE, HEAD_ROW_NUMBER, new AbstractExcelListener<CoreRiskPointDeviceUpDownLoadBO>() {
        @Override
        public void customSaveData(List<CoreRiskPointDeviceUpDownLoadBO> list) {
            List<String> exceptionMessage = new ArrayList<>();
            if(list.size() <= 0){
                exceptionMessage.add("请添加数据");
                throw new CustomizeException(JSONObject.toJSON(exceptionMessage).toString());
            }

           // 导入校验调用位置 
		   coreRiskPointDeviceService.validate(list,HEAD_ROW_NUMBER,EXCEPTION_LINE_COUNT);
            //数据转换及赋值
            List<CoreRiskPointDevice> coreRiskPointDevices = ObjectUtils.cloneObjList(list, CoreRiskPointDevice.class);
            batchSave(coreRiskPointDevices);
        }
    });
}
~~~

2.在接口Iservice层添加方法，List参数根据实际需要自己更改
*** 必须单独写一个service方法，否则注解切面切不过去
~~~
Result validate(List<CoreRiskPointDeviceUpDownLoadBO> list, int head, int exceptionCount);
~~~
3.在serviceImpl 实现层，实现方法，并添加 @AutoValidateExcel  注释
~~~
@AutoValidateExcel
@Override
public Result validate(List<CoreRiskPointDeviceUpDownLoadBO> list, int head, int exceptionCount) {
    final String[] s = {""};
    s[0] = "导入成功，导入条数:" + list.size();
    return Result.ok(s[0]);
}
~~~
4.修改 BO类，添加校验注释
~~~
/**
 * 风险点名称
 * */
@ExcelProperty(index = 0)
@ValidateRule(columnName = "风险点名称",isUnique = true,required = true, type = ColumnDataTypeEnum.STRING, maxLen = 100)
private String riskPointName;
        
/**
 * 风险点地点
 * */
@ExcelProperty(index = 1)
@ValidateRule(columnName = "风险点地点",isGroupUnique = true,type = ColumnDataTypeEnum.STRING, maxLen = 100)
private String riskPointPlace;
        
/**
 * 检查项目名称
 * */
@ExcelProperty(index = 2)
@ValidateRule(columnName = "检查项目名称",required = true, type = ColumnDataTypeEnum.STRING, maxLen = 100)
private String checkProjectName;
        
/**
 * 检查项目标准
 * */
@ExcelProperty(index = 3)
@ValidateRule(columnName = "检查项目标准",required = true, type = ColumnDataTypeEnum.STRING, maxLen = 100)
private String checkProjectStandard;
        
/**
 * 事故类型及后果
 * */
@ExcelProperty(index = 4)
@ValidateRule(columnName = "事故类型及后果",required = true, type = ColumnDataTypeEnum.STRING, maxLen = 100)
private String accidentTypeAftermath;

/**
 * 风险点级别 01 低风险 02 一般风险 03 较大风险 04 重大风险
 * */
@ExcelProperty(index = 5)
@ValidateRule(columnName = "风险等级",type = ColumnDataTypeEnum.DICT, dict = "{\"01\":\"低风险\",\"02\":\"一般风险\",\"03\":\"较大风险\",\"04\":\"重大风险\"}")
private String riskLevelTypeCode;

/**
 * 管控级别  01 单位 02 部门 03 岗位
 * */
@ExcelProperty(index = 6)
@ValidateRule(columnName = "管控层级",required = true, type = ColumnDataTypeEnum.DICT, dict = "{\"01\":\"单位\",\"02\":\"部门\",\"03\":\"岗位\"}")
private String managementLevelCode;
~~~
BO类注解说明：
- @ExcelProperty(index = 0)
    1. 此注解用于读取excel数据，必填添加。index 为 excel列的顺序，0对应A列，1对应B列。
- @ValidateRule
    1. columnName ：必填，字段列名，用于报错误信息
    1. required： 校验字段是否为必填项
    1. isUnique： 单独校验重复数据，只能在一个字段上添加
    1. isGroupUnique： 组合校验重复数据，与isUnique 只能存在一个
    1. type：类型为ColumnDataTypeEnum

~~~
**ColumnDataTypeEnum.STRING #字符串检验**
	可选参数：maxLen  最大长度；
**ColumnDataTypeEnum.DICT #字典类型校验**
	必填参数：dict 字典项 内容为json格式的map。往数据库存的值是key，excel里的值是value
	示例： dict = "{\"01\":\"低风险\",\"02\":\"一般风险\",\"03\":\"较大风险\",\"04\":\"重大风险\"}"

**ColumnDataTypeEnum.INTEGER #INTEGER类型校验**
  非 INTEGER 类型，错误提示非 整数类型

**ColumnDataTypeEnum.DOUBLE #DOUBLE类型校验**
  非 DOUBLE 类型，错误提示非 小数类型
  
**ColumnDataTypeEnum.DATE #DATE类型校验**
	必填参数：dateFormat 日期格式化格式
	示例： type = ColumnDataTypeEnum.DATE, dateFormat = ColumnDateFormatEnum.YYYY_MM_DD_HH_MM_SS
		*- ColumnDateFormatEnum #日期格式化枚举*

			- YYYY_MM_DD_1("yyyy-mm-dd")
			错误提示应为 yyyy-mm-dd 格式

			- YYYY_MM_DD_HH_MM_SS("yyyy-MM-dd HH:mm:ss")
			错误提示应为 yyyy-MM-dd HH:mm:ss 格式
~~~

