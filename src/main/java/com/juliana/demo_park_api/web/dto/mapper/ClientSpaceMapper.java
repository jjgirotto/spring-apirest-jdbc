package com.juliana.demo_park_api.web.dto.mapper;

import com.juliana.demo_park_api.entities.ClientSpace;
import com.juliana.demo_park_api.web.dto.ParkingCreateDto;
import com.juliana.demo_park_api.web.dto.ParkingResponseDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientSpaceMapper {

    public static ClientSpace toClientSpace(ParkingCreateDto dto) {
        return new ModelMapper().map(dto, ClientSpace.class);
    }

    public static ParkingResponseDto toDto(ClientSpace clientSpace) {
        return new ModelMapper().map(clientSpace, ParkingResponseDto.class);
    }
}
