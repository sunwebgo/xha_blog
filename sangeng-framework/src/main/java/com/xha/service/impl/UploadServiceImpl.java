package com.xha.service.impl;


import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.ObjectMetadata;
import com.xha.domain.ResponseResult;
import com.xha.service.UploadService;
import com.xha.utils.ImgUtils;
import com.xha.utils.OssUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;


@Service
public class UploadServiceImpl implements UploadService {

    /**
     * 文件上传阿里云OSS
     *
     * @param imgFile
     * @return
     */
    @Override
    public ResponseResult uploadImg(MultipartFile imgFile) {
//        1.获取到原始文件名
        String originalFilename = imgFile.getOriginalFilename();
        String fileName = ImgUtils.generateFilePath(originalFilename);
        String imgUrl = ossUpload(imgFile, fileName);
        return ResponseResult.okResult(imgUrl);
    }


    private String ossUpload(MultipartFile multipartFile, String fileName) {
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder()
                .build(OssUtils.END_POINT, OssUtils.ACCESS_KEY_ID, OssUtils.ACCESS_KEY_SECRET);


        // 填写Object完整路径，完整路径中不能包含Bucket名称，例如exampledir/exampleobject.txt。
        String objectName = "sangengblog/" + fileName;
        try {

            InputStream inputStream = multipartFile.getInputStream();
            // 创建PutObject请求。
            ossClient.putObject(OssUtils.BUCKET_NAME, objectName, inputStream);
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
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
//        返回对象存储中的图片路径
        return "https://" + OssUtils.BUCKET_NAME + "." + OssUtils.END_POINT + "/" + objectName;
    }
}
