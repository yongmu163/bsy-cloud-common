package cn.bsy.cloud.common.file.constant;

/**
 * @Description: 文件类型枚举
 * @Author gaoh
 * @Date 2022/7/31 0031 下午 3:02
 **/
public enum FileTypeEnum {
    /**
     * 视频
     */
    FILE_TYPE_VIDEO("VIDEO", "视频"),
    /**
     * 音频
     */
    FILE_TYPE_AUDIO("AUDIO", "音频"),
    /**
     * 图片
     */
    FILE_TYPE_IMAGE("IMAGE", "图片"),
    /**
     * 其他文件
     */
    FILE_TYPE_OTHER("OTHER", "其他文件");


    private final String code;

    private final String desc;

    FileTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
