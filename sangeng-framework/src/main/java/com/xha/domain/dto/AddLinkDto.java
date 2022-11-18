package com.xha.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddLinkDto {
    private String name;

    private Integer status;

    private String address;

    private String description;

    private String logo;
}
