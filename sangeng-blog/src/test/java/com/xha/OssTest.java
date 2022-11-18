package com.xha;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@ConfigurationProperties(prefix = "oss")
@SpringBootTest
public class OssTest {

    private String endpoint;

    private String accessKeyId;

    private String accessKeySecret;

    private String bucketName;


    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }


    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    @Test
    public void test(){
        System.out.println(accessKeySecret);
        System.out.println(accessKeyId);
        System.out.println(endpoint);
    }

}                    