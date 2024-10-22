package com.hana.hana_spring.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

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
    private String domain;
    private String key;
    private String secret;
    private String bucket;
    private String region;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String upload(MultipartFile file) {
        // 生成UUID作为文件名
        String uuid = UUID.randomUUID().toString();
        // 获取文件的原始扩展名
        String original_filename = file.getOriginalFilename();
        String extension = original_filename.substring(original_filename.lastIndexOf("."));
        // 拼接新的文件名
        String object_name = uuid + extension;

        // 配置七牛云的存储区域
        Configuration cfg = new Configuration(region());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本
        UploadManager upload_manager = new UploadManager(cfg);

        try {
            // 获取文件输入流
            InputStream input_stream = file.getInputStream();

            // 构建七牛云认证对象
            Auth auth = Auth.create(key, secret);
            String upload_token = auth.uploadToken(bucket);

            // 上传文件到七牛云
            Response response = upload_manager.put(input_stream, object_name, upload_token, null, null);

            // 解析上传成功的结果
            DefaultPutRet put_ret = objectMapper.readValue(response.bodyString(), DefaultPutRet.class);

            // 返回文件的访问URL
            return "http://" + domain + "/" + put_ret.key;
        } catch (IOException e) {
            log.error("上传失败！");
            return null;
        }
    }

    public void delete(String url) {
        String object_name = get_object_name_from_url(url);

        // 配置七牛云存储区域
        Configuration cfg = new Configuration(region());

        try {
            // 构建七牛云认证对象
            Auth auth = Auth.create(key, secret);
            BucketManager bucket_manager = new BucketManager(auth, cfg);

            bucket_manager.delete(bucket, object_name);
        } catch (QiniuException e) {
            log.error("删除文件失败！");
        }
    }

    private String get_object_name_from_url(String url) {
        // 假设URL格式为 https://<bucketName>.<endpoint>/<objectName>
        String[] parts = url.split("/", 4);
        return parts[3]; // 获取 objectName
    }

    private Region region() {
        return Region.createWithRegionId(region.toLowerCase());
    }
}
