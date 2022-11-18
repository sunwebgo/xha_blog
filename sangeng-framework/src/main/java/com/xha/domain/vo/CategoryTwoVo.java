package com.xha.domain.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryTwoVo {
    private Long id;
    private String name;
    private String status;
    private String description;
}
