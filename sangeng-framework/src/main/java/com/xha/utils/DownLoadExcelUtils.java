package com.xha.utils;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class DownLoadExcelUtils {


    /**
     * 设置Excel下载的请求头
     * @param fileName 文件名称
     * @param response 响应
     * @throws UnsupportedEncodingException 不支持编码异常
     */
    public static void setDownLoadHeader(String fileName, HttpServletResponse response) throws UnsupportedEncodingException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fname = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename=" + fname + ".xlsx");
    }
}
