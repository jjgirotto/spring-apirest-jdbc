package com.juliana.demo_park_api.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.br.CPF;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class ClientCreateDto {

    @NotBlank(message = "{NotBlank.clientCreateDto.name}")
    @Size(message = "{Size.clientCreateDto.name}")
    private String name;

    @Size(min = 11, max = 11, message = "{Size.clientCreateDto.cpf}")
    @CPF(message = "{CPF.clientCreateDto.cpf}")
    private String cpf;
}
