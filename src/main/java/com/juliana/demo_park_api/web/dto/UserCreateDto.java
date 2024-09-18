package com.juliana.demo_park_api.web.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class UserCreateDto {

    @NotBlank(message = "{NotBlank.userCreateDto.username}")
    @Email(message = "{Email.userCreateDto.username}", regexp = "^[a-z0-9.+-]+@[a-z0-9.-]+\\.[a-z]{2,}$")
    private String username;
    @NotBlank(message = "{NotBlank.userCreateDto.password}")
    @Size(min = 6, max = 6, message = "{Size.userCreateDto.password}")
    private String password;
}
