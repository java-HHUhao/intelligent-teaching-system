package cn.edu.hhu.spring.boot.starter.store.utils;

import io.minio.*;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;

/**
 * MinIO工具类
 */
@Slf4j
public class MinioUtils {
    
    private static MinioClient minioClient;

    /**
     * 初始化MinIO客户端
     *
     * @param endpoint   MinIO服务地址
     * @param accessKey 访问密钥
     * @param secretKey 密钥
     */
    public static void init(String endpoint, String accessKey, String secretKey) {
        minioClient = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }
    
    /**
     * 上传文件到MinIO
     *
     * @param bucketName  存储桶名称
     * @param objectName  对象名称
     * @param inputStream 文件输入流
     * @param contentType 文件类型
     * @throws Exception 上传异常
         */
    public static void uploadFile(String bucketName, String objectName, InputStream inputStream, String contentType) throws Exception {
            checkInit();
        // 检查存储桶是否存在，不存在则创建
        boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (!found) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }
        
        // 上传文件
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .stream(inputStream, inputStream.available(), -1)
                        .contentType(contentType)
                        .build()
        );
    }
    
    /**
     * 获取文件访问URL
     *
     * @param bucketName 存储桶名称
     * @param objectName 对象名称
     * @return 文件访问URL
     * @throws Exception 获取URL异常
         */
    public static String getFileUrl(String bucketName, String objectName) throws Exception {
            checkInit();
        return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .method(Method.GET)
                        .build()
        );
    }

    /**
     * 检查是否已初始化
     */
    private static void checkInit() {
        if (minioClient == null) {
            throw new RuntimeException("MinIO客户端未初始化，请先调用init方法");
        }
    }
} 