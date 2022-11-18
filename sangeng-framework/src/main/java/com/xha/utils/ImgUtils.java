package com.xha.utils;


import java.util.UUID;

public class ImgUtils {
    public static String generateFilePath(String originalFilename) {
//        生成UUID
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
//        获取到文件后缀名，截取字符串
        int index = originalFilename.lastIndexOf(".");
        String fileType = originalFilename.substring(index);
        return new StringBuilder().append(uuid).append(fileType).toString();
    }
}
