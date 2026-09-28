package com.backend.endpoint.dto.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GeneralSearchResponseDto {

    private Long id;
    private String name;
    private SearchCategory searchCategory;
}