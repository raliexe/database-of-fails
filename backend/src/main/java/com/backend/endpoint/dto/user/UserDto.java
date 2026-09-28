package com.backend.endpoint.dto.user;

import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Builder
public class UserDto {

    private Long id;
    private String nickname;
    private String email;
    private Boolean isAdmin;
    private Boolean isLocked;

}
