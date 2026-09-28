package com.backend.endpoint.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResetPasswordDto {

    @NotBlank(message = "Token required!")
    private String token;

    @NotBlank(message = "Password required")
    @Length(min = 8, message = "Minimum password length is 8!")
    private String password;

    @NotBlank(message = "Password confirmation required!")
    @Length(min = 8, message = "Minimum password length is 8!")
    private String passwordConfirmation;
}