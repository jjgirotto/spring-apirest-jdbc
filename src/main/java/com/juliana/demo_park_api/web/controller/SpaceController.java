package com.juliana.demo_park_api.web.controller;

import com.juliana.demo_park_api.entities.Space;
import com.juliana.demo_park_api.services.SpaceService;
import com.juliana.demo_park_api.web.dto.*;
import com.juliana.demo_park_api.web.dto.mapper.SpaceMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

@Tag(name = "Spaces", description = "It contains operations related to spaces")
@RestController
@RequestMapping("api/v1/spaces")
@RequiredArgsConstructor
public class SpaceController {

    private final SpaceService spaceService;

    @Operation(summary = "Create a new space", description = "Resource to create a new space" +
            "Request requires a bearer token, restricted access to Role='ADMIN'",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Resource created!",
                            headers = @Header(name = HttpHeaders.LOCATION, description = "URL resource created")),
                    @ApiResponse(responseCode = "409", description = "Space already registered in the system",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Resource not processed by invalid data",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> create(@RequestBody @Valid SpaceCreateDto dto) {
        Space space = spaceService.save(SpaceMapper.toSpace(dto));
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri().path("/{code}")
                .buildAndExpand(space.getCode())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @Operation(summary = "Get a space by code", description = "Resource to get space by code"  +
            "Request requires a bearer token, restricted access to Role='ADMIN'",
            responses = {
                    @ApiResponse(responseCode = "200", description = "It requires a bearer token. Restricted access to admin",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SpaceResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Resource not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @GetMapping("/{code}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SpaceResponseDto> getByCode(@PathVariable String code) {
        Space space = spaceService.searchByCode(code);
        return ResponseEntity.ok(SpaceMapper.toDto(space));
    }

}
