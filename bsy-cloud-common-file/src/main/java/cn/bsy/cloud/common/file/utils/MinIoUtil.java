package cn.bsy.cloud.common.file.utils;

import cn.hutool.core.util.StrUtil;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.*;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static io.minio.GetPresignedObjectUrlArgs.DEFAULT_EXPIRY_TIME;

/**
 * minio8的操作工具类，通用版
 *
 * @author gaoh
 */
@Component(value = "minIoUtil")
@AllArgsConstructor
@ConditionalOnProperty(prefix = "minio", name = {"endpoint", "accessKey", "secretKey", "port"}, matchIfMissing = false)
public class MinIoUtil {
    private MinioClient minioClient;

    /**
     * 判断存储桶，存在返回true
     *
     * @param bucketName 存储桶名称
     * @return
     */
    @SneakyThrows
    public boolean bucketExists(String bucketName) {
        boolean flag = false;
        flag = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (flag) {
            return true;
        }
        return false;
    }

    /**
     * 创建
     */
    @SneakyThrows
    public boolean makeBucket(String bucketName) {
        boolean flag = bucketExists(bucketName);
        if (flag) {
            return false;
        } else {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            return true;
        }
    }

    /**
     * 删除
     *
     * @return
     */
    @SneakyThrows
    public boolean removeBucket(String bucketName) {
        boolean flag = bucketExists(bucketName);
        if (flag) {
            Iterable<Result<Item>> myObjects = listObjects(bucketName);
            for (Result<Item> result : myObjects) {
                Item item = result.get();
                // 有对象文件，则删除失败
                if (item.size() > 0) {
                    return false;
                }
            }
            //存储桶为空时才能删
            minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
            flag = bucketExists(bucketName);
            if (!flag) {
                return true;
            }
        }
        return false;
    }

    /**
     * 查询列表
     *
     * @return
     */
    @SneakyThrows
    public List<String> listBucketNames() {
        List<Bucket> bucketList = listBuckets();
        List<String> bucketListName = new ArrayList<>(bucketList.size());
        for (Bucket bucket : bucketList) {
            bucketListName.add(bucket.name());
        }
        return bucketListName;
    }

    /**
     * 查询所有
     *
     * @return
     */
    @SneakyThrows
    public List<Bucket> listBuckets() {
        return minioClient.listBuckets();
    }

    /**
     * 查询存储中的对象
     */
    @SneakyThrows
    public List<String> listObjectNames(String bucketName) {
        List<String> listObjectNames = new ArrayList<>();
        boolean flag = bucketExists(bucketName);
        if (flag) {
            Iterable<Result<Item>> myObjects = listObjects(bucketName);
            for (Result<Item> result : myObjects) {
                Item item = result.get();
                listObjectNames.add(item.objectName());
            }
        }
        return listObjectNames;
    }

    /**
     * 查询所有对象
     */
    @SneakyThrows
    public Iterable<Result<Item>> listObjects(String bucketName) {
        return minioClient.listObjects(ListObjectsArgs.builder().bucket(bucketName).build());
    }

    /**
     * 通过prefix查询
     *
     * @param bucketName 存储桶名称
     * @param prefix     前缀
     * @param after      后缀
     * @param maxKeys    最大数量
     * @return
     */
    @SneakyThrows
    public Iterable<Result<Item>> listObjects(String bucketName, String prefix, String after, int maxKeys) {
        ListObjectsArgs.Builder builder = ListObjectsArgs.builder().bucket(bucketName);
        if (prefix != null && prefix.length() > 0) {
            builder.prefix(prefix);
        }
        if (after != null && after.length() > 0) {
            builder.startAfter(after);
        }
        if (maxKeys > 0) {
            builder.maxKeys(maxKeys);
        }
        return minioClient.listObjects(builder.build());
    }

    /**
     * 删除
     */
    @SneakyThrows
    public void deleteObjectTags(String bucketName, String objectName) {
        minioClient.deleteObjectTags(DeleteObjectTagsArgs.builder().bucket(bucketName).object(objectName).build());
    }


