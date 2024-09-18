package com.juliana.demo_park_api.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class SpaceCreateDto {

    @NotBlank(message = "{NotBlank.spaceCreateDto.code}")
    @Size(min = 4, max = 4, message = "{Size.spaceCreateDto.code}")
    private String code;

    @NotBlank(message = "{NotBlank.spaceCreateDto.status}")
    @Pattern(regexp = "FREE|OCCUPIED", message = "{Pattern.spaceCreateDto.status}")
    private String status;
}
