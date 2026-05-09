package com.gymapp.common.dto;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageRequestDto {

    @Min(value = 0, message = "Page must be 0 or greater")
    private Integer page;

    @Min(value = 1, message = "Size must be 1 or greater")
    private Integer size;
    private String sortBy;
    private String direction;
}


