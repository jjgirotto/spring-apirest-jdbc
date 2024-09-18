package com.juliana.demo_park_api.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.br.CPF;

@Getter
@Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ParkingCreateDto {
    @NotBlank(message = "{NotBlank.parkingCreateDto.plate}")
    @Size(min = 8, max = 8, message = "{Size.parkingCreateDto.plate}")
    @Pattern(regexp = "[A-Z]{3}-[0-9]{4}", message = "{Pattern.parkingCreateDto.plate}")
    private String plate;
    @NotBlank(message = "{NotBlank.parkingCreateDto.brand}")
    private String brand;
    @NotBlank(message = "{NotBlank.parkingCreateDto.model}")
    private String model;
    @NotBlank(message = "{NotBlank.parkingCreateDto.color}")
    private String color;
    @NotBlank(message = "{NotBlank.parkingCreateDto.clientCpf}")
    @Size(min = 11, max = 11, message = "{Size.parkingCreateDto.clientCpf}")
    @CPF(message = "{CPF.parkingCreateDto.clientCpf}")
    private String clientCpf;
}
