package com.juliana.demo_park_api.web.dto;

import jakarta.persistence.Column;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class UserCreateDto {
    private String username;
    private String password;
}
