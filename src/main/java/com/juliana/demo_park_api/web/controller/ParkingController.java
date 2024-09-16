package com.juliana.demo_park_api.web.controller;

import com.juliana.demo_park_api.entities.ClientSpace;
import com.juliana.demo_park_api.services.ClientSpaceService;
import com.juliana.demo_park_api.services.ParkingService;
import com.juliana.demo_park_api.web.dto.ParkingCreateDto;
import com.juliana.demo_park_api.web.dto.ParkingResponseDto;
import com.juliana.demo_park_api.web.dto.mapper.ClientSpaceMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.ErrorMessage;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.PATH;

@Tag(name = "Parkings", description = "It contains operations related to parking entry and exit.")
@RestController
@RequestMapping("api/v1/parkings")
@RequiredArgsConstructor
public class ParkingController {

    private final ParkingService parkingService;
    private final ClientSpaceService clientSpaceService;

    @Operation(summary = "Check-in operation", description = "Resource to entry on the parking lot" +
            "Request requires a bearer token, restricted access to Role='ADMIN'",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Resource created!",
                            headers = @Header(name = HttpHeaders.LOCATION, description = "URL created"),
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ParkingResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Possible causes: <br/>" +
                            "CPF not registered in the system; <br/> " +
                            "No free space found.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Resource not processed by invalid data",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "Resource not allowed to Client",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PostMapping("/checkin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ParkingResponseDto> checkIN(@RequestBody @Valid ParkingCreateDto dto) {
        ClientSpace clientSpace = ClientSpaceMapper.toClientSpace(dto);
        parkingService.checkIn(clientSpace);
        ParkingResponseDto responseDto = ClientSpaceMapper.toDto(clientSpace);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri().path("/{recipt}")
                .buildAndExpand(clientSpace.getRecipt())
                .toUri();
        return ResponseEntity.created(location).body(responseDto);
    }

    @Operation(summary = "Locate a parking vehicle", description = "Resource to return a vehicle" +
            "by recipt number. Request requires a bearer token.",
            security = @SecurityRequirement(name = "security"),
            parameters = {
                    @Parameter(in = PATH, name = "recipt", description = "Recipt number generated by check-in")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Resource found",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ParkingResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Recipt not found.",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class)))
            })
    @GetMapping("/checkin/{recipt}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
    public ResponseEntity<ParkingResponseDto> getByRecipt(@PathVariable String recipt) {
        ClientSpace clientSpace = clientSpaceService.searchByRecipt(recipt);
        ParkingResponseDto dto = ClientSpaceMapper.toDto(clientSpace);
        return ResponseEntity.ok(dto);
    }
}
