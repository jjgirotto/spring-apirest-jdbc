package com.juliana.demo_park_api.web.dto;

import com.juliana.demo_park_api.entities.User;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ResponseDto {
    private Long id;
    private String username;
    private String role;
}
