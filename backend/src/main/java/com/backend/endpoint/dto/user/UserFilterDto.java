package com.backend.endpoint.dto.user;

import com.backend.endpoint.dto.PageRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserFilterDto extends PageRequestDto {

    @Size(max = 100, message = "Email can be at most 100 character long!")
    private String email;

    private Boolean isLocked;
}