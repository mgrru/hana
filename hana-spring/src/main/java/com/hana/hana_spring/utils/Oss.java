package com.hana.hana_spring.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@ConfigurationProperties("oss.pic")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Oss {
    private String endpoint;
    private String username;
    private String password;
    private String bucket;

    public String upload(MultipartFile file) {
        // 生成UUID作为文件名
        String uuid = UUID.randomUUID().toString();
        // 获取文件的原始扩展名
        String original_filename = file.getOriginalFilename();
        String extension = original_filename.substring(original_filename.lastIndexOf("."));
        // 拼接新的文件名
        String object_name = uuid + extension;

        OSS oss_client = new OSSClientBuilder().build(endpoint, username, password);

        try {
            // 获取文件输入流
            InputStream input_stream = file.getInputStream();

            // 上传文件到指定的Bucket
            oss_client.putObject(bucket, object_name, input_stream);

            // 返回文件的访问URL
            return "https://" + bucket + "." + endpoint + "/" + object_name;
        } catch (IOException e) {
            log.error("上传失败！");
            return null;
        } finally {
            // 关闭OSSClient
            oss_client.shutdown();
        }
    }

    public void delete(String url) {
        String object_name = get_object_name_from_url(url);

        // 创建OSSClient实例
        OSS oss_client = new OSSClientBuilder().build(endpoint, username, password);

        try {
            // 删除文件
            oss_client.deleteObject(bucket, object_name);
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            // 关闭OSSClient
            if (oss_client != null) {
                oss_client.shutdown();
            }
        }
    }

    private String get_object_name_from_url(String url) {
        // 假设URL格式为 https://<bucketName>.<endpoint>/<objectName>
        String[] parts = url.split("/", 4);
        return parts[3]; // 获取 objectName
    }
}
