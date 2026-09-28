package com.backend.endpoint.dto.user;

import com.backend.endpoint.dto.PageResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;

@AllArgsConstructor
public class UserPagedDto extends PageResponseDto<UserDto> {

    public UserPagedDto(Page<UserDto> userPage) {
        super(userPage);
    }
}