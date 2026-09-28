package com.backend.endpoint.dto.user;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDtoWithToken extends UserDto {

    private String token;

    public UserDtoWithToken(UserDto userDto, String token) {
        super(userDto.getId(), userDto.getNickname(), userDto.getEmail(),
                userDto.getIsAdmin(), userDto.getIsLocked());
        this.token = token;
    }
}