package com.xha.controller;

import com.xha.domain.ResponseResult;
import com.xha.service.UploadService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@RestController
public class UploadController {


    @Resource
    private UploadService uploadService;

    /**
     * 上传img
     *
     * @param img img
     * @return {@link ResponseResult}
     */
    @PostMapping("/upload")
    public ResponseResult uploadImg(MultipartFile img){
        return uploadService.uploadImg(img);
    }
}
