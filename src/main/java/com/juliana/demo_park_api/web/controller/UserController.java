package com.juliana.demo_park_api.web.controller;

import com.juliana.demo_park_api.entities.User;
import com.juliana.demo_park_api.services.UserService;
import com.juliana.demo_park_api.web.dto.ResponseDto;
import com.juliana.demo_park_api.web.dto.UserCreateDto;
import com.juliana.demo_park_api.web.dto.UserPasswordDto;
import com.juliana.demo_park_api.web.dto.mapper.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Tag(name = "Users", description = "It contains operations for register, update and read for users")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Create a new user", description = "Resource to create a new user",
            responses = {
                @ApiResponse(responseCode = "201", description = "Resource created!",
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class))),
                @ApiResponse(responseCode = "409", description = "Username already registered in the system",
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                @ApiResponse(responseCode = "422", description = "Resource not processed by invalid data",
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PostMapping
    public ResponseEntity<ResponseDto> create(@Valid @RequestBody UserCreateDto userDto) {
        User obj = userService.save(UserMapper.toUser(userDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.toDto(obj));
    }

    @Operation(summary = "Get a user by id", description = "Resource to get user by id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Resource found!",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Resource not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto> getById(@PathVariable Long id) {
        User user = userService.searchById(id);
        return ResponseEntity.ok(UserMapper.toDto(user));
    }

    @Operation(summary = "Update password", description = "Update password",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Updated password!",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))),
                    @ApiResponse(responseCode = "404", description = "Resource not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "400", description = "Password does not match",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updatePassword(@PathVariable Long id, @Valid @RequestBody UserPasswordDto dto) {
        User user = userService.editPassword(id, dto.getCurrentPassword(), dto.getNewPassword(), dto.getConfirmPassword());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get all users", description = "Resource to get all users",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Resources found!",
                            content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ResponseDto.class))))
            })
    @GetMapping
    public ResponseEntity<List<ResponseDto>> getAll() {
        List<User> users = userService.searchAll();
        return ResponseEntity.ok(UserMapper.toListDto(users));
    }
}
