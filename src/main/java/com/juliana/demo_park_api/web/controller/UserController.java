package com.juliana.demo_park_api.web.controller;

import com.juliana.demo_park_api.entities.User;
import com.juliana.demo_park_api.services.UserService;
import com.juliana.demo_park_api.web.dto.ResponseDto;
import com.juliana.demo_park_api.web.dto.UserCreateDto;
import com.juliana.demo_park_api.web.dto.UserPasswordDto;
import com.juliana.demo_park_api.web.dto.mapper.UserMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<ResponseDto> create(@Valid @RequestBody UserCreateDto userDto) {
        User obj = userService.save(UserMapper.toUser(userDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.toDto(obj));
    }
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto> getById(@PathVariable Long id) {
        User user = userService.searchById(id);
        return ResponseEntity.ok(UserMapper.toDto(user));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updatePassword(@PathVariable Long id, @RequestBody UserPasswordDto dto) {
        User user = userService.editPassword(id, dto.getCurrentPassword(), dto.getNewPassword(), dto.getConfirmPassword());
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ResponseDto>> getAll() {
        List<User> users = userService.searchAll();
        return ResponseEntity.ok(UserMapper.toListDto(users));
    }
}
