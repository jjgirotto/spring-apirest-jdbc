package com.juliana.demo_park_api.web.controller;

import com.juliana.demo_park_api.JWT.JwtUserDetails;
import com.juliana.demo_park_api.entities.ClientSpace;
import com.juliana.demo_park_api.repositories.projection.ClientSpaceProjection;
import com.juliana.demo_park_api.services.ClientService;
import com.juliana.demo_park_api.services.ClientSpaceService;
import com.juliana.demo_park_api.services.JasperService;
import com.juliana.demo_park_api.services.ParkingService;
import com.juliana.demo_park_api.web.dto.PageableDto;
import com.juliana.demo_park_api.web.dto.ParkingCreateDto;
import com.juliana.demo_park_api.web.dto.ParkingResponseDto;
import com.juliana.demo_park_api.web.dto.mapper.ClientSpaceMapper;
import com.juliana.demo_park_api.web.dto.mapper.PageableMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.ErrorMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.PATH;
import static io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY;

@Tag(name = "Parkings", description = "It contains operations related to parking entry and exit.")
@RestController
@RequestMapping("api/v1/parkings")
@RequiredArgsConstructor
public class ParkingController {

    private final ParkingService parkingService;
    private final ClientSpaceService clientSpaceService;
    private final ClientService clientService;
    private final JasperService jasperService;

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

    @Operation(summary = "Check-out operation", description = "Resource to exit on the parking lot" +
            "Request requires a bearer token, restricted access to Role='ADMIN'",
            security = @SecurityRequirement(name = "security"),
            parameters = { @Parameter(in = PATH, name = "recipt", description = "Recipt number generated by check-in",
                    required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Resource updated",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ParkingResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Recipt number does not exist or " +
                            "the vehicle already got through check-out.",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "Resource not allowed to CLIENT",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PutMapping("/checkout/{recipt}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ParkingResponseDto> checkOUT(@PathVariable String recipt) {
        ClientSpace clientSpace = parkingService.checkOut(recipt);
        ParkingResponseDto dto = ClientSpaceMapper.toDto(clientSpace);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Locate the registries of clients parking by CPF",
            description = "Locate the registries of clients parking by CPF. Request requires a bearer token.",
            security = @SecurityRequirement(name = "security"),
            parameters = {
                    @Parameter(in = PATH, name = "cpf", description =  "CPF client number to be consulted",
                            required = true
                    ),
                    @Parameter(in = QUERY, name = "page", description = "Represents the returned page",
                            content = @Content(schema = @Schema(type = "integer", defaultValue = "0"))
                    ),
                    @Parameter(in = QUERY, name = "size", description = "Represents the number of elements on the page",
                            content = @Content(schema = @Schema(type = "integer", defaultValue = "5"))
                    ),
                    @Parameter(in = QUERY, name = "sort", description = "Pattern of sort 'dateEntry,asc'. ",
                            array = @ArraySchema(schema = @Schema(type = "string", defaultValue = "dateEntry,asc")),
                            hidden = true
                    )
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Resource found",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = PageableDto.class))),
                    @ApiResponse(responseCode = "403", description = "Resource not allowed to client",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class)))
            })
    @GetMapping("/cpf/{cpf}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageableDto> getAllParkingsByCpf(@PathVariable String cpf, @Parameter(hidden = true)
    @PageableDefault(size = 5, sort = "dateEntry",
            direction = Sort.Direction.ASC) Pageable pageable) {
        Page<ClientSpaceProjection> projection = clientSpaceService.searchAllByCpf(cpf, pageable);
        PageableDto dto = PageableMapper.toDto(projection);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Locate the registries of clients parking with logged user",
            description = "Locate the registries of clients parking with logged user. " +
                    "Request requires a bearer token.",
            security = @SecurityRequirement(name = "security"),
            parameters = {
                    @Parameter(in = QUERY, name = "page",
                            content = @Content(schema = @Schema(type = "integer", defaultValue = "0")),
                            description = "Represents the returned page"
                    ),
                    @Parameter(in = QUERY, name = "size",
                            content = @Content(schema = @Schema(type = "integer", defaultValue = "5")),
                            description = "Represents the number of elements on the page"
                    ),
                    @Parameter(in = QUERY, name = "sort", hidden = true,
                            array = @ArraySchema(schema = @Schema(type = "string", defaultValue = "dateEntry,asc")),
                            description = "Pattern of sort 'dateEntry,asc'. ")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Resource found",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ParkingResponseDto.class))),
                    @ApiResponse(responseCode = "403", description = "Resource not allowed to admin",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class)))
            })
    @GetMapping
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<PageableDto> getAllClientsParking(@AuthenticationPrincipal JwtUserDetails user,
                                                                      @Parameter(hidden = true) @PageableDefault(
                                                                              size = 5, sort = "dateEntry",
                                                                              direction = Sort.Direction.ASC) Pageable pageable) {
        Page<ClientSpaceProjection> projection = clientSpaceService.searchAllByUserId(user.getId(), pageable);
        PageableDto dto = PageableMapper.toDto(projection);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/report")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<Void> getReport(HttpServletResponse response, @AuthenticationPrincipal JwtUserDetails user) throws IOException {
        String cpf = clientService.searchByUserId(user.getId()).getCpf();
        jasperService.addParams("CPF", cpf);
        byte[] bytes = jasperService.generatePdf();
        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        response.setHeader("Content-disposition", "inline; filename=" + System.currentTimeMillis() + ".pdf");
        response.getOutputStream().write(bytes);
        return ResponseEntity.ok().build();
    }

}
