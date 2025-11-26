package com.sourcery.defect_registration_system.office.controller;

import com.sourcery.defect_registration_system.office.dto.CreateOfficeRequest;
import com.sourcery.defect_registration_system.office.dto.OfficeResponse;
import com.sourcery.defect_registration_system.office.service.OfficeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Offices", description = "Office management endpoints")
@SecurityRequirement(name = "cookieAuth")
@RestController
@RequiredArgsConstructor
@RequestMapping("/offices")
public class OfficeController {

    private final OfficeService officeService;

    @Operation(
            summary = "Get office by ID",
            description = "Returns the office details for the given UUID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Office returned successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized – user must be authenticated"),
            @ApiResponse(responseCode = "404", description = "Office not found")
    })
    @GetMapping("/{id}")
    public OfficeResponse getOfficeById(@PathVariable("id") UUID id) {
        return officeService.getOfficeById(id);
    }

    @Operation(
            summary = "Get list of offices",
            description = "Returns list of offices."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of offices returned successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized – user must be authenticated"),
    })
    @GetMapping
    public List<OfficeResponse> getAllOffices() {
        return officeService.getAllOffices();
    }

    @Operation(
            summary = "Create a new office"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Office created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "401", description = "Unauthorized – user must be authenticated")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OfficeResponse createOffice(@RequestBody @Valid CreateOfficeRequest request) {
        return officeService.createOffice(request);
    }
}