    /**
     * 有时候是IO流的形式进行读取或者传递接口
     *
     * @return
     */
    @SneakyThrows
    public InputStream getObject(String bucketName, String objectName) {

        InputStream stream = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .build());
        return stream;
    }

    /**
     * 通过起始位置进行控制续传
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     * @param offset     起始字节的位置
     * @param length     要读取的长度 (可选，如果无值则代表读到文件结尾)
     * @return
     */
    @SneakyThrows
    public InputStream getObject(String bucketName, String objectName, long offset, Long length) {
        InputStream stream = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .offset(offset)
                        .length(length)
                        .build());
        return stream;
    }

    /**
     * 查询对应对象的标签
     *
     * @return
     */
    public Tags getObjectTags(String bucketName, String objectName) {
        Tags tags = null;
        try {
            tags = minioClient.getObjectTags(GetObjectTagsArgs.builder().bucket(bucketName).object(objectName).build());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return tags;
    }

    /**
     * 删除
     */
    @SneakyThrows
    public boolean removeObject(String bucketName, String objectName) {
        minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(objectName).build());
        return true;
    }

    /**
     * 批量删除
     */
    @SneakyThrows
    public List<String> removeObjects(String bucketName, List<String> objectNames) {
        List<String> deleteErrorNames = new ArrayList<>();
        boolean flag = bucketExists(bucketName);
        if (flag) {
            List<DeleteObject> list = new LinkedList<>();
            objectNames.forEach(item -> list.add(new DeleteObject(item)));

            Iterable<Result<DeleteError>> results = minioClient.removeObjects(RemoveObjectsArgs.builder().bucket(bucketName).objects(list).build());
            for (Result<DeleteError> result : results) {
                DeleteError error = result.get();
                deleteErrorNames.add(error.objectName());
            }
        }
        return deleteErrorNames;
    }

    /**
     * 给文件添加tags
     */
    @SneakyThrows
    public void setObjectTags(String bucketName, String objectName, Map<String, String> tags) {
        minioClient.setObjectTags(SetObjectTagsArgs.builder().bucket(bucketName).object(objectName).tags(tags).build());
    }

    /**
     * 分享链接
     *
     * @param bucketName 存储桶名称
     * @param objectName 存储桶里的对象名称
     * @param expires    失效时间（以秒为单位），默认是7天，不得大于七天
     * @return
     */
    @SneakyThrows
    public String getPresignedObjectUrl(String bucketName, String objectName, Integer expires, Method method) {
        String url = "";
        if (expires < 1 || expires > DEFAULT_EXPIRY_TIME) {
            throw new Exception();
        }
        if (method == null) {
            method = Method.GET;
        }
        url = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                .method(method)
                .bucket(bucketName).object(objectName)
                .expiry(expires, TimeUnit.SECONDS).build());
        return url;
    }


    /**
     * 下载文件方式1
     */
    @SneakyThrows
    public void downloadObject(String bucketName, String objectName, String fileName) {
        minioClient.downloadObject(
                DownloadObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .filename(fileName)
                        .build());
    }

    /**
     * 下载文件方式2
     * @param bucketName 桶名
     * @param objectName 文件存储名
     * @param response
     */
    @SneakyThrows
    public void downloadObject(String bucketName, String objectName, HttpServletResponse response) {
        downloadObject(bucketName,objectName,response,null);
    }
    /**
     * 下载文件方式3
     * @param bucketName 桶名
     * @param objectName 文件存储名
     * @param fileName 文件真实名称
     * @param response
     */
    @SneakyThrows
    public void downloadObject(String bucketName, String objectName, HttpServletResponse response, String fileName) {
        InputStream is = getObject(bucketName, objectName);
        if (is == null) {
            return;
        }
        if(StrUtil.isBlank(fileName)){
            fileName = objectName.substring(objectName.indexOf("/") + 1, objectName.length());
        }
        // 解决乱码问题
        fileName = URLEncoder.encode(fileName, "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        ServletOutputStream servletOutputStream = response.getOutputStream();
        int len;
        byte[] buffer = new byte[1024];
        while ((len = is.read(buffer)) > 0) {
            servletOutputStream.write(buffer, 0, len);
        }
        servletOutputStream.flush();
        is.close();
        servletOutputStream.close();
    }
}
