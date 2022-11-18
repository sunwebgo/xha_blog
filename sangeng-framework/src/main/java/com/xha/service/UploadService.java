package com.xha.service;

import com.xha.domain.ResponseResult;
import org.springframework.web.multipart.MultipartFile;


public interface UploadService {
    ResponseResult uploadImg(MultipartFile img);
}
