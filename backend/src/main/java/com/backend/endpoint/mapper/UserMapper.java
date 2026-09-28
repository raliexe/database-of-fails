package com.backend.endpoint.mapper;

import com.backend.endpoint.dto.user.UserDto;
import com.backend.endpoint.dto.user.UserRegisterDto;
import com.backend.entity.AppUser;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper
public abstract class UserMapper {

    @Autowired
    private PasswordEncoder passwordEncoder;

    //Used for registration process
    public AppUser registerDtoToEntity(UserRegisterDto userRegisterDto) {
        if (userRegisterDto == null) {
            return null;
        }
        return new AppUser(userRegisterDto.getNickname(), userRegisterDto.getEmail(),
                passwordEncoder.encode(userRegisterDto.getPassword()), userRegisterDto.getIsAdmin());
    }

    public UserDto entityToUserDto(AppUser appUser) {
        if (appUser == null) {
            return null;
        }

        return new UserDto(appUser.getId(), appUser.getNickname(), appUser.getEmail(),
                appUser.getIsAdmin(), appUser.getIsLocked());
    }

    public UserRegisterDto entityToUserRegisterDto(AppUser appUser) {
        if (appUser == null) {
            return null;
        }
        return new UserRegisterDto(appUser.getEmail(), appUser.getNickname(),
                appUser.getPassword(), appUser.getPassword(), appUser.getIsAdmin());
    }

    public AppUser userRegisterDtoToEntity(UserRegisterDto userRegisterDto) {
        if (userRegisterDto == null) {
            return null;
        }
        return new AppUser(userRegisterDto.getNickname(), userRegisterDto.getEmail(), userRegisterDto.getPassword(),
                false);
    }
}