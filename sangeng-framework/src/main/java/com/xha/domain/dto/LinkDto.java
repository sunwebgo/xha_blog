package com.xha.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LinkDto {
    private Long id;

    private String name;

    private String status;

    private String address;

    private String description;

    private String logo;
}
