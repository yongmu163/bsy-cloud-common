package cn.bsy.cloud.common.file.constant;

import cn.hutool.core.util.StrUtil;

/**
 * @Description: 压缩类文件后缀枚举
 * @Author gaoh
 * @Date 2022/7/31 0031 下午 3:02
 **/
public enum CompressedFileSuffixEnum {
    /**
     * 7z
     */
    FILE_TYPE_7Z(".7z"),
    /**
     * zip
     */
    FILE_TYPE_ZIP(".zip"),
    /**
     * rar
     */
    FILE_TYPE_RAR(".rar"),
    /**
     * Z
     */
    FILE_TYPE_Z(".Z"),
    /**
     * gz
     */
    FILE_TYPE_GZ(".gz"),
    /**
     * tar
     */
    FILE_TYPE_TAR(".tar"),
    /**
     * jar
     */
    FILE_TYPE_JAR(".jar");


    private final String code;
    CompressedFileSuffixEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    /**
     * 根据编码获取对应的枚举类
     *
     * @param code
     * @return
     */
    public static CompressedFileSuffixEnum getCompressedFileSuffixEnum(String code) {
        for (CompressedFileSuffixEnum compressedFileSuffixEnum : values()) {
            if (StrUtil.equals(code, compressedFileSuffixEnum.code)) {
                return compressedFileSuffixEnum;
            }
        }
        return null;
    }
}
