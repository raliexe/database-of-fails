package com.backend.endpoint;

import com.backend.endpoint.dto.user.UserDto;
import com.backend.endpoint.dto.user.UserFilterDto;
import com.backend.endpoint.dto.user.UserPagedDto;
import com.backend.endpoint.dto.user.UserRegisterDto;
import com.backend.endpoint.mapper.UserMapper;
import com.backend.entity.AppUser;
import com.backend.service.email.EmailService;
import com.backend.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/management")
public class ManagementEndpoint {

    private final UserService userService;
    private final EmailService emailService;
    private final UserMapper userMapper;

    @Secured("ROLE_ADMIN")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    @Operation(summary = "Get list of locked/unlocked and/or searched by email users", security = @SecurityRequirement(name = "apiKey"))
    public UserPagedDto getUsers(@Valid UserFilterDto userFilterDto) {
        log.info("GET /api/v1/management {}", userFilterDto);
        Pageable pageable = PageRequest.of(userFilterDto.getPage(), userFilterDto.getSize());
        Page<AppUser> users = userService.getUnLockedUsers(userFilterDto);
        List<UserDto> userDtos = users.getContent().stream().map(userMapper::entityToUserDto).collect(Collectors.toList());
        UserPagedDto userPagedDto = new UserPagedDto(new PageImpl<>(userDtos, pageable, users.getTotalElements()));
        log.info("GET /api/v1/management {}", userPagedDto);
        return userPagedDto;
    }

    @Secured("ROLE_ADMIN")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/status")
    @Operation(summary = "Lock/Unlock user by given id", security = @SecurityRequirement(name = "apiKey"))
    public void changeLockStatus(@RequestParam Long id) {
        log.info("PUT /api/v1/management {}", id);
        userService.changeLockStatus(id);
    }

    @Secured("ROLE_ADMIN")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    @Operation(summary = "Reset password of a user", security = @SecurityRequirement(name = "apiKey"))
    public void resetPasswordAsAdmin(@PathVariable Long id) {
        log.info("PUT /api/v1/management/{}", id);
        emailService.sendPasswordResetMail(id);
    }

    @Secured("ROLE_ADMIN")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @Operation(summary = "Create a new user")
    public UserDto create(@Valid @RequestBody UserRegisterDto userRegisterDto) {
        log.info("POST /api/v1/management {}", userRegisterDto.toString());
        UserDto addedUser = userMapper.entityToUserDto(userService.addAppUser(userMapper.registerDtoToEntity(userRegisterDto)));
        log.info("POST /api/v1/users added: {}", addedUser);
        return addedUser;
    }
}