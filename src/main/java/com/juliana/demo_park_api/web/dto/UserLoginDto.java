package com.juliana.demo_park_api.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserLoginDto {

    @NotBlank
    @Email(regexp = "^[a-zA-Z0-9._+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "invalid email format")
    private String username;

    @NotBlank
    @Size(min = 6, max = 6)
    private String password;

}
