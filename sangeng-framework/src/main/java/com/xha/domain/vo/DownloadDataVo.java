package com.xha.domain.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DownloadDataVo {

    @ExcelProperty("分类id")
    private Long id;

    @ExcelProperty("分类名")
    private String name;

    @ExcelProperty("状态")
    private String status;

    @ExcelProperty("描述")
    private String description;
}
