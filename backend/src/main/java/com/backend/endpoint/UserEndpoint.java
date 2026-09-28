package com.backend.endpoint;

import com.backend.endpoint.dto.user.*;
import com.backend.endpoint.mapper.UserMapper;
import com.backend.entity.AppUser;
import com.backend.security.JwtTokenizer;
import com.backend.service.email.EmailService;
import com.backend.service.user.UserService;
import com.backend.util.AuthenticationManager;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;


@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/users")
public class UserEndpoint {

    private final UserService userService;
    private final EmailService emailService;
    private final UserMapper userMapper;

    private final AuthenticationManager authenticationManager;
    private final JwtTokenizer jwtTokenizer;

    @PermitAll
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @Operation(summary = "Create a new user")
    public UserDto create(@Valid @RequestBody UserRegisterDto userRegisterDto) {
        log.info("POST /api/v1/users {}", userRegisterDto.toString());
        userRegisterDto.setIsAdmin(false);
        UserDto addedUser = userMapper.entityToUserDto(userService.addAppUser(userMapper.registerDtoToEntity(userRegisterDto)));
        log.info("POST /api/v1/users added: {}", addedUser);
        return addedUser;
    }

    @Secured("ROLE_USER")
    @GetMapping
    @Operation(summary = "Get user information", security = @SecurityRequirement(name = "apiKey"))
    public UserDto find() {
        log.info("GET /api/v1/users/");
        String email = authenticationManager.getLoggedInUser();
        UserDto userDto = userMapper.entityToUserDto(userService.findAppUserByEmail(email));
        log.info("GET /api/v1/users/ returned: {}", userDto);
        return userDto;
    }

    @Secured("ROLE_USER")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping
    @Operation(summary = "Update user information", security = @SecurityRequirement(name = "apiKey"))
    public UserDto update(@Valid @RequestBody UserRegisterDto userDto) {
        log.info("Put /api/v1/users {}", userDto);
        String email = authenticationManager.getLoggedInUser();
        log.info(email);
        AppUser currentUser = userService.findAppUserByEmail(email);
        if (!email.equals(userDto.getEmail())) {
            String token = jwtTokenizer.getAuthToken(userDto.getEmail(), currentUser.getRoles());
            AppUser toUpdate = userMapper.userRegisterDtoToEntity(userDto);
            UserDto updatedUser = userMapper.entityToUserDto(userService.updateUser(toUpdate, currentUser.getId()));
            UserDtoWithToken updatedUserDtoWithToken = new UserDtoWithToken(updatedUser, token);
            log.info("Put /api/v1/users returned: {}", updatedUserDtoWithToken);
            return updatedUserDtoWithToken;

        }
        AppUser toUpdate = userMapper.userRegisterDtoToEntity(userDto);
        UserDto updatedUser = userMapper.entityToUserDto(userService.updateUser(toUpdate, currentUser.getId()));
        log.info("Put /api/v1/users returned: {}", updatedUser);
        return updatedUser;
    }

    @Secured("ROLE_USER")
    @DeleteMapping
    @Operation(summary = "Delete user", security = @SecurityRequirement(name = "apiKey"))
    void delete() {
        log.info("DELETE /api/v1/users");
        String email = authenticationManager.getLoggedInUser();
        AppUser currentUser = userService.findAppUserByEmail(email);
        userService.deleteUserById(currentUser.getId());
    }

    @PermitAll
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/forgot-password")
    @Operation(summary = "Forgot password")
    public void forgotPassword(@Valid @RequestBody ForgotPasswordDto forgotPasswordDto) {
        log.info("POST /api/v1/users/forgot-password {}", forgotPasswordDto.getEmail());
        emailService.sendForgotPasswordMail(forgotPasswordDto.getEmail());
    }

    @PermitAll
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/reset-password")
    @Operation(summary = "Reset password")
    public void forgotPassword(@Valid @RequestBody ResetPasswordDto resetPasswordDto) {
        log.info("POST /api/v1/users/reset-password");
        userService.resetPassword(resetPasswordDto);
    }
}