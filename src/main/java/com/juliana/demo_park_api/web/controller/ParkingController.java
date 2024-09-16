package com.juliana.demo_park_api.web.controller;

import com.juliana.demo_park_api.entities.ClientSpace;
import com.juliana.demo_park_api.services.ParkingService;
import com.juliana.demo_park_api.web.dto.ParkingCreateDto;
import com.juliana.demo_park_api.web.dto.ParkingResponseDto;
import com.juliana.demo_park_api.web.dto.mapper.ClientSpaceMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Tag(name = "Parkings", description = "It contains operations related to parkings.")
@RestController
@RequestMapping("api/v1/parkings")
@RequiredArgsConstructor
public class ParkingController {

    private final ParkingService parkingService;

    @PostMapping("/checkin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ParkingResponseDto> checkIn(@RequestBody @Valid ParkingCreateDto dto) {
        ClientSpace clientSpace = ClientSpaceMapper.toClientSpace(dto);
        parkingService.checkIn(clientSpace);
        ParkingResponseDto responseDto = ClientSpaceMapper.toDto(clientSpace);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri().path("/{recipt}")
                .buildAndExpand(clientSpace.getRecipt())
                .toUri();
        return ResponseEntity.created(location).body(responseDto);
    }
}
