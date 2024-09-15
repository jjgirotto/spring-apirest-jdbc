package com.juliana.demo_park_api.web.dto.mapper;

import com.juliana.demo_park_api.entities.Client;
import com.juliana.demo_park_api.web.dto.ClientCreateDto;
import com.juliana.demo_park_api.web.dto.ClientResponseDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientMapper {

    public static Client toClient(ClientCreateDto createDto) {
        return new ModelMapper().map(createDto, Client.class);
    }

    public static ClientResponseDto toDto(Client client) {
        return new ModelMapper().map(client, ClientResponseDto.class);
    }

}
