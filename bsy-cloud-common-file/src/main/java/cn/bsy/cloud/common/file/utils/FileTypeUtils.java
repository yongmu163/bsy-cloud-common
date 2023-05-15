package cn.bsy.cloud.common.file.utils;

import cn.bsy.cloud.common.file.constant.FileTypeEnum;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;

/**
 * @Description: 文件类型判断的工具类
 * @Author gaoh
 * @Date 2022年07月31日 下午 2:19
 **/
@Slf4j
@UtilityClass
public class FileTypeUtils {
    static final String JPG = ".JPG";
    static final String JPEG = ".JPEG";
    static final String GIF = ".GIF";
    static final String PNG = ".PNG";
    static final String BMP = ".BMP";
    static final String PCX = ".PCX";
    static final String TGA = ".TGA";
    static final String PSD = ".PSD";
    static final String TIFF = ".TIFF";
    static final String MP3 = ".mp3";
    static final String OGG = ".OGG";
    static final String WAV = ".WAV";
    static final String REAL = ".REAL";
    static final String APE = ".APE";
    static final String MODULE = ".MODULE";
    static final String MIDI = ".MIDI";
    static final String VQF = ".VQF";
    static final String CD = ".CD";
    static final String MP4 = ".mp4";
    static final String AVI = ".avi";
    static final String MPEG_1 = ".MPEG-1";
    static final String RM = ".RM";
    static final String ASF = ".ASF";
    static final String WMV = ".WMV";
    static final String QLV = ".qlv";
    static final String MPEG_2 = ".MPEG-2";
    static final String MPEG_4 = ".MPEG4";
    static final String MOV = ".mov";
    static final String GP = ".3gp";

    /**
     * MultipartFile 转换成File
     *
     * @param multfile
     * @return
     */
    public File multipartToFile(MultipartFile multfile) {
        CommonsMultipartFile cf = (CommonsMultipartFile) multfile;
        DiskFileItem fi = (DiskFileItem) cf.getFileItem();
        File file = fi.getStoreLocation();
        return file;
    }

    /**
     * 图片格式判断
     *
     * @param perfix
     * @return
     */
    public boolean imageType(String perfix) {
        if (JPG.equalsIgnoreCase(perfix)
                || JPEG.equalsIgnoreCase(perfix)
                || GIF.equalsIgnoreCase(perfix)
                || PNG.equalsIgnoreCase(perfix)
                || BMP.equalsIgnoreCase(perfix)
                || PCX.equalsIgnoreCase(perfix)
                || TGA.equalsIgnoreCase(perfix)
                || PSD.equalsIgnoreCase(perfix)
                || TIFF.equalsIgnoreCase(perfix)) {
            return true;
        }
        return false;
    }

    /**
     * 音频格式判断
     *
     * @param perfix
     * @return
     */
    public boolean audioType(String perfix) {
        if (perfix.equalsIgnoreCase(MP3)
                || perfix.equalsIgnoreCase(OGG)
                || perfix.equalsIgnoreCase(WAV)
                || perfix.equalsIgnoreCase(REAL)
                || perfix.equalsIgnoreCase(APE)
                || perfix.equalsIgnoreCase(MODULE)
                || perfix.equalsIgnoreCase(MIDI)
                || perfix.equalsIgnoreCase(VQF)
                || perfix.equalsIgnoreCase(CD)) {
            return true;
        }
        return false;
    }

    /**
     * 视频格式判断
     * @param perfix
     * @return
     */
    public boolean videoType(String perfix) {
        if (perfix.equalsIgnoreCase(MP4)
                || perfix.equalsIgnoreCase(AVI)
                || perfix.equalsIgnoreCase(MPEG_1)
                || perfix.equalsIgnoreCase(RM)
                || perfix.equalsIgnoreCase(ASF)
                || perfix.equalsIgnoreCase(WMV)
                || perfix.equalsIgnoreCase(QLV)
                || perfix.equalsIgnoreCase(MPEG_2)
                || perfix.equalsIgnoreCase(MPEG_4)
                || perfix.equalsIgnoreCase(MOV)
                || perfix.equalsIgnoreCase(GP)) {
            return true;
        }
        return false;
    }

    /**
     * 根据后缀获取文件枚举类型
     * @param perfix
     * @return
     */
    public FileTypeEnum getFileType(String perfix) {
        if(FileTypeUtils.imageType(perfix)) {
          return  FileTypeEnum.FILE_TYPE_IMAGE;
        }
        if(FileTypeUtils.audioType(perfix)) {
            return  FileTypeEnum.FILE_TYPE_AUDIO;
        }
        if(FileTypeUtils.videoType(perfix)) {
            return  FileTypeEnum.FILE_TYPE_VIDEO;
        }
        return FileTypeEnum.FILE_TYPE_OTHER;
    }
}
