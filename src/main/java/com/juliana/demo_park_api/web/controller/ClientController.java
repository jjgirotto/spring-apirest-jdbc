package com.juliana.demo_park_api.web.controller;

import com.juliana.demo_park_api.JWT.JwtUserDetails;
import com.juliana.demo_park_api.entities.Client;
import com.juliana.demo_park_api.entities.User;
import com.juliana.demo_park_api.services.ClientService;
import com.juliana.demo_park_api.services.UserService;
import com.juliana.demo_park_api.web.dto.ClientCreateDto;
import com.juliana.demo_park_api.web.dto.ClientResponseDto;
import com.juliana.demo_park_api.web.dto.mapper.ClientMapper;
import com.juliana.demo_park_api.web.dto.mapper.UserMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;
    private final UserService userService;

    @PostMapping
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<ClientResponseDto> create(@RequestBody @Valid ClientCreateDto dto, @AuthenticationPrincipal JwtUserDetails userDetails) {
        Client client = ClientMapper.toClient(dto);
        client.setUser(userService.searchById(userDetails.getId()));
        clientService.save(client);
        return ResponseEntity.status(201).body(ClientMapper.toDto(client));
    }
}
