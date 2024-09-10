package com.juliana.demo_park_api.web.dto.mapper;

import com.juliana.demo_park_api.entities.User;
import com.juliana.demo_park_api.web.dto.ResponseDto;
import com.juliana.demo_park_api.web.dto.UserCreateDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.ui.Model;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {

    public static User toUser(UserCreateDto userDto) {
        return new ModelMapper().map(userDto, User.class);
    }
    public static ResponseDto toDto(User user) {
        String role = user.getRole().name().substring("ROLE_".length());
        PropertyMap<User, ResponseDto> props = new PropertyMap<User, ResponseDto>() {
            @Override
            protected void configure() {
                map().setRole(role);
            }
        };
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(props);
        return modelMapper.map(user, ResponseDto.class);
    }

    public static List<ResponseDto> toListDto (List<User> users) {
        return users.stream().map(user -> toDto(user)).collect(Collectors.toList());
    }
}
