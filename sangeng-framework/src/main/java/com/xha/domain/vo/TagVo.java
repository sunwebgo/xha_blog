package com.xha.domain.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagVo {

    private Long id;

    /**
     * 标签名
     */
    private String name;


    /**
     * 备注
     */
    private String remark;
}
