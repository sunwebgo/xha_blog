package com.xha.domain.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagByIdVo {
    private Long id;

    private String name;

    private String remark;
}
