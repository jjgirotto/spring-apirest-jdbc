package com.juliana.demo_park_api.web.dto.mapper;

import com.juliana.demo_park_api.entities.Client;
import com.juliana.demo_park_api.entities.Space;
import com.juliana.demo_park_api.web.dto.ClientResponseDto;
import com.juliana.demo_park_api.web.dto.SpaceCreateDto;
import com.juliana.demo_park_api.web.dto.SpaceResponseDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SpaceMapper {

    public static Space toSpace(SpaceCreateDto dto) {
        return new ModelMapper().map(dto, Space.class);
    }

    public static SpaceResponseDto toDto(Space space) {
        return new ModelMapper().map(space, SpaceResponseDto.class);
    }
}
