package com.backend.endpoint.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public abstract class PageRequestDto {

    @NotNull(message = "Page size must be given!")
    @Min(value = 0, message = "Page Size must be greater than or eqaul to 0!")
    private Integer size;

    @NotNull(message = "Page must be given!")
    @Min(value = 0, message = "Page must be greater than or eqaul to 0!")
    private Integer page;
}