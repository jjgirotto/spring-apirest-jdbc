package com.juliana.demo_park_api.web.controller;

import com.juliana.demo_park_api.JWT.JwtUserDetails;
import com.juliana.demo_park_api.entities.Client;
import com.juliana.demo_park_api.entities.User;
import com.juliana.demo_park_api.services.ClientService;
import com.juliana.demo_park_api.services.UserService;
import com.juliana.demo_park_api.web.dto.ClientCreateDto;
import com.juliana.demo_park_api.web.dto.ClientResponseDto;
import com.juliana.demo_park_api.web.dto.UserResponseDto;
import com.juliana.demo_park_api.web.dto.mapper.ClientMapper;
import com.juliana.demo_park_api.web.dto.mapper.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Clients", description = "It contains operations for register, update and read for clients")
@RestController
@RequestMapping("api/v1/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;
    private final UserService userService;

    @Operation(summary = "Create a new client", description = "Resource to create a new client attached with a registered user" +
            "Request requires a bearer token, restricted access to Role='CLIENT'",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Resource created!",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(responseCode = "409", description = "CPF already registered in the system",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Resource not processed by invalid data",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "Resource not allowed to Admin",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PostMapping
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<ClientResponseDto> create(@RequestBody @Valid ClientCreateDto dto, @AuthenticationPrincipal JwtUserDetails userDetails) {
        Client client = ClientMapper.toClient(dto);
        client.setUser(userService.searchById(userDetails.getId()));
        clientService.save(client);
        return ResponseEntity.status(201).body(ClientMapper.toDto(client));
    }

    @Operation(summary = "Get a client by id", description = "Resource to get client by id" +
            "Request requires a bearer token, restricted access to Role='CLIENT'",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Resource found!",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(responseCode = "403", description = "Client not allowed to access this resource",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "404", description = "Client not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClientResponseDto> getById(@PathVariable Long id) {
        Client client = clientService.searchById(id);
        return ResponseEntity.ok(ClientMapper.toDto(client));
    }
}
